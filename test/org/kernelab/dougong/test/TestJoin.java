package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestJoin
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectWithJoinOnCondition().toString(new StringBuilder()));
		Tools.debug(makeSelectWithJoinUsingColumns().toString(new StringBuilder()));
		Tools.debug(makeSelectWithNaturalJoin().toString(new StringBuilder()));
	}

	public static Select makeSelectWithJoinOnCondition()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(c.COMP_ID, d.COMP_ID, d.DEPT_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
		;
	}

	public static Select makeSelectWithJoinUsingColumns()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(c = SQL.table(COMP.class, "c")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), d.COMP_ID) //
				.innerJoin(s = SQL.table(STAF.class, "s"), s.COMP_ID, s.DEPT_ID) //
				.select(c.COMP_ID, s.DEPT_ID, d.DEPT_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
		;
	}

	public static Select makeSelectWithNaturalJoin()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(c = SQL.table(COMP.class, "c")) //
				.natural().innerJoin(d = SQL.table(DEPT.class, "d")) //
				.natural().innerJoin(s = SQL.table(STAF.class, "s")) //
				.select(c.COMP_ID, s.DEPT_ID, d.DEPT_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
		;
	}
}
