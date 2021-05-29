package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestScope
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectItemsAndScope().toString(new StringBuilder()));
	}

	public static Select makeSelectItemsAndScope()
	{
		DEPT d = null;
		STAF s = null;

		Items list = SQL.list(SQL.expr("1").as("h"), SQL.expr("2")).as("L");

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						d.DEPT_ID, //
						d.DEPT_NAME, //
						list //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0")) //
						.and(d.DEPT_ID.in(list)) //
				) //
				.orderBy(d.COMP_ID) //
		;
	}
}
