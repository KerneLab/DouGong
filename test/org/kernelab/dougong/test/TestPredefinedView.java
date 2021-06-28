package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestPredefinedView
{
	public static void main(String[] args)
	{
		SQL sql = new SQL(new OracleProvider());
		TestView view = null;

		Select sel = sql.from(view = sql.subquery(TestView.class, "D")) //
				.select(view.DEPT_NAME, //
						view.$("COMP_ID").as("compId"), //
						view.$("comp_id_1").as("compId1") //
				);

		Tools.debug(sel.toString());
	}
}
