package org.kernelab.dougong.core.ddl.table;

import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.ddl.DDL;

public interface DropTable extends DDL
{
	public boolean ifExists();

	public DropTable ifExists(boolean ifExists);

	public Table table();

	public DropTable table(Table table);
}
