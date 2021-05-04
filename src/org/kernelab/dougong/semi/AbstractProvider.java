package org.kernelab.dougong.semi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;

import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.Member;
import org.kernelab.dougong.core.Partitioned;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.AbsoluteKey;
import org.kernelab.dougong.core.dml.Alias;
import org.kernelab.dougong.core.dml.Aliases;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Label;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.dml.param.ByteParam;
import org.kernelab.dougong.core.dml.param.DateParam;
import org.kernelab.dougong.core.dml.param.DecimalParam;
import org.kernelab.dougong.core.dml.param.DoubleParam;
import org.kernelab.dougong.core.dml.param.FloatParam;
import org.kernelab.dougong.core.dml.param.IntParam;
import org.kernelab.dougong.core.dml.param.IterableParam;
import org.kernelab.dougong.core.dml.param.LongParam;
import org.kernelab.dougong.core.dml.param.ShortParam;
import org.kernelab.dougong.core.dml.param.StringParam;
import org.kernelab.dougong.core.dml.param.TimestampParam;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.ddl.AbstractAbsoluteKey;
import org.kernelab.dougong.semi.dml.AbstractPrimitive;
import org.kernelab.dougong.semi.dml.AbstractTotalItems;
import org.kernelab.dougong.semi.dml.cond.AbstractLikeCondition;
import org.kernelab.dougong.semi.dml.opr.AbstractStringExpressionResult;
import org.kernelab.dougong.semi.dml.param.AbstractByteParam;
import org.kernelab.dougong.semi.dml.param.AbstractDateParam;
import org.kernelab.dougong.semi.dml.param.AbstractDecimalParam;
import org.kernelab.dougong.semi.dml.param.AbstractDoubleParam;
import org.kernelab.dougong.semi.dml.param.AbstractFloatParam;
import org.kernelab.dougong.semi.dml.param.AbstractIntParam;
import org.kernelab.dougong.semi.dml.param.AbstractIterableParam;
import org.kernelab.dougong.semi.dml.param.AbstractLongParam;
import org.kernelab.dougong.semi.dml.param.AbstractShortParam;
import org.kernelab.dougong.semi.dml.param.AbstractStringParam;
import org.kernelab.dougong.semi.dml.param.AbstractTimestampParam;

public abstract class AbstractProvider extends AbstractCastable implements Provider
{
	public static final char	OBJECT_SEPARATOR_CHAR	= '.';

	public static final char	STRING_QUOTE_BOUNDARY	= '\'';

	private SQL					sql;

	public AbsoluteKey provideAbsoluteKey(Entity entity, Column... columns)
	{
		return provideProvider(new AbstractAbsoluteKey(entity, columns));
	}

	public String provideAliasLabel(String alias)
	{
		return Tools.notNullOrEmpty(alias) ? provideNameText(alias) : null;
	}

	public int provideColumnType(Column column)
	{
		if (column == null)
		{
			return Types.NULL;
		}

		TypeMeta type = column.field() == null ? null : column.field().getAnnotation(TypeMeta.class);

		if (type != null)
		{
			Integer t = provideTypeByName(type.type());

			if (t != null)
			{
				return t;
			}
		}

		return Types.OTHER;
	}

	public String provideDefaultDateFormat()
	{
		return "yyyy-MM-dd";
	}

	public String provideDefaultDateTimeFormat()
	{
		return "yyyy-MM-dd HH:mm:ss";
	}

	public String provideDefaultTimestampFormat()
	{
		return "yyyy-MM-dd HH:mm:ss.SSS";
	}

	public ResultSet provideDoInsertAndReturnGenerates(SQLKit kit, SQL sql, Insert insert, Map<String, Object> params,
			Column[] returns) throws SQLException
	{
		String[] genames = new String[returns.length];
		int i = 0;
		for (Column column : returns)
		{
			genames[i] = column.name();
			i++;
		}
		PreparedStatement ps = kit.prepareStatement(insert.toString(), params, genames);
		kit.update(ps, params);
		return ps.getGeneratedKeys();
	}

	public <T extends Function> T provideFunction(Class<T> cls)
	{
		try
		{
			return this.provideProvider(cls.newInstance());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String provideHint(String hint)
	{
		if (Tools.isNullOrWhite(hint))
		{
			return null;
		}
		else
		{
			return "/*+ " + hint + " */";
		}
	}

	@Override
	public Expression provideLikeAmongPattern(Expression pattern, String escape)
	{
		Expression wildcard = provideStringItem("'%'");
		return provideJointOperator().operate(wildcard, this.provideLikePatternEscaped(pattern, escape), wildcard);
	}

	@Override
	public Expression provideLikeHeadPattern(Expression pattern, String escape)
	{
		Expression wildcard = provideStringItem("'%'");
		return provideJointOperator().operate(this.provideLikePatternEscaped(pattern, escape), wildcard);
	}

	public Expression provideLikePatternDefaultEscape()
	{
		return provideStringItem("'" + provideEscapeValueLiterally(AbstractLikeCondition.ESCAPE) + "'");
	}

	public Expression provideLikePatternEscaped(Expression pattern, String escape)
	{
		if (Tools.isNullOrEmpty(escape))
		{
			return pattern;
		}
		String expr = pattern.toStringExpress(new StringBuilder()).toString();
		String esc = provideEscapeValueLiterally(escape);
		return provideStringItem("REPLACE(REPLACE(REPLACE(" + expr + ",'" + esc + "','" + esc + esc + "')" //
				+ ",'%','" + esc + "%'),'_','" + esc + "_')");
	}

	@Override
	public Expression provideLikeTailPattern(Expression pattern, String escape)
	{
		Expression wildcard = provideStringItem("'%'");
		return provideJointOperator().operate(wildcard, this.provideLikePatternEscaped(pattern, escape));
	}

	public Item provideNullItem()
	{
		return this.provideStringItem(SQL.NULL);
	}

	public String provideNumberLiteral(Number number)
	{
		if (number == null)
		{
			return SQL.NULL;
		}
		else
		{
			return number.toString();
		}
	}

	public StringBuilder provideOutputAlias(StringBuilder buffer, Alias alias)
	{
		if (buffer != null && alias != null)
		{
			if (alias instanceof Aliases && ((Aliases) alias).aliases() != null)
			{
				buffer.append(" (");
				boolean first = true;
				for (String al : ((Aliases) alias).aliases())
				{
					if (first)
					{
						first = false;
					}
					else
					{
						buffer.append(',');
					}
					buffer.append(this.provideAliasLabel(al));
				}
				buffer.append(')');
			}
			else
			{
				String label = this.provideAliasLabel(alias.alias());
				if (label != null)
				{
					buffer.append(' ');
					buffer.append(label);
				}
			}
		}
		return buffer;
	}

	public StringBuilder provideOutputColumnExpress(StringBuilder buffer, Column column)
	{
		String alias = this.provideAliasLabel(column.view().alias());
		if (alias != null)
		{
			buffer.append(alias);
			buffer.append('.');
		}
		this.provideOutputNameText(buffer, column.name());
		return buffer;
	}

	public StringBuilder provideOutputColumnReference(StringBuilder buffer, Column column)
	{
		String alias = this.provideAliasLabel(column.view().alias());
		if (alias != null)
		{
			buffer.append(alias);
			buffer.append('.');
		}
		return this.provideOutputNameText(buffer, column.name());
	}

	public StringBuilder provideOutputColumnSelect(StringBuilder buffer, Column column)
	{
		String select = Entitys.getColumnSelectExpression(column);
		if (select == null)
		{
			this.provideOutputColumnExpress(buffer, column);
		}
		else
		{
			buffer.append(select);
		}
		return Utils.outputAlias(this, buffer, column);
	}

	public StringBuilder provideOutputMember(StringBuilder buffer, Member member)
	{
		if (buffer != null)
		{
			if (Tools.notNullOrEmpty(member.schema()))
			{
				this.provideOutputNameText(buffer, member.schema());
				buffer.append(OBJECT_SEPARATOR_CHAR);
			}
			this.provideOutputNameText(buffer, member.name());
		}
		return buffer;
	}

	public StringBuilder provideOutputNameText(StringBuilder buffer, String name)
	{
		if (buffer != null)
		{
			buffer.append(this.provideNameText(name));
		}
		return buffer;
	}

	public StringBuilder provideOutputTableName(StringBuilder buffer, Table table)
	{
		this.provideOutputMember(buffer, table);
		return buffer;
	}

	public StringBuilder provideOutputTableNameAliased(StringBuilder buffer, Table table)
	{
		this.provideOutputTableName(buffer, table);
		this.provideOutputTablePartitionClause(buffer, table);
		this.provideOutputAlias(buffer, table);
		return buffer;
	}

	public StringBuilder provideOutputTablePartitionClause(StringBuilder buffer, Partitioned part)
	{
		String name = part.partition();
		if (name != null)
		{
			buffer.append(" PARTITION (");
			buffer.append(name);
			buffer.append(')');
		}
		return buffer;
	}

	public StringBuilder provideOutputWithSubqueryAliased(StringBuilder buffer, Subquery query)
	{
		if (buffer != null && query != null)
		{
			buffer.append(this.provideAliasLabel(query.withName()));
			this.provideOutputAlias(buffer, query);
		}
		return buffer;
	}

	public StringItem provideParameter(String name)
	{
		return provideStringItem("?" + name + "?");
	}

	public DecimalParam provideParameter(String name, BigDecimal value)
	{
		return provideProvider(new AbstractDecimalParam(name, value));
	}

	public ByteParam provideParameter(String name, Byte value)
	{
		return provideProvider(new AbstractByteParam(name, value));
	}

	public DateParam provideParameter(String name, Date value)
	{
		return provideProvider(new AbstractDateParam(name, value));
	}

	public DoubleParam provideParameter(String name, Double value)
	{
		return provideProvider(new AbstractDoubleParam(name, value));
	}

	public FloatParam provideParameter(String name, Float value)
	{
		return provideProvider(new AbstractFloatParam(name, value));
	}

	public IntParam provideParameter(String name, Integer value)
	{
		return provideProvider(new AbstractIntParam(name, value));
	}

	public IterableParam provideParameter(String name, Iterable<?> value)
	{
		return provideProvider(new AbstractIterableParam(name, value));
	}

	public LongParam provideParameter(String name, Long value)
	{
		return provideProvider(new AbstractLongParam(name, value));
	}

	public ShortParam provideParameter(String name, Short value)
	{
		return provideProvider(new AbstractShortParam(name, value));
	}

	public StringParam provideParameter(String name, String value)
	{
		return provideProvider(new AbstractStringParam(name, value));
	}

	public TimestampParam provideParameter(String name, Timestamp value)
	{
		return provideProvider(new AbstractTimestampParam(name, value));
	}

	public String provideParameterExpression(String name)
	{
		return "?" + name + "?";
	}

	public AbstractPrimitive providePrimitive()
	{
		return this.provideProvider(new AbstractPrimitive());
	}

	@SuppressWarnings("unchecked")
	public <T extends Providable> T provideProvider(Providable providable)
	{
		if (providable != null)
		{
			providable.provider(this);
		}
		return (T) providable;
	}

	protected <T extends Object> T provideProvider(T object)
	{
		if (object instanceof Providable)
		{
			((Providable) object).provider(this);
		}
		return object;
	}

	public String provideReferName(Expression expr)
	{
		if (expr instanceof Label)
		{
			return ((Label) expr).label();
		}

		String refer = null;

		if (expr instanceof Alias)
		{
			refer = ((Alias) expr).alias();
		}

		if (refer == null)
		{
			if (expr instanceof Column)
			{
				refer = ((Column) expr).name();
			}
			else
			{
				refer = expr.toStringExpress(new StringBuilder()).toString();
			}
		}

		return refer;
	}

	public Result provideResult(String expression)
	{
		return provideProvider(new AbstractStringExpressionResult(expression));
	}

	public SQL provideSQL()
	{
		if (this.sql == null)
		{
			this.sql = new SQL(this);
		}
		return this.sql;
	}

	public String provideStandardTypeName(String name)
	{
		if (name == null)
		{
			return null;
		}

		int end = name.indexOf('(');

		if (end == -1)
		{
			end = name.length();
		}

		return name.substring(0, end).trim().toUpperCase();
	}

	public <T extends Subquery> T provideSubquery(Class<T> cls)
	{
		return provideView(cls);
	}

	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select)
	{
		try
		{
			T s = cls.newInstance();
			s.select(select);
			return this.provideProvider(s);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public <T extends Table> T provideTable(Class<T> cls)
	{
		return provideView(cls);
	}

	public String provideTextLiteral(String text)
	{
		if (text == null)
		{
			return SQL.NULL;
		}
		else
		{
			return STRING_QUOTE_BOUNDARY + this.provideTextContent(text) + STRING_QUOTE_BOUNDARY;
		}
	}

	public Result provideToLowerCase(Expression expr)
	{
		return provideResult("LOWER(" + expr.toStringExpress(new StringBuilder()) + ")");
	}

	public AllItems provideTotalItems()
	{
		return new AbstractTotalItems();
	}

	public Result provideToUpperCase(Expression expr)
	{
		return provideResult("UPPER(" + expr.toStringExpress(new StringBuilder()) + ")");
	}

	public Integer provideTypeByName(String name)
	{
		name = provideStandardTypeName(name);

		if (name == null)
		{
			return null;
		}

		try
		{
			Field field = Types.class.getField(name);

			if (Modifier.isStatic(field.getModifiers()))
			{
				return field.getInt(null);
			}
		}
		catch (Exception e)
		{
		}

		return null;
	}

	public <T extends View> T provideView(Class<T> cls)
	{
		if (cls != null)
		{
			try
			{
				return this.provideProvider(cls.newInstance());
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		else
		{
			return null;
		}
	}
}
