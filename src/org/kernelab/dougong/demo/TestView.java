package org.kernelab.dougong.demo;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.semi.dml.PredeclaredView;

@MemberMeta(follow = true)
public class TestView extends PredeclaredView
{
	@NameMeta(name = "comp_id_1")
	public Column	comp_id_1;

	@NameMeta(name = "COMP_ID")
	public Column	COMP_ID;

	@NameMeta(name = "DEPT_NAME")
	public Column	DEPT_NAME;

	@NameMeta(name = "STAF_NAME")
	public Column	STAF_NAME;

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
				.join(c = sql.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.join(d = sql.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(c.COMP_ID.as("comp_id_1"), d.COMP_ID, d.DEPT_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(sql.expr("0"))) //
		;
	}
}
