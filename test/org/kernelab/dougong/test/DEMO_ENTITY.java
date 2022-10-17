package org.kernelab.dougong.test;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.GenerateValueMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta(schema = "demo")
public class DEMO_ENTITY extends AbstractTable
{
	public static SQL	$	= new SQL(new OracleProvider());

	@NameMeta(name = "ROWID")
	@DataMeta(alias = "rowid")
	@GenerateValueMeta(strategy = GenerateValueMeta.IDENTITY)
	public Column		ROWID;

	@NameMeta(name = "ID")
	@DataMeta(alias = "id", value = "REPLACE(UUID(),'-','')")
	public Column		ID;

	@NameMeta(name = "GENDER")
	@DataMeta(alias = "gender")
	public Column		GENDER;
}
