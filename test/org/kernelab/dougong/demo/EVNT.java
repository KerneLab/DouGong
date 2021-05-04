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
public class EVNT extends AbstractTable
{
	@ForeignKeyMeta
	public static final String	FK_EVENT_SELF	= "FK_EVENT_SELF";

	@NameMeta(name = "ID")
	@DataMeta(alias = "id")
	@PrimaryKeyMeta(ordinal = 1)
	public Column				ID;

	@NameMeta(name = "NAME")
	@DataMeta(alias = "name")
	public Column				NAME;

	@NameMeta(name = "NEXT_ID")
	@DataMeta(alias = "nextId")
	public Column				NEXT_ID;

	@ForeignKeyMeta
	public ForeignKey FK_EVENT_SELF(EVNT ref)
	{
		return foreignKey(ref, NEXT_ID);
	}
}
