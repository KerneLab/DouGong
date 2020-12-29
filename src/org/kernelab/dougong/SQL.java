package org.kernelab.dougong;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Function;
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
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.opr.CaseDecideExpression;
import org.kernelab.dougong.core.dml.opr.CaseSwitchExpression;
import org.kernelab.dougong.semi.dml.cond.AbstractLikeCondition;

public class SQL
{
	public static final String	NULL	= "NULL";

	private final Provider		provider;

	public SQL(Provider provider)
	{
		this.provider = provider;
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

	public ComposableCondition False()
	{
		return provider().provideLogicalCondition().and(expr("0").eq(expr("1")));
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

	public Provider provider()
	{
		return provider;
	}

	/**
	 * Make a StringItem <b>"key"</b> which refer to a single item according to
	 * the given key.
	 * 
	 * @param key
	 *            The reference name.
	 * @return
	 */
	public StringItem ref(String key)
	{
		return expr(provider().provideAliasLabel(key));
	}

	public <T extends Subquery> T subquery(Class<T> cls)
	{
		return provider().provideSubquery(cls);
	}

	public <T extends Subquery> T subquery(Class<T> cls, Select select)
	{
		return provider().provideSubquery(cls, select);
	}

	public <T extends Table> T table(Class<T> cls)
	{
		return table(cls, null);
	}

	public <T extends Table> T table(Class<T> cls, String alias)
	{
		return provider().provideTable(cls).as(alias);
	}

	public ComposableCondition True()
	{
		return provider().provideLogicalCondition().and(expr("0").eq(expr("0")));
	}

	public <T extends View> T view(Class<T> cls)
	{
		return provider().provideView(cls);
	}

	public Primitive with(Withable... views)
	{
		return provider().providePrimitive().with(views);
	}
}
