package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.param.Param;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;

public class TestAlias
{
	public static SQL $ = new SQL(new MariaProvider());
	// public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelect1());
	}

	public static Select makeSelect()
	{
		DEPT d = null;
		STAF s = null;

		StringItem i = $.val("abc");
		Param<?> p = $.param("p1", 1);

		Select select = $.from(d = $.table(DEPT.class, "d")) //
				.innerJoin(s = $.table(STAF.class, "s"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID.as("comp"), //
						d.COMP_ID.as("compId"), //
						i.as("aa"), //
						i.as("bb"), //
						p.as("p1"), //
						p.as("p2") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(s.STAF_ID);

		return select;
	}

	public static Select makeSelect1()
	{
		DEPT d = null;
		STAF s = null;

		Select select = $.from(d = $.table(DEPT.class, "d")) //
				.innerJoin(s = $.table(STAF.class, "s"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, d.DEP_NAME, d.DEPT_ID.as("dpId")) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(s.STAF_ID) //
				// .fillAliasByField() //
				.fillAliasByMeta() //
		// .fillAliasByName() //
		;

		Tools.debug(select.as("S"));

		Tools.debug(select.as("T").ref("compId"));

		return select;
	}
}
