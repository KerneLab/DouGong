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

	public AllItems all()
	{
		return this.provider().provideAllItems(this);
	}

	@SuppressWarnings("unchecked")
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

	protected void initTable()
	{
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

	public String name()
	{
		return name;
	}

	protected AbstractTable name(String name)
	{
		this.name = name;
		return this;
	}

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
			table = this.getClass().newInstance() //
					.schema(this.schema()) //
					.name(this.name()) //
					.provider(this.provider());
		}
		catch (Exception e)
		{
		}
		return table;
	}

	public String schema()
	{
		return schema;
	}

	protected AbstractTable schema(String schema)
	{
		this.schema = schema;
		return this;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		return this.provider().provideOutputTableName(buffer, this);
	}

	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return this.provider().provideOutputTableNameAliased(buffer, this);
	}

	public StringBuilder toStringInsertable(StringBuilder buffer)
	{
		return this.provider().provideOutputTableNameAliased(buffer, this);
	}

	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		return this.provider().provideOutputTableNameAliased(buffer, this);
	}

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
