package org.kernelab.dougong;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.ListItem;
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

	public Primitive from(View view)
	{
		return provider().providePrimitive().from(view);
	}

	public StringItem item(String string)
	{
		return provider().provideStringItem(string);
	}

	public ListItem list(Object... items)
	{
		return provider().provideListItem().list(items);
	}

	protected Provider provider()
	{
		return provider;
	}

	@SuppressWarnings("unchecked")
	public <T extends Function> T function(Class<T> cls, Expression... args)
	{
		return (T) provider().provideFunction(cls).call(args);
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
