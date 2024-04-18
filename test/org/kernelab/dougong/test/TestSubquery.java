package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractSubquery;

public class TestSubquery extends AbstractSubquery
{
	protected static final SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		TestSubquery sub = null;
		Select s = $.from(sub = TestTable.makeSelect().to(TestSubquery.class).as("T")) //
				.where(sub.col.isNotNull()) //
				.select(sub.all(), sub.col.as("cc"), $.func(F_TEST_FUNC.class, sub.col).as("FF"));
		Tools.debug(s.toString());

		DEMO_ENTITY t = $.table(DEMO_ENTITY.class);
		Insert i = $.insert(t).select(sub).pairs(t.ID, sub.col);
		Tools.debug(i);

		DEMO_ENTITY d = $.table(new DEMO_ENTITY());
		DEMO_ENTITY e = $.from(d = d.as("D")).where(d.ID.isNotNull().and(d.$("GENDER").isNotNull())).select(d.all())
				.to(new DEMO_ENTITY());
		Tools.debug($.from(e = e.as("E")).where(e.GENDER.eq($.v(1))).select(e.ID, e.$("GENDER")));
	}

	public Column col;
}
