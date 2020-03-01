package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;

@MemberMeta()
public class STAF extends AbstractTable
{
	@NameMeta(name = "COMP_ID")
	@DataMeta(alias = "compId")
	@PrimaryKeyMeta
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_ID")
	@DataMeta(alias = "deptId")
	@PrimaryKeyMeta
	public Column	DEPT_ID;

	@NameMeta(name = "STAF_ID")
	@DataMeta(alias = "stafId")
	@PrimaryKeyMeta
	public Column	STAF_ID;

	@NameMeta(name = "STAF_NAME")
	public Column	STAF_NAME;

	@NameMeta(name = "STAF_SALARY")
	@DataMeta(value = "NVL(?stafSalary?,0.0)")
	public Column	STAF_SALARY;

	public ForeignKey foreignKeyOfCOMP(COMP ref)
	{
		return this.foreignKey(ref, this.COMP_ID);
	}

	public ForeignKey foreignKeyOfCOMP(DEPT ref)
	{
		return this.foreignKey(ref, this.COMP_ID, this.DEPT_ID);
	}
}
