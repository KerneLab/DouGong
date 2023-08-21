package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestSetopr
{
	public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectSetopr());
		Tools.debug(makeSelectSetopr1());
		Tools.debug(makeSelectSetopr2());
	}

	public static Select makeSelectSetopr()
	{
		DEPT d = null;

		Select s = $.from(d = $.table(DEPT.class, "A")) //
				.select(d.COMP_ID, d.DEPT_ID) //
				.orderBy(d.DEPT_ID) //
				.limit($.v(1), $.v(2)) //
		;

		Select u = $.from(d = $.table(DEPT.class, "B")) //
				.select(d.COMP_ID, d.DEPT_ID) //
				.orderBy(d.COMP_ID) //
				.limit($.v(2), $.v(1)) //
		;

		return s.unionAll(u);
	}

	public static Select makeSelectSetopr1()
	{
		Select a = makeSelectSetopr().alias("A");

		return $.from(a).select(a.$("COMP_ID"));
	}

	public static Select makeSelectSetopr2()
	{
		Select a = makeSelectSetopr().alias("A");

		STAF s = null;

		return $.from(s = $.table(STAF.class, "S")).where($.list(s.COMP_ID, s.DEPT_ID).in(a)).select(s.all());
	}

}
