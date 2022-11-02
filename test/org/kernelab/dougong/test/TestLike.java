package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;

public class TestLike
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new MariaProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectLike().toString(new StringBuilder()));
		Tools.debug(SQL.patnAmong(SQL.expr("'Mik'"), "\\").toString(new StringBuilder()));

		// Tools.debug(makeSelectCase().toString(new StringBuilder()));
	}

	public static Select makeSelectCase()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						SQL.Case() //
								.when(d.COMP_ID.gt(SQL.expr("1")), SQL.expr("'A'")) //
								// .els(SQL.expr("'Z'")) //
								.as("c") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0")) //
						.and(d.COMP_ID.like(SQL.expr("'sfd%'"), SQL.expr("'\\'")))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectLike()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.all() //
				) //
				.where(s.STAF_NAME.like(SQL.patnAmong(SQL.param("name"))) //
						.and(s.STAF_ID.iLike(SQL.patnAmong("name")))) //
				.orderBy(d.COMP_ID) //
		;
	}
}
