package org.kernelab.dougong.demo;

import java.util.Map;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.PredefinedView;

public class DemoPredefinedView extends PredefinedView
{
	public static void main(String[] args)
	{
		DemoPredefinedView v = Config.SQL.view(DemoPredefinedView.class);

		System.out.println(v.select().where(v.c.COMP_ID.eq(Config.SQL.param("compId"))));

		System.out.println(v.select().where(v.d.DEPT_ID.eq(Config.SQL.param("deptId"))).orderBy(v.d.DEPT_NAME));
	}

	public COMP	c;

	public DEPT	d;

	@Override
	public Map<String, Object> parameters()
	{
		return null;
	}

	@Override
	protected Select select(SQL sql)
	{
		return sql.from(c = sql.view(COMP.class).as("c")) //
				.innerJoin(d = sql.view(DEPT.class).as("d"), d.FK_DEPT(c)) //
				.select(c.COMP_ID.as("compId"), //
						c.COM_NAME.as("compName"), //
						d.DEPT_ID.as("id"), //
						d.DEPT_NAME.as("name"));
	}
}
