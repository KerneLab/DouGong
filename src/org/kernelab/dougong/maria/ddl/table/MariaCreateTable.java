package org.kernelab.dougong.maria.ddl.table;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.semi.ddl.table.AbstractCreateTable;

public class MariaCreateTable extends AbstractCreateTable
{
	@Override
	protected boolean isNeed(Column col)
	{
		return true;
	}

	@Override
	protected void textOfSelect(StringBuilder buffer)
	{
		buffer.append("\n ");
		this.select().toString(buffer);
	}
}
