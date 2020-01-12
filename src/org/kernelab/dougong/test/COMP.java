package org.kernelab.dougong.test;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;

@MemberMeta(follow = true)
public class COMP extends AbstractTable
{
	@NameMeta(name = "COMP_ID")
	public Column	COMP_ID;

	@NameMeta(name = "COM_NAME")
	public Column	COM_NAME;
}
