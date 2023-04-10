package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;

public class TestOrder
{
	public static SQL SQL = new SQL(new MariaProvider());
	// public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectWithOrderByCondition().toString(new StringBuilder()));
		Tools.debug(makeSelectWithOrderByUsingColumns().toString(new StringBuilder()));
		Tools.debug(makeSelectWithOrderBySelectExpr().toString(new StringBuilder()));
	}

	public static Select makeSelectWithOrderBySelectExpr()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, d.DEP_NAME, SQL.func(F_TEST_FUNC.class, s.STAF_NAME).as("FF")) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(SQL.$("FF").nullsLast().descend()) //
		;
	}

	public static Select makeSelectWithOrderByCondition()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, d.DEP_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID.nullsLast().descend()) //
		;
	}

	public static Select makeSelectWithOrderByUsingColumns()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), d.COMP_ID, d.DEPT_ID) //
				.select(c.COMP_ID, d.DEP_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(SQL.val(0))) //
				.orderBy(d.COMP_ID.nullsFirst()) //
		;
	}
}
