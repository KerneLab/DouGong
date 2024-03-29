package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.meta.PseudoColumnMeta;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.core.meta.AbsoluteKeyMeta;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta()
@NameMeta(name = "COMP")
public class COMP extends AbstractTable
{
	@NameMeta(name = "ROWID")
	@TypeMeta(type = "VARCHAR2", precision = 18, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "rowid", select = "ROWIDTOCHAR(?.ROWID)")
	@AbsoluteKeyMeta
	@PseudoColumnMeta
	public Column	ROWID;

	@NameMeta(name = "COMPID")
	@TypeMeta(type = "VARCHAR2", precision = 10, nullable = TypeMeta.NO_NULLS)
	@DataMeta(alias = "compId")
	@PrimaryKeyMeta(ordinal = 1)
	public Column	COMP_ID;

	@NameMeta(name = "COM_NAME")
	@TypeMeta(type = "VARCHAR2", precision = 20, nullable = TypeMeta.NULLABLE)
	@DataMeta(alias = "compName")
	public Column	COM_NAME;
}
