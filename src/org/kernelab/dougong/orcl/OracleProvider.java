package org.kernelab.dougong.orcl;

import java.lang.reflect.Field;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.opr.ArithmeticOperable;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;
import org.kernelab.dougong.core.util.KeysFetcher;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.orcl.ddl.OracleForeignKey;
import org.kernelab.dougong.orcl.ddl.OraclePrimaryKey;
import org.kernelab.dougong.orcl.dml.OracleAllItems;
import org.kernelab.dougong.orcl.dml.OracleDelete;
import org.kernelab.dougong.orcl.dml.OracleInsert;
import org.kernelab.dougong.orcl.dml.OracleItems;
import org.kernelab.dougong.orcl.dml.OracleJoin;
import org.kernelab.dougong.orcl.dml.OraclePriorExpression;
import org.kernelab.dougong.orcl.dml.OracleReference;
import org.kernelab.dougong.orcl.dml.OracleSelect;
import org.kernelab.dougong.orcl.dml.OracleSortable;
import org.kernelab.dougong.orcl.dml.OracleStringItem;
import org.kernelab.dougong.orcl.dml.OracleUpdate;
import org.kernelab.dougong.orcl.dml.cond.OracleComparisonCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleLikeCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleLogicalCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleMembershipCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleNullCondition;
import org.kernelab.dougong.orcl.dml.cond.OracleRangeCondition;
import org.kernelab.dougong.orcl.dml.opr.OracleArithmeticOperator;
import org.kernelab.dougong.orcl.dml.opr.OracleCaseDecideExpression;
import org.kernelab.dougong.orcl.dml.opr.OracleCaseSwitchExpression;
import org.kernelab.dougong.orcl.dml.opr.OracleJointOperator;
import org.kernelab.dougong.orcl.util.OracleKeysFetcher;
import org.kernelab.dougong.semi.AbstractProvider;
import org.kernelab.dougong.semi.dml.AbstractMerge;

public class OracleProvider extends AbstractProvider
{
	public static final char TEXT_BOUNDARY_CHAR = '"';

	public static void main(String[] args)
	{
		// SQL q = new SQL(new OracleProvider());
	}

	public OraclePriorExpression prior(Expression expr)
	{
		return providePriorExpression(expr);
	}

	public OracleAllItems provideAllItems(View view)
	{
		return new OracleAllItems(view);
	}

	public OracleCaseDecideExpression provideCaseExpression()
	{
		return new OracleCaseDecideExpression(this);
	}

	public OracleCaseSwitchExpression provideCaseExpression(Expression value)
	{
		return (OracleCaseSwitchExpression) new OracleCaseSwitchExpression(this).caseValue(value);
	}

	public OracleColumn provideColumn(View view, String name, Field field)
	{
		return new OracleColumn(view, name, field);
	}

	public OracleComparisonCondition provideComparisonCondition()
	{
		return new OracleComparisonCondition();
	}

	public OracleDelete provideDelete()
	{
		return this.provideProvider(new OracleDelete());
	}

	public DivideOperator provideDivideOperator()
	{
		return this.provideProvider(new OracleArithmeticOperator(ArithmeticOperable.DIVIDE));
	}

	public ForeignKey provideForeignKey(PrimaryKey reference, Entity entity, Column... columns)
	{
		return provideProvider(new OracleForeignKey(reference, entity, columns));
	}

	public OracleInsert provideInsert()
	{
		return provideProvider(new OracleInsert());
	}

	public OracleItems provideItems()
	{
		return provideProvider(new OracleItems());
	}

	public OracleJoin provideJoin()
	{
		return new OracleJoin();
	}

	public OracleJointOperator provideJointOperator()
	{
		return provideProvider(new OracleJointOperator());
	}

	public KeysFetcher provideKeysFetcher()
	{
		return new OracleKeysFetcher();
	}

	public OracleLikeCondition provideLikeCondition()
	{
		return new OracleLikeCondition();
	}

	public OracleLogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}

	public OracleMembershipCondition provideMembershipCondition()
	{
		return new OracleMembershipCondition();
	}

	public AbstractMerge provideMerge()
	{
		return provideProvider(new AbstractMerge());
	}

	public MinusOperator provideMinusOperator()
	{
		return provideProvider(new OracleArithmeticOperator(ArithmeticOperable.MINUS));
	}

	public MultiplyOperator provideMultiplyOperator()
	{
		return provideProvider(new OracleArithmeticOperator(ArithmeticOperable.MULTIPLY));
	}

	public String provideNameText(String name)
	{
		if (name != null)
		{
			return TEXT_BOUNDARY_CHAR + name + TEXT_BOUNDARY_CHAR;
		}
		else
		{
			return SQL.NULL;
		}
	}

	public OracleNullCondition provideNullCondition()
	{
		return new OracleNullCondition();
	}

	public StringBuilder provideOutputFunction(StringBuilder buffer, Function function)
	{
		if (!function.isPseudoColumn())
		{
			this.provideOutputMember(buffer, function);
		}
		else
		{
			if (Tools.notNullOrEmpty(function.schema()))
			{
				this.provideOutputNameText(buffer, function.schema());
				buffer.append(OBJECT_SEPARATOR_CHAR);
			} // SYSDATE MUST NOT be surrounded with quotes
			buffer.append(function.name());
		}

		Expression[] args = function.args();

		if (!function.isPseudoColumn())
		{
			buffer.append('(');

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

			buffer.append(')');
		}

		return buffer;
	}

	public StringBuilder provideOutputOrder(StringBuilder buffer, Sortable sort)
	{
		if (buffer != null && sort != null)
		{
			buffer.append(' ');

			if (sort.ascending())
			{
				buffer.append("ASC");
			}
			else
			{
				buffer.append("DESC");
			}

			if (sort instanceof OracleSortable)
			{
				OracleSortable os = (OracleSortable) sort;

				switch (os.getNullsPosition())
				{
					case OracleSortable.NULLS_FIRST:
						buffer.append(' ').append(OracleSortable.NULLS_FIRST_OPR);
						break;

					case OracleSortable.NULLS_LAST:
						buffer.append(' ').append(OracleSortable.NULLS_LAST_OPR);
						break;
				}
			}
		}
		return buffer;
	}

	public PlusOperator providePlusOperator()
	{
		return provideProvider(new OracleArithmeticOperator(ArithmeticOperable.PLUS));
	}

	public PrimaryKey providePrimaryKey(Entity entity, Column... columns)
	{
		return provideProvider(new OraclePrimaryKey(entity, columns));
	}

	public OraclePriorExpression providePriorExpression(Expression expr)
	{
		return new OraclePriorExpression(this, expr);
	}

	public OracleRangeCondition provideRangeCondition()
	{
		return new OracleRangeCondition();
	}

	public OracleReference provideReference(View view, Expression expr)
	{
		return provideProvider(new OracleReference(view, expr));
	}

	public OracleSelect provideSelect()
	{
		return this.provideProvider(new OracleSelect());
	}

	public OracleStringItem provideStringItem(String item)
	{
		return (OracleStringItem) provideProvider(new OracleStringItem(this).setString(item));
	}

	public OracleUpdate provideUpdate()
	{
		return this.provideProvider(new OracleUpdate());
	}

	public OracleSortable sort(Sortable sort)
	{
		return (OracleSortable) sort;
	}
}
