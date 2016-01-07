package org.kernelab.dougong;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllColumns;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;

public class SQL
{
	public static final String	NULL	= "NULL";

	private final Provider		provider;

	public SQL(Provider provider)
	{
		this.provider = provider;
	}

	public AllColumns all()
	{
		return provider().provideAllColumns(null);
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
	public <T extends Function> T function(Class<T> cls, Expression... args)
	{
		return (T) provider().provideFunction(cls).call(args);
	}

	/**
	 * Make a StringItem <b>?</b> which represents a single item holder.
	 * 
	 * @return
	 */
	public StringItem item()
	{
		return expr("?");
	}

	/**
	 * Make a StringItem <b>?key?</b> which represents a single item holder
	 * according to the given key.
	 * 
	 * @param key
	 *            The item name.
	 * @return
	 */
	public StringItem item(String key)
	{
		return expr("?" + key + "?");
	}

	public Items list(Expression... exprs)
	{
		return provider().provideItems().list(exprs);
	}

	public Item Null()
	{
		return provider().provideNullItem();
	}

	protected Provider provider()
	{
		return provider;
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
}
