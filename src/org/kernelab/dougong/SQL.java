package org.kernelab.dougong;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Merge;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.cond.ExistsCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.core.dml.opr.CaseDecideExpression;
import org.kernelab.dougong.core.dml.opr.CaseSwitchExpression;
import org.kernelab.dougong.core.dml.param.ByteParam;
import org.kernelab.dougong.core.dml.param.DateParam;
import org.kernelab.dougong.core.dml.param.DecimalParam;
import org.kernelab.dougong.core.dml.param.DoubleParam;
import org.kernelab.dougong.core.dml.param.FloatParam;
import org.kernelab.dougong.core.dml.param.IntParam;
import org.kernelab.dougong.core.dml.param.IterableParam;
import org.kernelab.dougong.core.dml.param.LongParam;
import org.kernelab.dougong.core.dml.param.Param;
import org.kernelab.dougong.core.dml.param.ShortParam;
import org.kernelab.dougong.core.dml.param.StringParam;
import org.kernelab.dougong.core.dml.param.TimestampParam;
import org.kernelab.dougong.core.util.Recursor;
import org.kernelab.dougong.semi.dml.ViewSelf;
import org.kernelab.dougong.semi.dml.cond.AbstractLikeCondition;

public class SQL implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4657293054310219395L;

	public static final String	NULL				= "NULL";

	protected final Provider	provider;

	public SQL(Provider provider)
	{
		this.provider = provider;
	}

	public StringItem $(String name)
	{
		return ref(name);
	}

	public AllItems all()
	{
		return provider().provideTotalItems();
	}

	public ComposableCondition and(Object... conds)
	{
		if (conds == null)
		{
			return null;
		}

		ComposableCondition cond = provider().provideLogicalCondition();

		for (int i = 0; i < conds.length; i++)
		{
			if (conds[i] instanceof Boolean)
			{
				if ((Boolean) conds[i] && i + 1 < conds.length && conds[i + 1] instanceof Condition)
				{
					cond = cond.and((Condition) conds[i + 1]);
				}
				i++;
			}
			else if (conds[i] instanceof Condition)
			{
				cond = cond.and((Condition) conds[i]);
			}
		}

		return cond;
	}

	public CaseDecideExpression Case()
	{
		return provider().provideCaseExpression();
	}

	public CaseSwitchExpression Case(Expression value)
	{
		return provider().provideCaseExpression(value);
	}

	public ComposableCondition cond(ComposableCondition c)
	{
		return provider().provideLogicalCondition().and(c);
	}

	public Expression escape()
	{
		return provider().provideLikePatternDefaultEscape();
	}

	public ExistsCondition exists(Select select)
	{
		return provider().provideExistsCondition().exists(select);
	}

	public ExistsCondition exists(Subquery subquery)
	{
		return provider().provideExistsCondition().exists(subquery.select());
	}

	/**
	 * Make a StringItem exactly according to the given expression string.
	 * 
	 * @param expr
	 *            The expression string.
	 * @return
	 */
	public StringItem expr(String expr)
	{
		return provider().provideStringItem(expr);
	}

	public <T extends Function> T f(Class<T> cls, Expression... args)
	{
		return func(cls, args);
	}

	public <T extends Function> T f(T func, Expression... args)
	{
		return func(func, args);
	}

	public ComposableCondition False()
	{
		return provider().provideLogicalCondition().and(expr("0").eq(expr("1")));
	}

	public StringItem formatDT(String format)
	{
		return val(provider().provideDatetimeFormat(format));
	}

	public Primitive from(View view)
	{
		return provider().providePrimitive().from(view);
	}

	@SuppressWarnings("unchecked")
	public <T extends Function> T func(Class<T> cls, Expression... args)
	{
		return (T) provider().provideFunction(cls).call(args);
	}

	@SuppressWarnings("unchecked")
	public <T extends Function> T func(T func, Expression... args)
	{
		provider().provideProvider(func);
		return (T) func.call(args);
	}

	public <T extends Insertable> Insert insert(T target, Column... columns)
	{
		return provider().provideInsert().into(target).columns(columns);
	}

	/**
	 * Make case insensitive among pattern ({@code '%'+value+'%'}) according to
	 * a given value with default escape.
	 * 
	 * @param value
	 * @return
	 */
	public Expression iPatnAmong(Expression value)
	{
		return iPatnAmong(value, null);
	}

	/**
	 * Make case insensitive among pattern ({@code '%'+value+'%'}) according to
	 * a given value and escape.
	 * 
	 * @param value
	 * @param escape
	 * @return
	 */
	public Expression iPatnAmong(Expression value, String escape)
	{
		return Case().when(value.isNull(), Null()) //
				.els(provider().provideLikeAmongPattern(provider().provideToUpperCase(value),
						escape == null ? AbstractLikeCondition.ESCAPE : escape));
	}

	/**
	 * Make case insensitive among pattern ({@code '%'+?param?+'%'}) according
	 * to a given param with default escape.
	 * 
	 * @param param
	 * @return
	 */
	public Expression iPatnAmong(String param)
	{
		return iPatnAmong(param(param));
	}

	/**
	 * Make case insensitive head pattern ({@code value+'%'}) according to a
	 * given value with default escape.
	 * 
	 * @param value
	 * @return
	 */
	public Expression iPatnHead(Expression value)
	{
		return iPatnHead(value, null);
	}

	/**
	 * Make case insensitive head pattern ({@code value+'%'}) according to a
	 * given value and escape.
	 * 
	 * @param value
	 * @param escape
	 * @return
	 */
	public Expression iPatnHead(Expression value, String escape)
	{
		return Case().when(value.isNull(), Null()) //
				.els(provider().provideLikeHeadPattern(provider().provideToUpperCase(value),
						escape == null ? AbstractLikeCondition.ESCAPE : escape));
	}

	/**
	 * Make case insensitive head pattern ({@code ?param?+'%'}) according to a
	 * given param with default escape.
	 * 
	 * @param param
	 * @return
	 */
	public Expression iPatnHead(String param)
	{
		return iPatnHead(param(param));
	}

	/**
	 * Make case insensitive tail pattern ({@code '%'+value}) according to a
	 * given value with default escape.
	 * 
	 * @param value
	 * @return
	 */
	public Expression iPatnTail(Expression value)
	{
		return iPatnTail(value, null);
	}

	/**
	 * Make case insensitive tail pattern ({@code '%'+value}) according to a
	 * given value and escape.
	 * 
	 * @param value
	 * @param escape
	 * @return
	 */
	public Expression iPatnTail(Expression value, String escape)
	{
		return Case().when(value.isNull(), Null()) //
				.els(provider().provideLikeTailPattern(provider().provideToUpperCase(value),
						escape == null ? AbstractLikeCondition.ESCAPE : escape));
	}

	/**
	 * Make case insensitive tail pattern ({@code '%'+?param?}) according to a
	 * given param with default escape.
	 * 
	 * @param param
	 * @return
	 */
	public Expression iPatnTail(String param)
	{
		return iPatnTail(param(param));
	}

	public Items list(Expression... exprs)
	{
		return provider().provideItems().list(exprs);
	}

	public Merge merge(View target)
	{
		return provider().provideMerge().into(target);
	}

	public LogicalCondition not(Condition cond)
	{
		return provider().provideLogicalCondition().not(cond);
	}

	public ExistsCondition notExists(Select select)
	{
		return (ExistsCondition) provider().provideExistsCondition().exists(select).not();
	}

	public ExistsCondition notExists(Subquery subquery)
	{
		return (ExistsCondition) provider().provideExistsCondition().exists(subquery.select()).not();
	}

	public Item Null()
	{
		return provider().provideNullItem();
	}

	/**
	 * To get an initial condition through various cases.<br />
	 * The odd parameter should be boolean to indicate its following Condition
	 * parameter should be the result Condition or not.<br />
	 * If no condition was taken then the last parameter would be taken if it is
	 * a Condition object and the total number of parameters is odd. Otherwise
	 * an empty Condition would be returned.
	 * 
	 * @param conds
	 * @return
	 */
	public ComposableCondition on(Object... conds)
	{
		if (conds != null)
		{
			int i = 0, l = conds.length - 1;

			for (; i < l; i += 2)
			{
				if (conds[i] instanceof Boolean && conds[i + 1] instanceof Condition)
				{
					if ((Boolean) conds[i])
					{
						return provider().provideLogicalCondition().and((Condition) conds[i + 1]);
					}
				}
			}

			if (conds.length > 0 && i < conds.length && conds[l] instanceof Condition)
			{
				return provider().provideLogicalCondition().and((Condition) conds[l]);
			}
			else
			{
				return provider().provideLogicalCondition();
			}
		}
		else
		{
			return null;
		}
	}

	public ComposableCondition or(Object... conds)
	{
		if (conds == null)
		{
			return null;
		}

		ComposableCondition cond = provider().provideLogicalCondition();

		for (int i = 0; i < conds.length; i++)
		{
			if (conds[i] instanceof Boolean)
			{
				if ((Boolean) conds[i] && i + 1 < conds.length && conds[i + 1] instanceof Condition)
				{
					cond = cond.or((Condition) conds[i + 1]);
				}
				i++;
			}
			else if (conds[i] instanceof Condition)
			{
				cond = cond.or((Condition) conds[i]);
			}
		}

		return cond;
	}

	public <T extends Providable> T p(Providable obj)
	{
		return provide(obj);
	}

	/**
	 * Make a StringItem <b>?</b> which represents a single item holder.
	 * 
	 * @return
	 */
	public StringItem param()
	{
		return expr("?");
	}

	/**
	 * Make a StringItem {@code ?name?} which represents a single item holder
	 * according to the given key.
	 * 
	 * @param key
	 *            The parameter name.
	 * @return
	 */
	public StringItem param(String key)
	{
		return provider().provideParameter(key);
	}

	public DecimalParam param(String name, BigDecimal value)
	{
		return provider().provideParameter(name, value);
	}

	public ByteParam param(String name, Byte value)
	{
		return provider().provideParameter(name, value);
	}

	public DateParam param(String name, Date value)
	{
		return provider().provideParameter(name, value);
	}

	public DoubleParam param(String name, Double value)
	{
		return provider().provideParameter(name, value);
	}

	public FloatParam param(String name, Float value)
	{
		return provider().provideParameter(name, value);
	}

	public IntParam param(String name, Integer value)
	{
		return provider().provideParameter(name, value);
	}

	public IterableParam param(String name, Iterable<?> value)
	{
		return provider().provideParameter(name, value);
	}

	public LongParam param(String name, Long value)
	{
		return provider().provideParameter(name, value);
	}

	public ShortParam param(String name, Short value)
	{
		return provider().provideParameter(name, value);
	}

	public StringParam param(String name, String value)
	{
		return provider().provideParameter(name, value);
	}

	public TimestampParam param(String name, Timestamp value)
	{
		return provider().provideParameter(name, value);
	}

	public Map<String, Object> params(Iterable<Param<?>> params)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (Param<?> param : params)
		{
			map.put(param.name(), param.value());
		}

		return map;
	}

	public Map<String, Object> params(Param<?>... params)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (Param<?> param : params)
		{
			map.put(param.name(), param.value());
		}

		return map;
	}

	/**
	 * Make among pattern ({@code '%'+value+'%'}) according to a given value
	 * with default escape.
	 * 
	 * @param value
	 * @return
	 */
	public Expression patnAmong(Expression value)
	{
		return patnAmong(value, null);
	}

	/**
	 * Make among pattern ({@code '%'+value+'%'}) according to a given value and
	 * escape.
	 * 
	 * @param value
	 * @param escape
	 * @return
	 */
	public Expression patnAmong(Expression value, String escape)
	{
		return Case().when(value.isNull(), Null()) //
				.els(provider().provideLikeAmongPattern(value, escape == null ? AbstractLikeCondition.ESCAPE : escape));
	}

	/**
	 * Make among pattern ({@code '%'+?param?+'%'}) according to a given param
	 * with default escape.
	 * 
	 * @param param
	 * @return
	 */
	public Expression patnAmong(String param)
	{
		return patnAmong(param(param));
	}

	/**
	 * Make head pattern ({@code value+'%'}) according to a given value with
	 * default escape.
	 * 
	 * @param value
	 * @return
	 */
	public Expression patnHead(Expression value)
	{
		return patnHead(value, null);
	}

	/**
	 * Make head pattern ({@code value+'%'}) according to a given value and
	 * escape.
	 * 
	 * @param value
	 * @param escape
	 * @return
	 */
	public Expression patnHead(Expression value, String escape)
	{
		return Case().when(value.isNull(), Null()) //
				.els(provider().provideLikeHeadPattern(value, escape == null ? AbstractLikeCondition.ESCAPE : escape));
	}

	/**
	 * Make head pattern ({@code ?param?+'%'}) according to a given param with
	 * default escape.
	 * 
	 * @param param
	 * @return
	 */
	public Expression patnHead(String param)
	{
		return patnHead(param(param));
	}

	/**
	 * Make tail pattern ({@code '%'+value}) according to a given value with
	 * default escape.
	 * 
	 * @param value
	 * @return
	 */
	public Expression patnTail(Expression value)
	{
		return patnTail(value, null);
	}

	/**
	 * Make tail pattern ({@code '%'+value}) according to a given value and
	 * escape.
	 * 
	 * @param value
	 * @param escape
	 * @return
	 */
	public Expression patnTail(Expression value, String escape)
	{
		return Case().when(value.isNull(), Null()) //
				.els(provider().provideLikeTailPattern(value, escape == null ? AbstractLikeCondition.ESCAPE : escape));
	}

	/**
	 * Make tail pattern ({@code '%'+?param?}) according to a given param with
	 * default escape.
	 * 
	 * @param param
	 * @return
	 */
	public Expression patnTail(String param)
	{
		return patnTail(param(param));
	}

	public Expression prior(Expression expr)
	{
		return provider().providePriorExpression(expr);
	}

	public <T extends Providable> T provide(Providable obj)
	{
		return provider().provideProvider(obj);
	}

	public Provider provider()
	{
		return provider;
	}

	public Recursor recursor(Primitive view)
	{
		return provider().provideRecursor(view);
	}

	/**
	 * Make a StringItem <b>"name"</b> which refer to a single item according to
	 * the given name.
	 * 
	 * @param name
	 *            The reference name.
	 * @return
	 */
	public StringItem ref(String name)
	{
		return expr(provider().provideAliasLabel(name));
	}

	public ViewSelf self()
	{
		return provider().provideView(ViewSelf.class);
	}

	public <T extends Subquery> T subquery(Class<T> cls)
	{
		return subquery(cls, (String) null);
	}

	public <T extends Subquery> T subquery(Class<T> cls, Select select)
	{
		return provider().provideSubquery(cls, select);
	}

	@SuppressWarnings("unchecked")
	public <T extends Subquery> T subquery(Class<T> cls, String alias)
	{
		return (T) provider().provideSubquery(cls).alias(alias);
	}

	public <T extends Table> T table(Class<T> cls)
	{
		return table(cls, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Table> T table(Class<T> cls, String alias)
	{
		return (T) provider().provideTable(cls).alias(alias);
	}

	public <T extends Table> T table(T table)
	{
		return table(table, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Table> T table(T table, String alias)
	{
		return (T) ((T) provider().provideProvider(table)).alias(alias);
	}

	public ComposableCondition True()
	{
		return provider().provideLogicalCondition().and(expr("0").eq(expr("0")));
	}

	public StringItem v(CharSequence text)
	{
		return this.val(text);
	}

	public StringItem v(Number number)
	{
		return this.val(number);
	}

	public StringItem val(CharSequence text)
	{
		return expr(provider().provideTextLiteral(text));
	}

	public StringItem val(Number number)
	{
		return expr(provider().provideNumberLiteral(number));
	}

	public <T extends View> T view(Class<T> cls)
	{
		return view(cls, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T view(Class<T> cls, String alias)
	{
		return (T) provider().provideView(cls).alias(alias);
	}

	public <T extends View> T view(T view)
	{
		return view(view, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T view(T view, String alias)
	{
		return (T) ((T) provider().provideProvider(view)).alias(alias);
	}

	public WithDefinition w(String name, String... columns)
	{
		return this.with(name, columns);
	}

	public WithDefinition with(String name, String... columns)
	{
		return provider().provideWithDefinition(name, columns);
	}

	public Primitive with(WithDefinition... withs)
	{
		return provider().providePrimitive().withs(false, withs);
	}

	public Primitive With(WithDefinition... withs)
	{
		return this.with(withs);
	}

	public Primitive withRecursive(WithDefinition... withs)
	{
		return provider().providePrimitive().withs(true, withs);
	}
}
