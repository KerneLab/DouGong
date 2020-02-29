package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;

@MemberMeta()
public class STAF extends AbstractTable
{
	@NameMeta(name = "COMP_ID")
	@DataMeta(alias = "compId")
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_ID")
	@DataMeta(alias = "deptId")
	public Column	DEPT_ID;

	@NameMeta(name = "STAF_ID")
	@DataMeta(alias = "stafId")
	public Column	STAF_ID;

	@NameMeta(name = "STAF_NAME")
	public Column	STAF_NAME;

	@NameMeta(name = "STAF_SALARY")
	@DataMeta(value = "NVL(?stafSalary?,0.0)")
	public Column	STAF_SALARY;
}
