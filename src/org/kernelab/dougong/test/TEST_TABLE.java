package org.kernelab.dougong.test;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;

@MemberMeta()
public class TEST_TABLE extends AbstractTable
{
	@NameMeta(name = "COMP_ID")
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_ID")
	public Column	DEPT_ID;

	@NameMeta(name = "STAF_ID")
	public Column	STAF_ID;

	@NameMeta(name = "STAF_NAME")
	public Column	STAF_NAME;
}
