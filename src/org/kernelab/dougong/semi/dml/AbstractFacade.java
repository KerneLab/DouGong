package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.util.Utils;

public class AbstractFacade extends AbstractSubquery implements Table
{
	private String	catalog	= null;

	private String	schema	= null;

	private String	name;

	private String	partition;

	@Override
	public AbstractFacade alias(String alias)
	{
		super.alias(alias);
		return this;
	}

	@Override
	public String catalog()
	{
		return catalog;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Table> T catalog(String catalog)
	{
		this.catalog = catalog;
		if (catalog == null)
		{
			this.catalog = Utils.getCatalogFromMember(this);
		}
		return (T) this;
	}

	@Override
	protected AbstractFacade clone()
	{
		AbstractFacade table = null;
		try
		{
			table = ((AbstractFacade) super.clone()) //
					.catalog(this.catalog()) //
					.schema(this.schema()) //
					.name(this.name());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return table;
	}

	protected void initTable()
	{
		this.catalog(Utils.getCatalogFromMember(this));
		this.schema(Utils.getSchemaFromMember(this));
		this.name(Utils.getNameFromNamed(this));
	}

	protected boolean isTable()
	{
		return select() == null;
	}

	@Override
	public String label()
	{
		if (isTable())
		{
			return alias() != null ? alias() : name();
		}
		else
		{
			return alias();
		}
	}

	@Override
	public String name()
	{
		return name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Table> T name(String name)
	{
		this.name = name;
		if (name == null)
		{
			this.name = Utils.getNameFromNamed(this);
		}
		return (T) this;
	}

	@Override
	public String partition()
	{
		return partition;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractFacade> T partition(String partName)
	{
		AbstractFacade table = this.clone();
		if (table != null)
		{
			table.partition = partName;
		}
		return (T) table;
	}

	@Override
	public AbstractFacade provider(Provider provider)
	{
		super.provider(provider);
		this.initTable();
		return this;
	}

	@Override
	public String schema()
	{
		return schema;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Table> T schema(String schema)
	{
		this.schema = schema;
		if (schema == null)
		{
			this.schema = Utils.getSchemaFromMember(this);
		}
		return (T) this;
	}

	@Override
	public String toString()
	{
		return this.toString(new StringBuilder()).toString();
	}

	@Override
	public String toString(int level)
	{
		return this.toString(new StringBuilder(), level).toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		if (isTable())
		{
			return this.toString(buffer, -1);
		}
		else
		{
			return super.toString(buffer);
		}
	}

	@Override
	public StringBuilder toString(StringBuilder buffer, int level)
	{
		return this.provider().provideOutputMember(buffer, this, level);
	}

	@Override
	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		if (isTable())
		{
			return this.provider().provideOutputTableNameAliased(buffer, this);
		}
		else
		{
			return super.toStringDeletable(buffer);
		}
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		if (isTable())
		{
			throw new UnsupportedOperationException(
					this.getClass().getName() + " instance of Table cannot be used as Expression");
		}
		else
		{
			return super.toStringExpress(buffer);
		}
	}

	@Override
	public StringBuilder toStringInsertable(StringBuilder buffer)
	{
		if (isTable())
		{
			return this.provider().provideOutputTableNameInsert(buffer, this);
		}
		else
		{
			return super.toStringInsertable(buffer);
		}
	}

	@Override
	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		if (isTable())
		{
			throw new UnsupportedOperationException(
					this.getClass().getName() + " instance of Table cannot be used as Scope");
		}
		else
		{
			return super.toStringScoped(buffer);
		}
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		if (isTable())
		{
			throw new UnsupportedOperationException(
					this.getClass().getName() + " instance of Table cannot be used as selected Item");
		}
		else
		{
			return super.toStringSelected(buffer);
		}
	}

	@Override
	public StringBuilder toStringSourceOfBody(StringBuilder buffer)
	{
		if (isTable())
		{
			throw new UnsupportedOperationException(
					this.getClass().getName() + " instance of Table cannot be used as Source");
		}
		else
		{
			return super.toStringSourceOfBody(buffer);
		}
	}

	@Override
	public StringBuilder toStringSourceOfWith(StringBuilder buffer)
	{
		if (isTable())
		{
			throw new UnsupportedOperationException(
					this.getClass().getName() + " instance of Table cannot be used as Source");
		}
		else
		{
			return super.toStringSourceOfWith(buffer);
		}
	}

	@Override
	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		if (isTable())
		{
			return this.provider().provideOutputTableNameAliased(buffer, this);
		}
		else
		{
			return super.toStringUpdatable(buffer);
		}
	}

	@Override
	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		if (isTable())
		{
			return this.provider().provideOutputTableNameAliased(buffer, this);
		}
		else
		{
			if (this.with() != null)
			{
				return this.provider().provideOutputWithableAliased(buffer, this);
			}
			else
			{
				buffer.append('(');
				this.toString(buffer);
				buffer.append(')');
				return this.provider().provideOutputAlias(buffer, this);
			}
		}
	}

	@Override
	public StringBuilder toStringWith(StringBuilder buffer)
	{
		if (isTable())
		{
			throw new UnsupportedOperationException(
					this.getClass().getName() + " instance of Table cannot be used as Withable");
		}
		else
		{
			return super.toStringWith(buffer);
		}
	}
}
