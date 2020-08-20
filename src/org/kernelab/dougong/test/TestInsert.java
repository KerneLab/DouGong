package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestInsert
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeInsert().toString(new StringBuilder()));
		Tools.debug(makeInsert1().toString(new StringBuilder()));
		Tools.debug(makeInsertByMetaMap().toString(new StringBuilder()));
		Tools.debug(makeSelect().toString(new StringBuilder()));
		Tools.debug(makeSelectPairs().toString(new StringBuilder()));
	}

	public static Insert makeInsert()
	{
		STAF s = null;

		return SQL.insert(s = SQL.table(STAF.class, "s"), s.COMP_ID, s.DEPT_ID) //
				.values(SQL.param("comp"), SQL.param("dept")) //
		;
	}

	public static Insert makeInsert1()
	{
		STAF s = SQL.table(STAF.class, "s");

		return s.insert().columns(s.COMP_ID, s.DEPT_ID) //
				.values(SQL.param("comp"), SQL.param("dept")) //
		;
	}

	public static Insert makeInsertByMetaMap()
	{
		return SQL.table(STAF.class, "s").insertByMetaMap(null);
	}

	public static Insert makeSelect()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(d.DEPT_ID) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
				.insert(s) //
				.columns(s.DEPT_ID) //
		;
	}

	public static Insert makeSelectPairs()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(c = SQL.table(COMP.class, "c"), c.COMP_ID.eq(s.COMP_ID)) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), d.DEPT_ID.eq(s.DEPT_ID)) //
				.select() //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
				.insert(s, s.DEPT_ID, d.DEPT_ID) //
		;
	}

}
