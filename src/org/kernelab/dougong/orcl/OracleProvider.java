package org.kernelab.dougong.orcl;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.opr.ModuloOperator;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.core.meta.Entitys.GenerateValueColumns;
import org.kernelab.dougong.core.util.KeysFetcher;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.orcl.ddl.OracleForeignKey;
import org.kernelab.dougong.orcl.ddl.OraclePrimaryKey;
import org.kernelab.dougong.orcl.ddl.table.OracleCreateTable;
import org.kernelab.dougong.orcl.ddl.table.OracleDropTable;
import org.kernelab.dougong.orcl.dml.OracleAllItems;
import org.kernelab.dougong.orcl.dml.OracleDelete;
import org.kernelab.dougong.orcl.dml.OracleInsert;
import org.kernelab.dougong.orcl.dml.OracleItems;
import org.kernelab.dougong.orcl.dml.OracleJoin;
import org.kernelab.dougong.orcl.dml.OracleReference;
import org.kernelab.dougong.orcl.dml.OracleSelect;
import org.kernelab.dougong.orcl.dml.OracleSetopr;
import org.kernelab.dougong.orcl.dml.OracleStringItem;
import org.kernelab.dougong.orcl.dml.OracleUpdate;
import org.kernelab.dougong.orcl.dml.cond.OracleComparisonCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleNullCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleRangeCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleRegexpLikeCondition;
import org.kernelab.dougong.orcl.dml.opr.OracleCaseDecideExpression;
import org.kernelab.dougong.orcl.dml.opr.OracleCaseSwitchExpression;
import org.kernelab.dougong.orcl.dml.opr.OracleJointOperator;
import org.kernelab.dougong.orcl.dml.opr.OracleModuloOperator;
import org.kernelab.dougong.orcl.util.OracleKeysFetcher;
import org.kernelab.dougong.semi.AbstractProvider;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;

public class OracleProvider extends AbstractProvider
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3963780549845482491L;

	public static final char	TEXT_BOUNDARY_CHAR	= '"';

	public static final String	TEXT_BOUNDARY_MARK	= "\"";

	public static final String	TEXT_BOUNDARY_ESC	= TEXT_BOUNDARY_MARK + TEXT_BOUNDARY_MARK;

	public static final String	RETURN_VAR_PREFIX	= ":ret_";

	@Override
	public OracleAllItems provideAllItems(View view)
	{
		return new OracleAllItems(view);
	}

	@Override
	public OracleCaseDecideExpression provideCaseExpression()
	{
		return new OracleCaseDecideExpression(this);
	}

	@Override
	public OracleCaseSwitchExpression provideCaseExpression(Expression value)
	{
		return (OracleCaseSwitchExpression) new OracleCaseSwitchExpression(this).caseValue(value);
	}

	@Override
	public OracleColumn provideColumn(View view, String name, Field field)
	{
		return new OracleColumn(view, name, field);
	}

	@Override
	public OracleComparisonCondition provideComparisonCondition()
	{
		return this.provideProvider(new OracleComparisonCondition());
	}

	@Override
	public OracleCreateTable provideCreateTable()
	{
		return this.provideProvider(new OracleCreateTable());
	}

	@Override
	public String provideDatetimeFormat(String format)
	{
		return format //
				.replaceAll("y", "Y") //
				.replaceAll("H+", "HH24") //
				.replaceAll("h+", "HH12") //
				.replaceAll("D+", "DDD") //
				.replaceAll("d+", "DD") //
				.replaceAll("M+", "MM") //
				.replaceAll("m+", "MI") //
				.replaceAll("a+", "PM") //
				.replaceAll("S+", "FF") //
				.replaceAll("s+", "SS") //
				.replaceAll("X+", "TZH") //
		;
	}

	@Override
	public OracleDelete provideDelete()
	{
		return this.provideProvider(new OracleDelete());
	}

	@Override
	public ResultSet provideDoInsertAndReturnGenerates(SQLKit kit, SQL sql, Insert insert, Map<String, Object> params,
			GenerateValueColumns generates, Column[] returns) throws SQLException
	{
		Expression[] rets = new Expression[returns.length];
		for (int i = 0; i < returns.length; i++)
		{
			Column column = returns[i];
			String select = Entitys.getColumnSelectExpression(this, column);
			rets[i] = select != null ? sql.expr(select) : column;
		}

		insert.to(OracleInsert.class).returning(rets);

		OraclePreparedStatement ps = (OraclePreparedStatement) kit.unwrap(OracleConnection.class)
				.prepareStatement(insert.toString(), params);

		kit.unwrap(null);

		int offset = kit.getParameter(ps).size() + 1;

		for (int i = 0; i < returns.length; i++)
		{
			ps.registerReturnParameter(offset + i, this.provideColumnType(returns[i]));
		}

		kit.update(ps, params);

		return ps.getReturnResultSet();
	}

	@Override
	public OracleDropTable provideDropTable()
	{
		return this.provideProvider(new OracleDropTable());
	}

	@Override
	public String provideEscapeValueLiterally(Object value)
	{
		if (value == null)
		{
			return SQL.NULL;
		}
		else
		{
			return value.toString().replace("'", "''");
		}
	}

	@Override
	public ForeignKey provideForeignKey(PrimaryKey reference, Entity entity, Column... columns)
	{
		return provideProvider(new OracleForeignKey(reference, entity, columns));
	}

	@Override
	public OracleInsert provideInsert()
	{
		return provideProvider(new OracleInsert());
	}

	@Override
	public ComposableCondition provideIsEmptyCondition(Expression expr)
	{
		return provideLogicalCondition()
				.and(expr.isNull().or(provideStringItem("NVL(LENGTH(" + expr.toString(new StringBuilder()) + "),0)")
						.eq(provideStringItem("0"))));
	}

	@Override
	public ComposableCondition provideIsNotEmptyCondition(Expression expr)
	{
		return provideLogicalCondition()
				.and(expr.isNotNull().and(provideStringItem("NVL(LENGTH(" + expr.toString(new StringBuilder()) + "),0)")
						.ne(provideStringItem("0"))));
	}

	@Override
	public OracleItems provideItems()
	{
		return provideProvider(new OracleItems());
	}

	@Override
	public OracleJoin provideJoin()
	{
		return new OracleJoin();
	}

	@Override
	public OracleJointOperator provideJointOperator()
	{
		return provideProvider(new OracleJointOperator());
	}

	@Override
	public KeysFetcher provideKeysFetcher()
	{
		return new OracleKeysFetcher();
	}

	@Override
	public ModuloOperator provideModuloOperator()
	{
		return provideProvider(new OracleModuloOperator());
	}

	@Override
	public String provideNameText(String name)
	{
		if (name != null)
		{
			return TEXT_BOUNDARY_CHAR + name.replace(TEXT_BOUNDARY_MARK, TEXT_BOUNDARY_ESC) + TEXT_BOUNDARY_CHAR;
		}
		else
		{
			return SQL.NULL;
		}
	}

	@Override
	public OracleNullCondition provideNullCondition()
	{
		return this.provideProvider(new OracleNullCondition());
	}

	@Override
	public StringBuilder provideOutputColumnExpress(StringBuilder buffer, Column column)
	{
		if (!column.isUsingByJoin())
		{
			String alias = column.view().provider().provideAliasLabel(this.provideViewAlias(column.view()));
			if (alias != null)
			{
				buffer.append(alias);
				buffer.append('.');
			}
		}
		return this.provideOutputNameText(buffer, column.name());
	}

	@Override
	public StringBuilder provideOutputFunction(StringBuilder buffer, Function function)
	{
		if (!function.isPseudo())
		{
			this.provideOutputMember(buffer, function, -1);
		}
		else
		{
			if (Tools.notNullOrEmpty(function.schema()))
			{
				if (Tools.notNullOrEmpty(function.catalog()))
				{
					this.provideOutputNameText(buffer, function.catalog());
					buffer.append(OBJECT_SEPARATOR_CHAR);
				}
				this.provideOutputNameText(buffer, function.schema());
				buffer.append(OBJECT_SEPARATOR_CHAR);
			} // SYSDATE MUST NOT be surrounded with quotes
			buffer.append(function.name());
		}

		Expression[] args = function.args();

		if (!function.isPseudo() || (args != null && args.length > 0))
		{
			buffer.append('(');
		}

		if (args != null && args.length > 0)
		{
			boolean first = true;

			for (Expression expr : args)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.outputExpr(buffer, expr);
			}
		}

		if (!function.isPseudo() || (args != null && args.length > 0))
		{
			buffer.append(')');
		}

		return buffer;
	}

	public StringBuilder provideOutputReturningClause(StringBuilder buffer, Expression[] returning)
	{
		if (returning != null && returning.length > 0)
		{
			buffer.append(" RETURNING ");

			StringBuilder into = new StringBuilder();
			int index = 1;
			for (Expression expr : returning)
			{
				if (index > 1)
				{
					buffer.append(',');
					into.append(',');
				}
				expr.toStringExpress(buffer);
				into.append(OracleProvider.RETURN_VAR_PREFIX + (index++));
			}

			buffer.append(" INTO ");
			buffer.append(into);
		}

		return buffer;
	}

	@Override
	public PrimaryKey providePrimaryKey(Entity entity, Column... columns)
	{
		return provideProvider(new OraclePrimaryKey(entity, columns));
	}

	@Override
	public OracleRangeCondition provideRangeCondition()
	{
		return this.provideProvider(new OracleRangeCondition());
	}

	@Override
	public OracleReference provideReference(View view, String name)
	{
		return provideProvider(new OracleReference(view, name));
	}

	@Override
	public OracleRegexpLikeCondition provideRegexpCondition()
	{
		return this.provideProvider(new OracleRegexpLikeCondition());
	}

	@Override
	public OracleSelect provideSelect()
	{
		return this.provideProvider(new OracleSelect());
	}

	@Override
	public OracleSetopr provideSetopr()
	{
		return new OracleSetopr();
	}

	@Override
	public String provideStandardTypeName(String name)
	{
		name = super.provideStandardTypeName(name);

		name = name.replaceFirst("\\d+$", "");

		if ("NUMBER".equals(name) || "INT".equals(name) || "INTEGER".equals(name))
		{
			return "NUMERIC";
		}
		else if ("DATE".equals(name))
		{
			return "TIMESTAMP";
		}
		else if ("LONG".equals(name))
		{
			return "LONGVARCHAR";
		}
		else if ("RAW".equals(name))
		{
			return "VARBINARY";
		}
		else if ("LONG RAW".equals(name))
		{
			return "LONGVARBINARY";
		}
		else
		{
			return name;
		}
	}

	@Override
	public OracleStringItem provideStringItem(String item)
	{
		return (OracleStringItem) provideProvider(new OracleStringItem(this).setString(item));
	}

	@Override
	public String provideTextContent(CharSequence text)
	{
		return text.toString().replace("'", "''");
	}

	@Override
	public OracleUpdate provideUpdate()
	{
		return this.provideProvider(new OracleUpdate());
	}
}
