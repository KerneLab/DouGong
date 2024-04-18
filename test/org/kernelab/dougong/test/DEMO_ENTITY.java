package org.kernelab.dougong.test;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.GenerateValueMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PseudoColumnMeta;
import org.kernelab.dougong.semi.dml.AbstractFacade;

@MemberMeta(schema = "demo")
@NameMeta(name = "real_entity")
public class DEMO_ENTITY extends AbstractFacade
{
	@NameMeta(name = "ROWID")
	@DataMeta(alias = "rowid")
	@GenerateValueMeta(strategy = GenerateValueMeta.IDENTITY)
	@PseudoColumnMeta
	public Column	ROWID;

	@NameMeta(name = "ID")
	@DataMeta(alias = "id", value = "REPLACE(UUID(),'-','')")
	public Column	ID;

	@NameMeta(name = "GENDER")
	@DataMeta(alias = "gender")
	public Column	GENDER;
}
