package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.semi.AbstractTable;

@NameMeta(name = "ROLE_JOB")
public class ROLE_JOB extends AbstractTable
{
	@ForeignKeyMeta
	public static final String	FK_ROLE_JOB_ROLE	= "FK_ROLE_JOB_ROLE";

	@ForeignKeyMeta
	public static final String	FK_ROLE_JOB_JOB	= "FK_ROLE_JOB_JOB";

	@NameMeta(name = "ROLE_ID")
	@TypeMeta(type = "VARCHAR2", precision = 5, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "roleId")
	@PrimaryKeyMeta(ordinal = 1)
	public Column	ROLE_ID;

	@NameMeta(name = "ROLE_JOB")
	@TypeMeta(type = "VARCHAR2", precision = 5, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "roleJob")
	@PrimaryKeyMeta(ordinal = 2)
	public Column	ROLE_JOB;

	@ForeignKeyMeta
	public ForeignKey FK_ROLE_JOB_ROLE(ROLE_LIST ref)
	{
		return foreignKey(ref, ROLE_ID);
	}

	@ForeignKeyMeta
	public ForeignKey FK_ROLE_JOB_JOB(JOBS ref)
	{
		return foreignKey(ref, ROLE_JOB);
	}
}
