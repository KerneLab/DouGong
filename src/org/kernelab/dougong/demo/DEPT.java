package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.semi.AbstractTable;

public class DEPT extends AbstractTable
{
	@PrimaryKeyMeta(position = 1)
	public Column	COMP_ID;

	@PrimaryKeyMeta(position = 2)
	public Column	DEPT_ID;

	public Column	DEPT_NAME;
}
