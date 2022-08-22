package org.kernelab.dougong.semi.ddl.table;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.ddl.table.CreateTable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.semi.AbstractText;

public abstract class AbstractCreateTable extends AbstractText implements CreateTable
{
	private boolean	temporary	= false;

	private Table	table;

	private boolean	ifNotExists	= false;

	private String	options;

	private Select	select;

	protected String getColumnDefine(Column col)
	{
		TypeMeta type = col.field().getAnnotation(TypeMeta.class);

		String define = this.provider().provideNameText(col.name());

		define += " " + type.type();
		if (type.precision() != 0)
		{
			define += "(" + type.precision();
			if (type.scale() != 0)
			{
				define += "," + type.scale();
			}
			define += ")";
		}

		if (type.nullable() == TypeMeta.NO_NULLS)
		{
			define += " NOT NULL";
		}

		return define;
	}

	@Override
	public boolean ifNotExists()
	{
		return ifNotExists;
	}

	public AbstractCreateTable ifNotExists(boolean ifNotExists)
	{
		this.ifNotExists = ifNotExists;
		return this;
	}

	protected abstract boolean isNeed(Column col);

	@Override
	public String options()
	{
		return options;
	}

	public AbstractCreateTable options(String options)
	{
		this.options = options;
		return this;
	}

	@Override
	public Select select()
	{
		return select;
	}

	public AbstractCreateTable select(Select select)
	{
		this.select = select;
		return this;
	}

	@Override
	public Table table()
	{
		return table;
	}

	public AbstractCreateTable table(Table table)
	{
		this.table = table;
		return this;
	}

	@Override
	public boolean temporary()
	{
		return temporary;
	}

	public AbstractCreateTable temporary(boolean temporary)
	{
		this.temporary = temporary;
		return this;
	}

	protected void textOfColumns(StringBuilder buffer)
	{
		buffer.append(" (\n");

		boolean first = true;
		Column c = null;
		for (Item i : table().items())
		{
			c = (Column) i;
			if (!isNeed(c))
			{
				continue;
			}
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(",\n");
			}
			buffer.append(this.getColumnDefine(c));
		}

		buffer.append("\n)");
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("CREATE");

		if (this.temporary())
		{
			buffer.append(" TEMPORARY");
		}

		buffer.append(" TABLE");

		if (this.ifNotExists())
		{
			buffer.append(" IF NOT EXISTS");
		}

		buffer.append(' ');

		this.provider().provideOutputNameText(buffer, table().name());
	}

	protected void textOfOptions(StringBuilder buffer)
	{
		if (Tools.notNullOrWhite(this.options()))
		{
			buffer.append(' ');
			buffer.append(this.options());
		}
	}

	protected void textOfSelect(StringBuilder buffer)
	{
		buffer.append(" AS \n");
		this.select().toString(buffer);
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);

		if (this.select() == null)
		{
			this.textOfColumns(buffer);
		}

		this.textOfOptions(buffer);

		if (this.select() != null)
		{
			this.textOfSelect(buffer);
		}

		return buffer;
	}
}
