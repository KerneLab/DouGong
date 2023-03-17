package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.semi.AbstractTable;

@NameMeta(name = "ROLE_LIST")
public class ROLE_LIST extends AbstractTable
{
	@NameMeta(name = "ROLE_ID")
	@TypeMeta(type = "VARCHAR2", precision = 5, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "roleId")
	@PrimaryKeyMeta(ordinal = 1)
	public Column	ROLE_ID;

	@NameMeta(name = "ROLE_NAME")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "roleName")
	public Column	ROLE_NAME;
}
