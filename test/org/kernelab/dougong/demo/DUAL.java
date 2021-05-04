package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta()
@NameMeta(name = "DUAL")
public class DUAL extends AbstractTable
{
	public Column DUMMY;
}
