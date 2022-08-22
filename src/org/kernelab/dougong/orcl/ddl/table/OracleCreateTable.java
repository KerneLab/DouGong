package org.kernelab.dougong.orcl.ddl.table;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.AbsoluteKeyMeta;
import org.kernelab.dougong.semi.ddl.table.AbstractCreateTable;

public class OracleCreateTable extends AbstractCreateTable
{
	@Override
	protected boolean isNeed(Column col)
	{
		return col.field().getAnnotation(AbsoluteKeyMeta.class) == null;
	}

	@Override
	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("CREATE");

		if (this.temporary())
		{
			buffer.append(" TEMPORARY");
		}

		buffer.append(" TABLE ");

		buffer.append(this.provider().provideNameText(table().name()));
	}
}
