package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestDelete
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeDelete().toString(new StringBuilder()));
		Tools.debug(makeDeleteJoin().toString(new StringBuilder()));
	}

	public static Delete makeDelete()
	{
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.where(s.COMP_ID.gt(SQL.expr("0"))) //
				.delete() //
		;
	}

	public static Delete makeDeleteJoin()
	{
		STAF s = null;
		DEPT d = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "D"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.where(s.COMP_ID.gt(SQL.expr("0"))) //
				.delete(s, d) //
		;
	}
}
