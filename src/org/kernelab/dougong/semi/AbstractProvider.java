package org.kernelab.dougong.semi;

import org.kernelab.dougong.core.Alias;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.semi.dml.AbstractPrimitive;

public abstract class AbstractProvider implements Provider
{
	@SuppressWarnings("unchecked")
	public <T extends Function> T provideFunction(Class<T> cls)
	{
		try
		{
			Function function = cls.newInstance();
			function.provider(this);
			return (T) function;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public StringBuilder provideOutputAlias(StringBuilder buffer, Alias alias)
	{
		if (buffer != null && alias != null)
		{
			String label = this.provideAliasLabel(alias.alias());
			if (label != null)
			{
				buffer.append(' ');
				buffer.append(label);
			}
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
		}
		return buffer;
	}

	public AbstractPrimitive providePrimitive()
	{
		return (AbstractPrimitive) new AbstractPrimitive().provider(this);
	}

	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select)
	{
		try
		{
			T s = cls.newInstance();
			s.select(select);
			s.provider(this);
			return s;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public <T extends Table> T provideTable(Class<T> cls)
	{
		if (cls != null)
		{
			T table = null;
			try
			{
				table = cls.newInstance();
				table.provider(this);
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			return table;
		}
		else
		{
			return null;
		}
	}

	public String provideTableNameAliased(Table table)
	{
		String alias = this.provideAliasLabel(table.alias());
		return this.provideTableName(table) + (alias != null ? " " + alias : "");
	}
}
