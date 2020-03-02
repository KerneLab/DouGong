package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestExpr
{
	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelect().toString());
	}

	public static Select makeSelect()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(d.COMP_ID, d.DEPT_NAME, s.STAF_NAME, SQL.function(F_TEST_FUNC.class)) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(SQL.expr("NVL(SSS)")) //
		;
	}

}
