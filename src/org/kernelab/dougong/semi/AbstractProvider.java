package org.kernelab.dougong.semi;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Alias;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.Member;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.semi.dml.AbstractPrimitive;

public abstract class AbstractProvider implements Provider
{
	public static final char	OBJECT_SEPARATOR_CHAR	= '.';

	public String provideAliasLabel(String alias)
	{
		return Tools.notNullOrEmpty(alias) ? provideNameText(alias) : null;
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

	public Item provideNullItem()
	{
		return this.provideStringItem(SQL.NULL);
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
		this.provideOutputAlias(buffer, table);
		return buffer;
	}

	public AbstractPrimitive providePrimitive()
	{
		return this.provideProvider(new AbstractPrimitive());
	}

	@SuppressWarnings("unchecked")
	protected <T extends Providable> T provideProvider(Providable providable)
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
		if (cls != null)
		{
			T table = null;
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
			return table;
		}
		else
		{
			return null;
		}
	}

	// public String provideTableNameText(Table table)
	// {
	// StringBuilder buffer = new StringBuilder(48);
	// this.provideOutputMember(buffer, table);
	// return buffer.toString();
	// }
	//
	// public String provideTableNameTextAliased(Table table)
	// {
	// String alias = this.provideAliasLabel(table.alias());
	// return this.provideTableNameText(table) + (alias != null ? " " + alias :
	// "");
	// }
}
