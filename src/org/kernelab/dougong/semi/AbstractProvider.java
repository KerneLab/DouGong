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

import org.kernelab.basis.Agent;
import org.kernelab.basis.Agent.AgentFactory;
import org.kernelab.basis.JSON;
import org.kernelab.basis.JSON.JSAN;
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
import org.kernelab.dougong.core.ddl.table.DropTable;
import org.kernelab.dougong.core.dml.Alias;
import org.kernelab.dougong.core.dml.Aliases;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Label;
import org.kernelab.dougong.core.dml.Pivot;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.opr.ArithmeticOperable;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.NegativeOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.dml.param.ByteParam;
import org.kernelab.dougong.core.dml.param.CharParam;
import org.kernelab.dougong.core.dml.param.DateParam;
import org.kernelab.dougong.core.dml.param.DecimalParam;
import org.kernelab.dougong.core.dml.param.DoubleParam;
import org.kernelab.dougong.core.dml.param.FloatParam;
import org.kernelab.dougong.core.dml.param.IntParam;
import org.kernelab.dougong.core.dml.param.IterableParam;
import org.kernelab.dougong.core.dml.param.JSANParam;
import org.kernelab.dougong.core.dml.param.JSONParam;
import org.kernelab.dougong.core.dml.param.LongParam;
import org.kernelab.dougong.core.dml.param.MapParam;
import org.kernelab.dougong.core.dml.param.ObjectParam;
import org.kernelab.dougong.core.dml.param.Param;
import org.kernelab.dougong.core.dml.param.ShortParam;
import org.kernelab.dougong.core.dml.param.StringParam;
import org.kernelab.dougong.core.dml.param.TimestampParam;
import org.kernelab.dougong.core.meta.AgentMeta;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.core.util.Recursor;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.ddl.AbstractAbsoluteKey;
import org.kernelab.dougong.semi.ddl.table.AbstractDropTable;
import org.kernelab.dougong.semi.dml.AbstractMerge;
import org.kernelab.dougong.semi.dml.AbstractPivot;
import org.kernelab.dougong.semi.dml.AbstractPrimitive;
import org.kernelab.dougong.semi.dml.AbstractPriorExpression;
import org.kernelab.dougong.semi.dml.AbstractTotalItems;
import org.kernelab.dougong.semi.dml.AbstractWithDefinition;
import org.kernelab.dougong.semi.dml.DaoAgent;
import org.kernelab.dougong.semi.dml.cond.AbstractExistsCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractLikeCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractMembershipCondition;
import org.kernelab.dougong.semi.dml.opr.AbstractArithmeticOperator;
import org.kernelab.dougong.semi.dml.opr.AbstractStringExpressionResult;
import org.kernelab.dougong.semi.dml.param.AbstractByteParam;
import org.kernelab.dougong.semi.dml.param.AbstractCharParam;
import org.kernelab.dougong.semi.dml.param.AbstractDateParam;
import org.kernelab.dougong.semi.dml.param.AbstractDecimalParam;
import org.kernelab.dougong.semi.dml.param.AbstractDoubleParam;
import org.kernelab.dougong.semi.dml.param.AbstractFloatParam;
import org.kernelab.dougong.semi.dml.param.AbstractIntParam;
import org.kernelab.dougong.semi.dml.param.AbstractIterableParam;
import org.kernelab.dougong.semi.dml.param.AbstractJSANParam;
import org.kernelab.dougong.semi.dml.param.AbstractJSONParam;
import org.kernelab.dougong.semi.dml.param.AbstractLongParam;
import org.kernelab.dougong.semi.dml.param.AbstractMapParam;
import org.kernelab.dougong.semi.dml.param.AbstractObjectParam;
import org.kernelab.dougong.semi.dml.param.AbstractShortParam;
import org.kernelab.dougong.semi.dml.param.AbstractStringParam;
import org.kernelab.dougong.semi.dml.param.AbstractTimestampParam;

public abstract class AbstractProvider extends AbstractCastable implements Provider
{
	/**
	 * 
	 */
	private static final long	serialVersionUID		= 3634389915965491293L;

	public static final char	OBJECT_SEPARATOR_CHAR	= '.';

	public static final char	STRING_QUOTE_BOUNDARY	= '\'';

	private SQL					sql;

	@Override
	public AbsoluteKey provideAbsoluteKey(Entity entity, Column... columns)
	{
		return provideProvider(new AbstractAbsoluteKey(entity, columns));
	}

	@Override
	public String provideAliasLabel(String alias)
	{
		return Tools.notNullOrEmpty(alias) ? provideNameText(alias) : null;
	}

	@Override
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

	@Override
	public <T> T provideDao(Class<T> cls)
	{
		if (!cls.isInterface())
		{
			try
			{
				return (T) cls.newInstance();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		AgentMeta meta = cls.getAnnotation(AgentMeta.class);
		if (meta == null)
		{
			return null;
		}

		try
		{
			return provideDao(cls, meta.value().newInstance());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T provideDao(Class<T> face, Object real)
	{
		return (T) Agent.newInstance(face, new AgentFactory()
		{
			@Override
			public <E> Agent newAgent(Class<E> face, Object real)
			{
				return provideProvider(new DaoAgent(provideProvider(real)));
			}
		}, real);
	}

	@Override
	public DivideOperator provideDivideOperator()
	{
		return this.provideProvider(new AbstractArithmeticOperator(ArithmeticOperable.DIVIDE));
	}

	@Override
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

	@Override
	public DropTable provideDropTable()
	{
		return this.provideProvider(new AbstractDropTable());
	}

	public AbstractExistsCondition provideExistsCondition()
	{
		return this.provideProvider(new AbstractExistsCondition());
	}

	@Override
	public <T extends Function> T provideFunction(Class<T> cls)
	{
		try
		{
			return this.provideProvider(provideNewInstance(cls));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
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
	public AbstractLikeCondition provideLikeCondition()
	{
		return this.provideProvider(new AbstractLikeCondition());
	}

	@Override
	public Expression provideLikeHeadPattern(Expression pattern, String escape)
	{
		Expression wildcard = provideStringItem("'%'");
		return provideJointOperator().operate(this.provideLikePatternEscaped(pattern, escape), wildcard);
	}

	@Override
	public Expression provideLikePatternDefaultEscape()
	{
		return provideStringItem("'" + provideEscapeValueLiterally(AbstractLikeCondition.ESCAPE) + "'");
	}

	@Override
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

	public AbstractMembershipCondition provideMembershipCondition()
	{
		return this.provideProvider(new AbstractMembershipCondition());
	}

	@Override
	public AbstractMerge provideMerge()
	{
		return provideProvider(new AbstractMerge());
	}

	@Override
	public MinusOperator provideMinusOperator()
	{
		return provideProvider(new AbstractArithmeticOperator(ArithmeticOperable.MINUS));
	}

	@Override
	public MultiplyOperator provideMultiplyOperator()
	{
		return provideProvider(new AbstractArithmeticOperator(ArithmeticOperable.MULTIPLY));
	}

	@Override
	public NegativeOperator provideNegativeOperator()
	{
		return provideProvider(new AbstractArithmeticOperator(ArithmeticOperable.MINUS));
	}

	@Override
	public <T> T provideNewInstance(Class<T> cls)
	{
		if (cls != null)
		{
			try
			{
				return cls.newInstance();
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Item provideNullItem()
	{
		return this.provideStringItem(SQL.NULL);
	}

	@Override
	public String provideNumberLiteral(Number number)
	{
		if (number == null)
		{
			return SQL.NULL;
		}
		else
		{
			return this.provideTextContent(number.toString());
		}
	}

	@Override
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

	@Override
	public StringBuilder provideOutputColumnExpress(StringBuilder buffer, Column column)
	{
		String alias = this.provideAliasLabel(this.provideViewAlias(column.view()));

		if (alias != null)
		{
			buffer.append(alias);
			buffer.append('.');
		}

		this.provideOutputNameText(buffer, column.name());

		return buffer;
	}

	@Override
	public StringBuilder provideOutputColumnReference(StringBuilder buffer, Column column)
	{
		String alias = this.provideAliasLabel(this.provideViewAlias(column.view()));
		if (alias != null)
		{
			buffer.append(alias);
			buffer.append('.');
		}
		return this.provideOutputNameText(buffer, column.name());
	}

	@Override
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

	@Override
	public StringBuilder provideOutputMember(StringBuilder buffer, Member member)
	{
		if (buffer != null)
		{
			if (Tools.notNullOrEmpty(member.schema()))
			{
				if (Tools.notNullOrEmpty(member.catalog()))
				{
					this.provideOutputNameText(buffer, member.catalog());
					buffer.append(OBJECT_SEPARATOR_CHAR);
				}
				this.provideOutputNameText(buffer, member.schema());
				buffer.append(OBJECT_SEPARATOR_CHAR);
			}
			this.provideOutputNameText(buffer, member.name());
		}
		return buffer;
	}

	@Override
	public StringBuilder provideOutputNameText(StringBuilder buffer, String name)
	{
		if (buffer != null)
		{
			buffer.append(this.provideNameText(name));
		}
		return buffer;
	}

	@Override
	public StringBuilder provideOutputTableName(StringBuilder buffer, Table table)
	{
		this.provideOutputMember(buffer, table);
		return buffer;
	}

	@Override
	public StringBuilder provideOutputTableNameAliased(StringBuilder buffer, Table table)
	{
		this.provideOutputTableName(buffer, table);
		this.provideOutputTablePartitionClause(buffer, table);
		this.provideOutputAlias(buffer, table);
		return buffer;
	}

	@Override
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

	@Override
	public StringBuilder provideOutputWithableAliased(StringBuilder buffer, Withable with)
	{
		if (buffer != null && with != null)
		{
			this.provideOutputWithDefinition(buffer, with.with());
			this.provideOutputAlias(buffer, with);
		}
		return buffer;
	}

	@Override
	public StringBuilder provideOutputWithDefinition(StringBuilder buffer, WithDefinition with)
	{
		if (buffer != null && with != null)
		{
			buffer.append(this.provideAliasLabel(with.name()));
		}
		return buffer;
	}

	@Override
	public Param<?> provideParameter(Class<? extends Param<?>> type, String name, Object value)
	{
		if (StringParam.class.equals(type))
		{
			return this.provideParameter(name, (String) value);
		}
		else if (IntParam.class.equals(type))
		{
			return this.provideParameter(name, (Integer) value);
		}
		else if (DoubleParam.class.equals(type))
		{
			return this.provideParameter(name, (Double) value);
		}
		else if (DateParam.class.equals(type))
		{
			return this.provideParameter(name, (Date) value);
		}
		else if (TimestampParam.class.equals(type))
		{
			return this.provideParameter(name, (Timestamp) value);
		}
		else if (DecimalParam.class.equals(type))
		{
			return this.provideParameter(name, (BigDecimal) value);
		}
		else if (ShortParam.class.equals(type))
		{
			return this.provideParameter(name, (Short) value);
		}
		else if (FloatParam.class.equals(type))
		{
			return this.provideParameter(name, (Float) value);
		}
		else if (ByteParam.class.equals(type))
		{
			return this.provideParameter(name, (Byte) value);
		}
		else if (LongParam.class.equals(type))
		{
			return this.provideParameter(name, (Long) value);
		}
		else if (CharParam.class.equals(type))
		{
			return this.provideParameter(name, (Character) value);
		}
		else if (JSANParam.class.equals(type))
		{
			return this.provideParameter(name, (JSAN) value);
		}
		else if (JSONParam.class.equals(type))
		{
			return this.provideParameter(name, (JSON) value);
		}
		else if (MapParam.class.equals(type))
		{
			return this.provideParameter(name, (Map<?, ?>) value);
		}
		else if (IterableParam.class.equals(type))
		{
			return this.provideParameter(name, (Iterable<?>) value);
		}
		else
		{
			return this.provideParameter(name, (Object) value);
		}
	}

	@Override
	public StringItem provideParameter(String name)
	{
		return provideStringItem("?" + name + "?");
	}

	@Override
	public DecimalParam provideParameter(String name, BigDecimal value)
	{
		return provideProvider(new AbstractDecimalParam(name, value));
	}

	@Override
	public ByteParam provideParameter(String name, Byte value)
	{
		return provideProvider(new AbstractByteParam(name, value));
	}

	@Override
	public CharParam provideParameter(String name, Character value)
	{
		return provideProvider(new AbstractCharParam(name, value));
	}

	@Override
	public DateParam provideParameter(String name, Date value)
	{
		return provideProvider(new AbstractDateParam(name, value));
	}

	@Override
	public DoubleParam provideParameter(String name, Double value)
	{
		return provideProvider(new AbstractDoubleParam(name, value));
	}

	@Override
	public FloatParam provideParameter(String name, Float value)
	{
		return provideProvider(new AbstractFloatParam(name, value));
	}

	@Override
	public IntParam provideParameter(String name, Integer value)
	{
		return provideProvider(new AbstractIntParam(name, value));
	}

	@Override
	public IterableParam provideParameter(String name, Iterable<?> value)
	{
		return provideProvider(new AbstractIterableParam(name, value));
	}

	@Override
	public JSANParam provideParameter(String name, JSAN value)
	{
		return provideProvider(new AbstractJSANParam(name, value));
	}

	@Override
	public JSONParam provideParameter(String name, JSON value)
	{
		return provideProvider(new AbstractJSONParam(name, value));
	}

	@Override
	public LongParam provideParameter(String name, Long value)
	{
		return provideProvider(new AbstractLongParam(name, value));
	}

	@Override
	public <K, V> MapParam<K, V> provideParameter(String name, Map<K, V> value)
	{
		return provideProvider(new AbstractMapParam<K, V>(name, value));
	}

	@Override
	public ShortParam provideParameter(String name, Short value)
	{
		return provideProvider(new AbstractShortParam(name, value));
	}

	@Override
	public StringParam provideParameter(String name, String value)
	{
		return provideProvider(new AbstractStringParam(name, value));
	}

	@Override
	public <T> ObjectParam<T> provideParameter(String name, T value)
	{
		return provideProvider(new AbstractObjectParam<T>(name, value));
	}

	@Override
	public TimestampParam provideParameter(String name, Timestamp value)
	{
		return provideProvider(new AbstractTimestampParam(name, value));
	}

	@Override
	public Param<?> provideParameterByValue(String name, Object value)
	{
		if (value == null)
		{
			return provideParameter(name, (Object) null);
		}
		return provideParameter(provideParameterType(value.getClass()), name, value);
	}

	@Override
	public String provideParameterExpression(String name)
	{
		return "?" + name + "?";
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends Param<?>> provideParameterType(Class<?> type)
	{
		if (String.class.equals(type))
		{
			return StringParam.class;
		}
		else if (Integer.TYPE.equals(type) || Integer.class.equals(type))
		{
			return IntParam.class;
		}
		else if (Double.TYPE.equals(type) || Double.class.equals(type))
		{
			return DoubleParam.class;
		}
		else if (Date.class.equals(type))
		{
			return DateParam.class;
		}
		else if (Timestamp.class.equals(type))
		{
			return TimestampParam.class;
		}
		else if (BigDecimal.class.equals(type))
		{
			return DecimalParam.class;
		}
		else if (Short.TYPE.equals(type) || Short.class.equals(type))
		{
			return ShortParam.class;
		}
		else if (Float.TYPE.equals(type) || Float.class.equals(type))
		{
			return FloatParam.class;
		}
		else if (Byte.TYPE.equals(type) || Byte.class.equals(type))
		{
			return ByteParam.class;
		}
		else if (Long.TYPE.equals(type) || Long.class.equals(type))
		{
			return LongParam.class;
		}
		else if (Character.TYPE.equals(type) || Character.class.equals(type))
		{
			return CharParam.class;
		}
		else if (JSAN.class.equals(type))
		{
			return JSANParam.class;
		}
		else if (JSON.class.equals(type))
		{
			return JSONParam.class;
		}
		else if (Map.class.equals(type))
		{
			return (Class<? extends Param<?>>) MapParam.class;
		}
		else if (Iterable.class.equals(type))
		{
			return IterableParam.class;
		}
		else
		{
			return (Class<? extends Param<?>>) ObjectParam.class;
		}
	}

	@Override
	public Pivot providePivot()
	{
		return provideProvider(new AbstractPivot());
	}

	@Override
	public PlusOperator providePlusOperator()
	{
		return provideProvider(new AbstractArithmeticOperator(ArithmeticOperable.PLUS));
	}

	@Override
	public AbstractPrimitive providePrimitive()
	{
		return this.provideProvider(new AbstractPrimitive());
	}

	@Override
	public AbstractPriorExpression providePriorExpression(Expression expr)
	{
		return provideProvider(new AbstractPriorExpression(expr));
	}

	@SuppressWarnings("unchecked")
	@Override
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

	@Override
	public Recursor provideRecursor(Primitive view)
	{
		return new Recursor().provider(this).view(view);
	}

	@Override
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

	@Override
	public Result provideResult(String expression)
	{
		return provideProvider(new AbstractStringExpressionResult(expression));
	}

	@Override
	public SQL provideSQL()
	{
		if (this.sql == null)
		{
			this.sql = new SQL(this);
		}
		return this.sql;
	}

	@Override
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

	@Override
	public <T extends Subquery> T provideSubquery(Class<T> cls)
	{
		return provideView(cls);
	}

	@Override
	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select)
	{
		try
		{
			T s = this.provideNewInstance(cls);
			s.select(select);
			return this.provideProvider(s);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T extends Table> T provideTable(Class<T> cls)
	{
		return provideView(cls);
	}

	@Override
	public String provideTextLiteral(CharSequence text)
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

	@Override
	public Result provideToLowerCase(Expression expr)
	{
		return provideResult("LOWER(" + expr.toStringExpress(new StringBuilder()) + ")");
	}

	@Override
	public AllItems provideTotalItems()
	{
		return new AbstractTotalItems();
	}

	@Override
	public Result provideToUpperCase(Expression expr)
	{
		return provideResult("UPPER(" + expr.toStringExpress(new StringBuilder()) + ")");
	}

	@Override
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

	@Override
	public <T extends View> T provideView(Class<T> cls)
	{
		return this.provideProvider(provideNewInstance(cls));
	}

	@Override
	public String provideViewAlias(View view)
	{
		String alias = view.alias();

		if (alias == null)
		{
			if (view instanceof Withable && ((Withable) view).with() != null)
			{
				alias = ((Withable) view).with().name();
			}
		}

		return alias;
	}

	@Override
	public WithDefinition provideWithDefinition(String name, String... columns)
	{
		return this.provideProvider(new AbstractWithDefinition(name, columns));
	}
}
