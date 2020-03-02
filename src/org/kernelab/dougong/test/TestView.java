package org.kernelab.dougong.test;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.semi.dml.PredeclaredView;

public class TestView extends PredeclaredView
{
	@Override
	public JSON parameters()
	{
		return new JSON();
	}

	@Override
	public Select select(SQL sql)
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return sql.from(s = sql.table(STAF.class, "s")) //
				.innerJoin(c = sql.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.innerJoin(d = sql.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(c.COMP_ID.as("comp_id_1"), d.COMP_ID, d.DEPT_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(sql.expr("0"))) //
		;
	}
}
