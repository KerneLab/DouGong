package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.InsertMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;

public class STAF extends AbstractTable
{
	@InsertMeta(param = "compId")
	public Column	COMP_ID;

	@InsertMeta(param = "deptId")
	public Column	DEPT_ID;

	@InsertMeta(param = "stafId")
	public Column	STAF_ID;

	public Column	STAF_NAME;

	@InsertMeta(value = "NVL(?salary?, 0.0)")
	public Column	STAF_SALARY;
}
