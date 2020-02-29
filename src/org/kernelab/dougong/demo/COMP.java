package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;
import org.kernelab.dougong.core.meta.InsertMeta;

@MemberMeta()
public class COMP extends AbstractTable
{
	@NameMeta(name = "COMP_ID")
	@InsertMeta(param = "compId")
	public Column	COMP_ID;

	@NameMeta(name = "COM_NAME")
	@InsertMeta(param = "comName")
	public Column	COM_NAME;
}
