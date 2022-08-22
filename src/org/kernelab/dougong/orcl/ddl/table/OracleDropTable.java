package org.kernelab.dougong.orcl.ddl.table;

import org.kernelab.dougong.semi.ddl.table.AbstractDropTable;

public class OracleDropTable extends AbstractDropTable
{
	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append("DROP TABLE");

		buffer.append(' ');

		this.provider().provideOutputNameText(buffer, table().name());

		return buffer;
	}
}
