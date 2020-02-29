package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;
import org.kernelab.dougong.core.meta.InsertMeta;

@MemberMeta()
public class STAF extends AbstractTable
{
	@NameMeta(name = "COMP_ID")
	@InsertMeta(param = "compId")
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_ID")
	@InsertMeta(param = "deptId")
	public Column	DEPT_ID;

	@NameMeta(name = "STAF_ID")
	@InsertMeta(param = "stafId")
	public Column	STAF_ID;

	@NameMeta(name = "STAF_NAME")
	@InsertMeta(param = "stafName")
	public Column	STAF_NAME;

	@NameMeta(name = "STAF_SALARY")
	@InsertMeta(param = "stafSalary")
	public Column	STAF_SALARY;
}
