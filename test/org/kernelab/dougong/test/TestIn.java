package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestIn
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeIn(makeSelect()).toString(new StringBuilder()));
	}

	public static Select makeSelect()
	{
		COMP c = null;
		DEPT d = null;
		@SuppressWarnings("unused")
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(d.COMP_ID) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID.desc()) //
		;
	}

	public static Select makeIn(Select scope)
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(d.COMP_ID, //
						d.DEP_NAME, //
						s.STAF_NAME, //
						SQL.func(F_TEST_FUNC.class, SQL.expr("1")) //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0")) //
						.and(d.COMP_ID.in(scope))) //
				.orderBy(d.COMP_ID.desc()) //
		;
	}
}
