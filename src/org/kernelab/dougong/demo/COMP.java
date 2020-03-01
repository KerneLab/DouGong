package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.semi.dml.AbstractTable;

@MemberMeta()
public class COMP extends AbstractTable
{
	@NameMeta(name = "COMP_ID")
	@DataMeta(alias = "compId")
	@PrimaryKeyMeta
	public Column	COMP_ID;

	@NameMeta(name = "COM_NAME")
	@DataMeta(alias = "comName")
	public Column	COM_NAME;
}
