package org.kernelab.dougong.core.ddl.table;

import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.ddl.DDL;
import org.kernelab.dougong.core.dml.Select;

public interface CreateTable extends DDL
{
	public boolean ifNotExists();

	public CreateTable ifNotExists(boolean ifNotExists);

	public String options();

	public CreateTable options(String options);

	public Select select();

	public CreateTable select(Select select);

	public Table table();

	public CreateTable table(Table table);

	public boolean temporary();

	public CreateTable temporary(boolean temporary);
}
