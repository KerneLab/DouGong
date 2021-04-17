package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.AbstractSubquery;

public class TestSubquery extends AbstractSubquery
{
	public static void main(String[] args)
	{
		SQL sql = TestTable.SQL;

		TestSubquery sub = null;

		Select s = sql.from(sub = TestTable.makeSelect().to(TestSubquery.class).as("T")) //
				.where(sub.col.isNotNull()) //
				.select(sub.all(), sql.func(F_TEST_FUNC.class, sub.col).as("FF"));

		Tools.debug(s.toString());
	}

	public Column col;
}
