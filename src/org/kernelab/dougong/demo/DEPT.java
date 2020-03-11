package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta()
public class DEPT extends AbstractTable
{
	@NameMeta(name = "COMP_ID2")
	@DataMeta(alias = "compId")
	@PrimaryKeyMeta(ordinal = 1)
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_ID2")
	@DataMeta(alias = "deptId")
	@PrimaryKeyMeta(ordinal = 2)
	public Column	DEPT_ID;

	@NameMeta(name = "DEPT_NAME")
	@DataMeta(alias = "deptName")
	public Column	DEPT_NAME;

	@ForeignKeyMeta
	public ForeignKey FRN_DEPT(COMP ref)
	{
		return foreignKey(ref, COMP_ID);
	}
}
