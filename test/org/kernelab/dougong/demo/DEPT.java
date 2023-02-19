package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.meta.AbsoluteKeyMeta;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.meta.PseudoColumnMeta;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta()
@NameMeta(name = "DEPT")
public class DEPT extends AbstractTable
{
	@ForeignKeyMeta
	public static final String	FK_DEPT	= "FK_DEPT";

	@NameMeta(name = "ROWID")
	@TypeMeta(type = "VARCHAR2", precision = 18, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "rowid", select = "ROWIDTOCHAR(ROWID)")
	@AbsoluteKeyMeta
	@PseudoColumnMeta
	public Column				ROWID;

	@NameMeta(name = "COMP_ID")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "compId")
	@PrimaryKeyMeta(ordinal = 1)
	public Column				COMP_ID;

	@NameMeta(name = "DEPT_ID")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "deptId")
	@PrimaryKeyMeta(ordinal = 2)
	public Column				DEPT_ID;

	@NameMeta(name = "DEPT_NAME")
	@TypeMeta(type = "VARCHAR2", precision = 20, nullable = TypeMeta.NULLABLE)
	@DataMeta(alias = "deptName")
	public Column				DEPT_NAME;

	@ForeignKeyMeta
	public ForeignKey FK_DEPT(COMP ref)
	{
		return foreignKey(ref, COMP_ID);
	}
}
