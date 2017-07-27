package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractTable;

@MemberMeta(schema = "fdsafd")
public class TestTable extends AbstractTable
{
	public static SQL	SQL	= new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelect().toString(new StringBuilder()));
	}

	public static Delete makeDelete()
	{
		TestTable t = null;

		return SQL.from(t = SQL.table(TestTable.class).as("t")) //
				.where(t.TEST_COLUMN.eq(SQL.expr("1")).and( //
						t.TEST_COLUMN.ge(SQL.expr("'1'")).or(t.TEST_COLUMN.le(SQL.expr("'hey'"))) //
						)) //
				.delete() //
		;
	}

	public static Select makeSelect()
	{
		TestTable t = null;

		return SQL.from(t = SQL.table(TestTable.class).as("t")) //
				.where(SQL.on(1 == 0, //
						t.TEST_COLUMN.eq(SQL.expr("1")).and( //
								t.TEST_COLUMN.ge(SQL.expr("'1'")) //
										.or(2 > 2, t.TEST_COLUMN.le(SQL.expr("'hey'"))) //
								), //
						t.TEST_COLUMN.eq(SQL.expr("2"))) //
				) //
				.select(t.TEST_COLUMN.as("col")) //
		;
	}

	public static Select makeSelectWithJoinOnCondition()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.join(c = SQL.table(COMP.class, "c"), s.COMP_ID.eq(c.COMP_ID)) //
				.join(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(c.COMP_ID, d.COMP_ID, d.DEPT_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
		;
	}

	public static Select makeSelectWithJoinUsingColumns()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.join(c = SQL.table(COMP.class, "c"), c.COMP_ID) //
				.join(d = SQL.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(c.COMP_ID, d.COMP_ID, d.DEPT_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
		;
	}

	public static Update makeUpdate()
	{
		TestTable t = null;

		return SQL.from(t = SQL.table(TestTable.class).as("t")) //
				.where(t.TEST_COLUMN.eq(SQL.expr("1")).and( //
						t.TEST_COLUMN.ge(SQL.expr("'1'")).or(t.TEST_COLUMN.le(SQL.expr("'hey'"))) //
						)) //
				.update() //
				.set(t.TEST_COLUMN, SQL.expr("1")) //
		;
	}

	@NameMeta(name = "fdsafe")
	public Column	TEST_COLUMN;
}
