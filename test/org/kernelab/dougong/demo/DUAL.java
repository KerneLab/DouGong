package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.TypeMeta;
import org.kernelab.dougong.semi.AbstractTable;

@NameMeta(name = "DUAL")
public class DUAL extends AbstractTable
{
	@NameMeta(name = "DUMMY")
	@TypeMeta(type = "VARCHAR2", precision = 1, nullable = TypeMeta.NULLABLE)
	@DataMeta(alias = "dummy")
	public Column	DUMMY;
}
