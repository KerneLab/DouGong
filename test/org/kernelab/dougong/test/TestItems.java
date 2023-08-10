package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;

public class TestItems
{
	public static SQL $ = new SQL(new MariaProvider());

	public static void main(String[] args)
	{
		Tools.debug(testNotIn());
	}

	public static Select testNotIn()
	{
		STAF s = $.table(STAF.class, "s");
		return $.from(s)
				.where($.list(s.COMP_ID, s.DEPT_ID).not().in($.list($.list($.v(1), $.v(2)), $.list($.v(2), $.v(3)))))
				.select(s.all());
	}
}
