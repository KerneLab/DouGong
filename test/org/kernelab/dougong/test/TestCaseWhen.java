package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestCaseWhen
{
	// public static SQL $ = new SQL(new MariaProvider());
	public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectCaseExpr().toString(new StringBuilder()));
	}

	public static Select makeSelectCaseExpr()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						d.DEP_NAME, //
						$.Case($.v(1)).when($.v(1), c.COMP_ID).when($.v(2), d.DEPT_ID).els(s.STAF_ID).as("A"), //
						$.Case().when($.v(1).gt($.v(0)), $.v("a")).when($.v(2).gt($.v(0)), $.v("b")).els($.v("c"))
								.as("B") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy($.Case($.v(1)).when($.v(1), c.COMP_ID).when($.v(2), d.DEPT_ID).els(s.STAF_ID).desc()) //
		;
	}
}
