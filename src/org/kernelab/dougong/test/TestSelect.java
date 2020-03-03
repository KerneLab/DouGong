package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class TestSelect
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectNested().toString(new StringBuilder()));
	}

	public static Select makeSelectAliasByMeta()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.STAF_ID, //
						s.STAF_NAME.as("name") //
				) //
				.as(AbstractSelect.class).fillAliasByMeta() //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectAllAliasByMeta()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(SQL.all()) //
				.as(AbstractSelect.class).fillAliasByMeta() //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectAllColumns()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.all(), //
						SQL.param("key").multiply(d.COMP_ID.plus(SQL.expr("2"))).as("k"), //
						d.DEPT_NAME.joint(SQL.param("nm").plus(SQL.expr("1"))).as("j"), //
						SQL.Null().as("n") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectAllColumns1()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(SQL.all() //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
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
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectLimit()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID.as("c"), //
						SQL.Case(d.COMP_ID).when(SQL.expr("1"), SQL.expr("'s'")).as("d"), //
						s.all() //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
				.limit(SQL.expr("0"), SQL.expr("1")) //
		;
	}

	public static Select makeSelectNested()
	{
		DEPT d = null;
		STAF s = null;

		Select sel = SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.FRN_STAF(d)) //
				.select(d.all(), //
						s.STAF_NAME.as("name") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
				.as(AbstractSelect.class).fillAliasByMeta()
		// .as("t") //
		;

		return SQL.from(sel) //
				.select(SQL.all()) //
		;
	}

	public static Select makeSelectReferece()
	{
		DEPT d = null;
		STAF s = null;

		Select sel = SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.STAF_NAME.as("name") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		// .as("t") //
		;

		return SQL.from(sel) //
				.select(sel.item("name").as("nm")) //
		;
	}

	public static Select makeSelectSelectionAsItem()
	{
		DEPT d = null;
		STAF s = null;
		COMP c = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						SQL.from(c = SQL.table(COMP.class, "c")) //
								.select(SQL.expr("1")) //
								.where(c.COMP_ID.eq(SQL.expr("1"))) //
								.orderBy(c.COMP_ID).as("i") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectSubquery()
	{
		STAF s = null;

		Select sub = SQL.from(s = SQL.table(STAF.class, "s")) //
				.select(s.STAF_ID.as("id"), //
						s.STAF_NAME.as("name") //
				).as("sub");

		return SQL.from(sub) //
				.select(sub.item("id"), //
						sub.item("name") //
		) //
		;
	}

	public static Select makeSelectTestingItems()
	{
		DEPT d = null;
		STAF s = null;
		COMP c = null;

		Select sel = SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						SQL.from(c = SQL.table(COMP.class, "c")) //
								.select(SQL.expr("1")) //
								.where(c.COMP_ID.eq(SQL.expr("1"))) //
								.orderBy(c.COMP_ID).as("i") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;

		Tools.debug(sel.itemsMap());

		return sel;
	}
}
