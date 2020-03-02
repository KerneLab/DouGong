package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestForeignKey
{
	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelect().toString());
	}

	public static Select makeSelect()
	{
		STAF s = SQL.table(STAF.class, "s");
		DEPT d = SQL.table(DEPT.class, "d");
		COMP c = SQL.table(COMP.class, "c");

		return SQL.from(d).innerJoin(s, s.FRN_STAF(d)) //
				.innerJoin(c, d.FRN_DEPT(c)) //
				.where(s.STAF_SALARY.isNotNull().and(d.DEPT_ID.isNotNull().and(s.STAF_NAME.isNotNull()))) //
				.select(d.DEPT_ID, s.STAF_ID, s.STAF_NAME);
	}
}
