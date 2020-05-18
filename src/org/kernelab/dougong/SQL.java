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
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.opr.CaseDecideExpression;
import org.kernelab.dougong.core.dml.opr.CaseSwitchExpression;

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

	public Items list(Expression... exprs)
	{
		return provider().provideItems().list(exprs);
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
