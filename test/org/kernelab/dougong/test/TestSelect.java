package org.kernelab.dougong.test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.DUAL;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class TestSelect
{
	// public static SQL $ = new SQL(new MariaProvider());
	public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		// Tools.debug(makeSelectHint().toString(new StringBuilder()));
		// Tools.debug(makeSelectExists().toString(new StringBuilder()));
		// Tools.debug(makeSelectPartitioned().toString(new StringBuilder()));
		// Tools.debug(makeSelectNested().toString(new StringBuilder()));
		Tools.debug(maekSelectValuesOracle().toString(new StringBuilder()));
	}

	public static Select maekSelectValuesMaria()
	{
		Calendar c = new GregorianCalendar();
		java.util.Date d = new java.util.Date();
		return $.from($.table(DUAL.class)) //
				.select($.valDate(STR_TO_DATE.class, System.currentTimeMillis()).as("a"), //
						$.valDatetime(STR_TO_DATE.class, System.currentTimeMillis()).as("b"), //
						$.valTimestamp(STR_TO_DATE.class, System.currentTimeMillis()).as("c"), //
						$.valDate(STR_TO_DATE.class, c).as("d"), //
						$.valDatetime(STR_TO_DATE.class, c).as("e"), //
						$.valTimestamp(STR_TO_DATE.class, c).as("f"), //
						$.valDate(STR_TO_DATE.class, d).as("g"), //
						$.valDatetime(STR_TO_DATE.class, d).as("h"), //
						$.valTimestamp(STR_TO_DATE.class, d).as("i"), //
						$.valTime(STR_TO_DATE.class, "20181231", "yyyyMMdd").as("j") //
				);
	}

	public static Select maekSelectValuesOracle()
	{
		Calendar c = new GregorianCalendar();
		java.util.Date d = new java.util.Date();
		return $.from($.table(DUAL.class)) //
				.select($.valDate(TO_DATE.class, System.currentTimeMillis()).as("a"), //
						$.valDatetime(TO_DATE.class, System.currentTimeMillis()).as("b"), //
						$.valTimestamp(TO_TIMESTAMP.class, System.currentTimeMillis()).as("c"), //
						$.valDate(TO_DATE.class, c).as("d"), //
						$.valDatetime(TO_DATE.class, c).as("e"), //
						$.valTimestamp(TO_TIMESTAMP.class, c).as("f"), //
						$.valDate(TO_DATE.class, d).as("g"), //
						$.valDatetime(TO_DATE.class, d).as("h"), //
						$.valTimestamp(TO_TIMESTAMP.class, d).as("i"), //
						$.valTime(TO_DATE.class, "20181231", "yyyyMMdd").as("j") //
				);
	}

	public static Select makeSelectAliasByMeta()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.STAF_ID, //
						s.STAF_NAME.joint(s.STAF_JOB).as("jj"), //
						s.STAF_NAME.as("name") //
				) //
				.to(AbstractSelect.class).fillAliasByMeta() //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectAllAliasByMeta()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select($.all()) //
				.to(AbstractSelect.class).fillAliasByMeta() //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectAllColumns()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.all(), //
						$.param("key").multiply(d.COMP_ID.plus($.expr("2"))).as("k"), //
						d.DEPT_NAME.joint($.param("nm").plus($.expr("1"))).as("j"), //
						$.Null().as("n") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectAllColumns1()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select($.all() //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectCase()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						$.Case() //
								.when(d.COMP_ID.gt($.expr("1")), $.expr("'A'")) //
								// .els(SQL.expr("'Z'")) //
								.as("c") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectExists()
	{
		DEPT d = null;
		STAF s = null;
		return $.from(d = $.view(DEPT.class).as("D")) //
				.where($.notExists($.from(s = $.view(STAF.class).as("S")) //
						.where(s.DEPT_ID.eq(d.DEPT_ID)) //
						.select($.expr("1")))) //
				.select(d.all()) //
		;
	}

	public static Select makeSelectGE()
	{
		DEPT d = null;
		return $.from(d = $.table(DEPT.class)) //
				.where(d.COMP_ID.ge($.expr("1"))) //
				.select(d.COMP_ID);
	}

	public static Select makeSelectPartitioned()
	{
		DEPT d = null;
		return $.from(d = $.table(DEPT.class).partition("ff")) //
				.where(d.COMP_ID.ge($.expr("1"))) //
				.select(d.COMP_ID);
	}

	public static Select makeSelectHint()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						$.Case() //
								.when(d.COMP_ID.gt($.expr("1")), $.expr("'A'")) //
								// .els(SQL.expr("'Z'")) //
								.as("c") //
				) //
				.hint("full(s)") //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectLimit()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID.as("c"), //
						$.Case(d.COMP_ID).when($.expr("1"), $.expr("'s'")).as("d"), //
						s.all() //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
				.limit($.expr("0"), $.expr("1")) //
		;
	}

	public static Select makeSelectNested()
	{
		DEPT d = null;
		STAF s = null;

		Select sel = $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.FK_STAF(d)) //
				.select(d.all(), //
						s.STAF_NAME.as("name") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
				.to(AbstractSelect.class).fillAliasByMeta()
		// .as("t") //
		;

		return $.from(sel) //
				.select($.all()) //
		;
	}

	public static Select makeSelectReferece()
	{
		DEPT d = null;
		STAF s = null;

		Select sel = $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.STAF_NAME.as("name") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		// .as("t") //
		;

		return $.from(sel) //
				.select(sel.item("name").as("nm")) //
		;
	}

	public static Select makeSelectSelectionAsItem()
	{
		DEPT d = null;
		STAF s = null;
		COMP c = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						$.from(c = $.table(COMP.class, "c")) //
								.select($.expr("1")) //
								.where(c.COMP_ID.eq($.expr("1"))) //
								.orderBy(c.COMP_ID).as("i") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectSubquery()
	{
		STAF s = null;

		Select sub = $.from(s = $.table(STAF.class, "s")) //
				.select(s.STAF_ID.as("id"), //
						s.STAF_NAME.as("name") //
				).as("sub");

		return $.from(sub) //
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

		Select sel = $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						$.from(c = $.table(COMP.class, "c")) //
								.select($.expr("1")) //
								.where(c.COMP_ID.eq($.expr("1"))) //
								.orderBy(c.COMP_ID).as("i") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;

		Tools.debug(sel.referItems());

		return sel;
	}
}
