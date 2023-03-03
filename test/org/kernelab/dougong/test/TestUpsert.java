package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.maria.MariaProvider;
import org.kernelab.dougong.maria.dml.MariaInsert;

public class TestUpsert
{
	public static SQL $ = new SQL(new MariaProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeInsertUpdateSet());
		Tools.debug(makeInsertUpdatesByValues());
		Tools.debug(makeInsertSelectUpdatesByValues());
		Tools.debug(makeInsertSelectUpdatesByColumns());
	}

	public static Insert makeInsertUpdateSet()
	{
		DEPT d = null;

		return $.insert(d = $.table(DEPT.class, "s"), d.COMP_ID, d.DEPT_ID, d.DEP_NAME) //
				.values($.param("comp"), $.param("dept"), $.param("name")) //
				.to(MariaInsert.class) //
				.updates(d.DEP_NAME, $.v("nameVal"));
	}

	public static Insert makeInsertUpdatesByValues()
	{
		DEPT d = null;

		return $.insert(d = $.table(DEPT.class, "s"), d.COMP_ID, d.DEPT_ID, d.DEP_NAME) //
				.values($.param("comp"), $.param("dept"), $.param("name")) //
				.to(MariaInsert.class) //
				.updatesByValues();
	}

	public static Insert makeInsertSelectUpdatesByValues()
	{
		DEPT d = null;
		Select s = $.from($.table(DEPT.class, "R")).select($.all());
		return $.insert(d = $.table(DEPT.class, "s"), d.COMP_ID, d.DEPT_ID, d.DEP_NAME) //
				.select(s) //
				.pairs(d.COMP_ID, s.$("COMP_ID"), d.DEPT_ID, s.$("DEPT_ID"), d.DEP_NAME, s.$("DEPT_NAME")) //
				.to(MariaInsert.class) //
				.updatesByValues();
	}

	public static Insert makeInsertSelectUpdatesByColumns()
	{
		DEPT d = null;
		Select s = $.from($.table(DEPT.class, "R")).select($.all());
		return $.insert(d = $.table(DEPT.class, "s"), d.COMP_ID, d.DEPT_ID, d.DEP_NAME) //
				.select(s) //
				.pairs(d.COMP_ID, s.$("COMP_ID"), d.DEPT_ID, s.$("DEPT_ID"), d.DEP_NAME, s.$("DEPT_NAME")) //
				.to(MariaInsert.class) //
				.updatesOfColumns(d.DEP_NAME, d.DEPT_ID);
	}
}
