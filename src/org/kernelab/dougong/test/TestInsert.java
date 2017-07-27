package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestInsert
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeInsert().toString(new StringBuilder()));
		Tools.debug(makeSelect().toString(new StringBuilder()));
	}

	public static Insert makeInsert()
	{
		STAF s = null;

		return SQL.insert(s = SQL.table(STAF.class, "s"), s.COMP_ID, s.DEPT_ID) //
				.values(SQL.param("comp"), SQL.param("dept")) //
		;
	}

	public static Insert makeSelect()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.join(c = SQL.table(COMP.class, "c"), c.COMP_ID) //
				.join(d = SQL.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(d.COMP_ID) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
				.insert(s, s.COMP_ID, s.DEPT_ID) //
		;
	}

}
