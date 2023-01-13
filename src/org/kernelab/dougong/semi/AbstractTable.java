package org.kernelab.dougong.semi;

import java.util.Map;
import java.util.Map.Entry;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractTable extends AbstractEntity implements Table
{
	private String	catalog	= null;

	private String	schema	= null;

	private String	name;

	private String	partition;

	public AbstractTable()
	{
		super();
	}

	@Override
	public AbstractTable alias(String alias)
	{
		super.alias(alias);
		return this;
	}

	@Override
	public AllItems all()
	{
		return this.provider().provideAllItems(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Table> T as(String alias)
	{
		AbstractTable table = this.replicate();
		if (table != null)
		{
			table.alias(alias);
			table.partition = this.partition();
		}
		return (T) table;
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

	protected void initTable()
	{
		this.catalog(Utils.getCatalogFromMember(this));
		this.schema(Utils.getSchemaFromMember(this));
		this.name(Utils.getNameFromNamed(this));
	}

	public Insert insertByMetaMap(Map<Column, Expression> meta)
	{
		if (meta == null)
		{
			meta = this.getColumnDefaultExpressions(null);
		}

		Column[] columns = new Column[meta.size()];
		Expression[] values = new Expression[meta.size()];

		int i = 0;
		for (Entry<Column, Expression> entry : meta.entrySet())
		{
			columns[i] = entry.getKey();
			values[i] = entry.getValue();
			i++;
		}

		return this.provider().provideInsert().into(this).columns(columns).values(values);
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
	public <T extends Table> T partition(String partName)
	{
		AbstractTable table = this.replicate();
		if (table != null)
		{
			table.partition = partName;
		}
		return (T) table;
	}

	@Override
	public AbstractTable provider(Provider provider)
	{
		super.provider(provider);
		this.initTable();
		return this;
	}

	protected AbstractTable replicate()
	{
		AbstractTable table = null;
		try
		{
			table = (AbstractTable) this.getClass().newInstance() //
					.provider(this.provider()) //
					.catalog(this.catalog()) //
					.schema(this.schema()) //
					.name(this.name()) //
			;
		}
		catch (Exception e)
		{
		}
		return table;
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
	public StringBuilder toString(StringBuilder buffer)
	{
		return this.provider().provideOutputTableName(buffer, this);
	}

	@Override
	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return this.provider().provideOutputTableNameAliased(buffer, this);
	}

	@Override
	public StringBuilder toStringInsertable(StringBuilder buffer)
	{
		return this.provider().provideOutputTableNameAliased(buffer, this);
	}

	@Override
	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		return this.provider().provideOutputTableNameAliased(buffer, this);
	}

	@Override
	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		return this.provider().provideOutputTableNameAliased(buffer, this);
	}

	public Update updateByMetaMap(Map<Column, Expression> meta)
	{
		Update update = this.provider().provideUpdate().update(this);

		for (Entry<Column, Expression> entry : meta.entrySet())
		{
			update = update.set(entry.getKey(), entry.getValue());
		}

		return update;
	}
}
