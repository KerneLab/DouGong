package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Insert;
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
	}

	public static Insert makeInsertUpdateSet()
	{
		DEPT d = null;

		return $.insert(d = $.table(DEPT.class, "s"), d.COMP_ID, d.DEPT_ID, d.DEPT_NAME) //
				.values($.param("comp"), $.param("dept"), $.param("name")) //
				.to(MariaInsert.class) //
				.updates(d.DEPT_NAME, $.v("nameVal"));
	}

	public static Insert makeInsertUpdatesByValues()
	{
		DEPT d = null;

		return $.insert(d = $.table(DEPT.class, "s"), d.COMP_ID, d.DEPT_ID, d.DEPT_NAME) //
				.values($.param("comp"), $.param("dept"), $.param("name")) //
				.to(MariaInsert.class) //
				.updatesByValues();
	}
}
