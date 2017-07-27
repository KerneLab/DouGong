package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.AbstractSubquery;

public class TestSubquery extends AbstractSubquery
{

	public static void main(String[] args)
	{
		TestSubquery sq = TestTable.SQL.subquery(TestSubquery.class, TestTable.makeSelect());

		Select s = TestTable.SQL.from(sq).select(sq.col);
		
		Tools.debug(s.toString());

	}

	public Column	col;

}
