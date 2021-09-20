package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta()
public class TEST_TABLE extends AbstractTable
{
	public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		TEST_TABLE t = $.table(TEST_TABLE.class);

		Tools.debug($.from(t).where(t.STAF_ID.isNotNull()).select(t.STAF_NAME).toString());
	}

	@NameMeta(name = "COMP_ID")
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_ID")
	public Column	DEPT_ID;

	@NameMeta(name = "STAF_ID")
	private Column	STAF_ID;

	@NameMeta(name = "STAF_NAME")
	public Column	STAF_NAME;

	public Column STAF_ID()
	{
		return STAF_ID;
	}

	public void STAF_ID(Column sTAF_ID)
	{
		STAF_ID = sTAF_ID;
	}
}
