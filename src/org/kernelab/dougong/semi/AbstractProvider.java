package org.kernelab.dougong.semi;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;

public abstract class AbstractProvider implements Provider
{
	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select)
	{
		try
		{
			T s = cls.newInstance();
			s.setSelect(select);
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
