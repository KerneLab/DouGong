package org.kernelab.dougong.semi;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.Member;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Alias;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Label;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.semi.dml.AbstractPrimitive;
import org.kernelab.dougong.semi.dml.AbstractTotalItems;

public abstract class AbstractProvider extends AbstractCastable implements Provider
{
	public static final char	OBJECT_SEPARATOR_CHAR	= '.';

	private SQL					sql;

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

	public SQL provideSQL()
	{
		if (this.sql == null)
		{
			this.sql = new SQL(this);
		}
		return this.sql;
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

	public AllItems provideTotalItems()
	{
		return new AbstractTotalItems();
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
