package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Merge;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestMerge
{
	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeMerge().toString(new StringBuilder()));
	}

	public static Merge makeMerge()
	{
		STAF t = null, s = null;

		return SQL.merge(t = SQL.table(STAF.class, "t")) //
				.using(s = SQL.table(STAF.class, "s")) //
				.on(t.COMP_ID.eq(s.COMP_ID)) //
				.whenMatched().update() //
				.sets(t.DEPT_ID, s.DEPT_ID, //
						t.STAF_ID, s.STAF_ID //
				) //
				.whenNotMatched() //
				.inserts(t.COMP_ID, s.COMP_ID, //
						t.DEPT_ID, s.DEPT_ID, //
						t.STAF_ID, s.STAF_ID //
				);
	}

}
