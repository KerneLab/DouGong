package org.kernelab.dougong.demo;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.orcl.OracleProvider;

public class DemoSelect
{
	public final static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelect().toString());
	}

	public static Select makeSelect()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(d = SQL.table(DEPT.class, "d")) //
				.innerJoin(s = SQL.table(STAF.class, "s"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID.as("comp"), //
						s.DEPT_ID.as("dept"), //
						s.STAF_ID.as("staf"), //
						s.STAF_SALARY.multiply(SQL.expr("0.1")).as("tax") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(s.STAF_ID);
	}
}
