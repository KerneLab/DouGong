package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.AbstractSubquery;

public class TestSubquery extends AbstractSubquery
{
	public static void main(String[] args)
	{
		TestSubquery sub = null;

		Select s = TestTable.SQL.from(sub = TestTable.makeSelect().as(TestSubquery.class).as("T")) //
				.where(sub.col.isNotNull()) //
				.select(sub.col.as("c"));

		Tools.debug(s.toString());

	}

	public Column col;
}
