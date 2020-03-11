package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.GenerateValueMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta()
public class COMP extends AbstractTable
{
	@NameMeta(name = "COMP_ID1")
	@DataMeta(alias = "compId", value = "SEQ_DEMO.nextval")
	@GenerateValueMeta(strategy = GenerateValueMeta.AUTO)
	@PrimaryKeyMeta(ordinal = 1)
	public Column	COMP_ID;

	@NameMeta(name = "COM_NAME")
	@DataMeta(alias = "compName")
	public Column	COM_NAME;
}
