package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.meta.AbsoluteKeyMeta;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.meta.PseudoColumnMeta;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.semi.AbstractTable;

@NameMeta(name = "STAF")
public class STAF extends AbstractTable
{
	@ForeignKeyMeta
	public static final String	FK_STAF_ROLE	= "FK_STAF_ROLE";

	@ForeignKeyMeta
	public static final String	FK_STAF	= "FK_STAF";

	@NameMeta(name = "ROWID")
	@TypeMeta(type = "VARCHAR2", precision = 18, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "rowid", select = "ROWIDTOCHAR(ROWID)")
	@AbsoluteKeyMeta
	@PseudoColumnMeta
	public Column	ROWID;
	
	@NameMeta(name = "COMP_ID")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "compId")
	@PrimaryKeyMeta(ordinal = 1)
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_ID")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "deptId")
	@PrimaryKeyMeta(ordinal = 2)
	public Column	DEPT_ID;

	@NameMeta(name = "STAF_ID")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "stafId")
	@PrimaryKeyMeta(ordinal = 3)
	public Column	STAF_ID;

	@NameMeta(name = "STAF_NAME")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NULLABLE)
	@DataMeta(alias = "stafName")
	public Column	STAF_NAME;

	@NameMeta(name = "STAF_SALARY")
	@TypeMeta(type = "NUMBER", precision = 10, scale = 2, nullable = TypeMeta.NULLABLE)
	@DataMeta(alias = "stafSalary")
	public Column	STAF_SALARY;

	@NameMeta(name = "STAF_ROLE")
	@TypeMeta(type = "VARCHAR2", precision = 5, nullable = TypeMeta.NULLABLE)
	@DataMeta(alias = "stafRole")
	public Column	STAF_ROLE;

	@ForeignKeyMeta
	public ForeignKey FK_STAF_ROLE(ROLE_LIST ref)
	{
		return foreignKey(ref, STAF_ROLE);
	}

	@ForeignKeyMeta
	public ForeignKey FK_STAF(DEPT ref)
	{
		return foreignKey(ref, COMP_ID, DEPT_ID);
	}
}
