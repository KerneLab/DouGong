package org.kernelab.dougong.test;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;

public class TestFunc
{
	// public static SQL $ = new SQL(new OracleProvider());
	public static SQL $ = new SQL(new MariaProvider());

	public static void main(String[] args)
	{
		System.out.println(makeSelect());
		System.out.println(makeSelectTrimLeading());
	}

	public static Select makeSelect()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(d.COMP_ID, d.DEPT_NAME, s.STAF_NAME, $.func(F_TEST_FUNC.class).as("a")) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
		;
	}

	public static Select makeSelectTrimLeading()
	{
		COMP c = null;

		return $.from(c = $.table(COMP.class, "C")) //
				.select($.func(TRIM_LEADING.class, c.COMP_ID, $.v("0")));
	}
}
