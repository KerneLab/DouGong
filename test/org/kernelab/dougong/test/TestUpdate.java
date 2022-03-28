package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestUpdate
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeUpdate().toString(new StringBuilder()));
		Tools.debug(makeUpdateJoin().toString(new StringBuilder()));
	}

	public static Update makeUpdate()
	{
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.update() //
				.hint("index(s)") //
				.set(s.STAF_NAME, SQL.param("name")) //
				.where(s.STAF_ID.eq(SQL.param("id"))) //
		;
	}

	public static Update makeUpdateJoin()
	{
		STAF s = null;
		DEPT d = null;

		return SQL.from(s = SQL.table(STAF.class, "S")) //
				.update() //
				.innerJoin(d = SQL.table(DEPT.class, "D"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.set(s.STAF_SALARY, s.STAF_SALARY.plus(SQL.val(1))) //
				.where(d.COMP_ID.isNotNull());
	}
}
