package org.kernelab.dougong.demo;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.PredeclaredView;

public class DemoPreclaredView extends PredeclaredView
{
	public static void main(String[] args)
	{
		DemoPreclaredView dpv = Config.SQL.view(DemoPreclaredView.class);

		Tools.debug(dpv.select().where(dpv.c.COMP_ID.eq(Config.SQL.param("id"))).toString());
	}

	public COMP	c;

	public DEPT	d;

	@Override
	public JSON parameters()
	{
		return null;
	}

	@Override
	protected Select select(SQL sql)
	{
		return sql.from(c = sql.view(COMP.class).as("c"))
				.innerJoin(d = sql.view(DEPT.class).as("d"), c.COMP_ID.eq(d.COMP_ID)) //
				.select(c.COMP_ID.as("compId"), //
						c.COM_NAME.as("compName"), //
						d.DEPT_ID.as("id"), //
						d.DEPT_NAME.as("name"));
	}
}
