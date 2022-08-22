package org.kernelab.dougong.semi.ddl.table;

import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.ddl.table.DropTable;
import org.kernelab.dougong.semi.AbstractText;

public class AbstractDropTable extends AbstractText implements DropTable
{
	private Table	table;

	private boolean	ifExists	= false;

	@Override
	public boolean ifExists()
	{
		return ifExists;
	}

	public AbstractDropTable ifExists(boolean ifExists)
	{
		this.ifExists = ifExists;
		return this;
	}

	@Override
	public Table table()
	{
		return table;
	}

	public AbstractDropTable table(Table table)
	{
		this.table = table;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append("DROP TABLE");

		if (this.ifExists())
		{
			buffer.append(" IF EXISTS");
		}

		buffer.append(' ');

		this.provider().provideOutputNameText(buffer, table().name());

		return buffer;
	}
}
