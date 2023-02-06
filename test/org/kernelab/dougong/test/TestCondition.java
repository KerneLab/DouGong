package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestCondition
{
	public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelect().toString());
		Tools.debug(makeSelectEmpty().toString());
		Tools.debug(makeSelectNot().toString());
	}

	public static Select makeSelect()
	{
		STAF s = $.table(STAF.class, "s");
		DEPT d = $.table(DEPT.class, "d");

		return $.from(s).innerJoin(d, s.DEPT_ID) //
				.where(s.STAF_SALARY.isNotNull().and(d.DEPT_ID.isNotNull().and(s.STAF_NAME.isNotNull()))) //
				.select(d.DEPT_ID, s.STAF_ID, s.STAF_NAME);
	}

	public static Select makeSelectEmpty()
	{
		STAF s = $.table(STAF.class, "s");

		ComposableCondition c = $.isEmpty(s.STAF_ID);
		c = c.and($.not($.isNotEmpty(s.STAF_NAME)));
		c = c.or(s.DEPT_ID.isNotNull().or(s.COMP_ID.isNotNull()));

		return $.from(s) //
				.where(c) //
				.select(s.STAF_ID, s.STAF_NAME);
	}

	public static Select makeSelectNot()
	{
		STAF s = $.table(STAF.class, "s");

		ComposableCondition c = $.not(s.STAF_ID.isNull());
		c = c.and($.not($.isNotEmpty(s.STAF_NAME)));
		c = c.or(s.DEPT_ID.isNotNull().or(s.COMP_ID.isNotNull()));

		return $.from(s) //
				.where(c) //
				.select(s.STAF_ID, s.STAF_NAME);
	}
}
