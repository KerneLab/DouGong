package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestJoin
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectWithJoinOnCondition().toString(new StringBuilder()));
		Tools.debug(makeSelectWithJoinUsingColumns().toString(new StringBuilder()));
		Tools.debug(makeSelectWithNaturalJoin().toString(new StringBuilder()));
		Tools.debug(makeSelectAllWithNaturalJoin().toString(new StringBuilder()));
		Tools.debug(makeSelectSemiJoin().toString(new StringBuilder()));
		Tools.debug(makeSelectUsingWith().toString(new StringBuilder()));
	}

	public static Select makeSelectWithJoinOnCondition()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.leftJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(c.COMP_ID, d.COMP_ID, d.DEP_NAME.asFieldName(), s.STAF_NAME) //
				.where(d.COMP_ID.gt($.expr("0"))) //
		;
	}

	public static Select makeSelectWithJoinUsingColumns()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(c = $.table(COMP.class, "c")) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.COMP_ID) //
				.leftJoin(s = $.table(STAF.class, "s"), s.COMP_ID, s.DEPT_ID) //
				.select(c.COMP_ID, s.DEPT_ID, d.DEP_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt($.expr("0"))) //
		;
	}

	public static Select makeSelectWithNaturalJoin()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(c = $.table(COMP.class, "c")) //
				.natural().innerJoin(d = $.table(DEPT.class, "d")) //
				.natural().leftJoin(s = $.table(STAF.class, "s")) //
				.select(c.COMP_ID, s.DEPT_ID, d.DEP_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt($.expr("0"))) //
		;
	}

	@SuppressWarnings("unused")
	public static Select makeSelectAllWithNaturalJoin()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(c = $.table(COMP.class, "c")) //
				.natural().innerJoin(d = $.table(DEPT.class, "d")) //
				.natural().leftJoin(s = $.table(STAF.class, "s")) //
				.select($.all()) //
				.where(d.COMP_ID.gt($.expr("0"))) //
		;
	}

	@SuppressWarnings("unused")
	public static Select makeSelectSemiJoin()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.natural().semiJoin(d = $.table(DEPT.class, "d")) //
				.select(s.COMP_ID, s.DEPT_ID, s.STAF_ID) //
		;
	}

	@SuppressWarnings("unused")
	public static Select makeSelectUsingWith()
	{
		COMP c;
		Select va, vb, vc;
		View q = null;

		Select sel = $.with( //
				$.with("va").as(va = $.from(c = $.view(COMP.class).as("T")).select(c.all())), //
				$.with("vb").as(vb = $.from($.view(DEPT.class).as("S")).select($.all())), //
				$.with("vc").as(vc = $.from(va = va.as("a")).natural().innerJoin(vb).select($.all())) //
		).from(q = vc.as("c")) //
				.select($.all());

		return sel;
	}
}
