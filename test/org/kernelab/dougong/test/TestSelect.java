package org.kernelab.dougong.test;

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

	public static Select maekSelectValuesMaria()
	{
		return $.from($.table(DUAL.class)) //
				.select($.val("hey'").as("a"), //
						$.val(123.56).as("b"), //
						$.func(STR_TO_DATE.class, $.val("20211231"), $.formatDT("yyyyMMdd")).as("c") //
				);
	}

	public static Select maekSelectValuesOracle()
	{
		return $.from($.table(DUAL.class)) //
				.select($.val("hey'").as("a"), //
						$.val(123.56).as("b"), //
						$.func(TO_DATE.class, $.val("20211231"), $.formatDT("yyyyMMdd")).as("c") //
				);
	}

	public static void main(String[] args)
	{
		// Tools.debug(makeSelectHint().toString(new StringBuilder()));
		// Tools.debug(makeSelectExists().toString(new StringBuilder()));
		// Tools.debug(makeSelectPartitioned().toString(new StringBuilder()));
		Tools.debug(makeSelectReferece().toString(new StringBuilder()));
		Tools.debug(makeSelectReferFunction().toString(new StringBuilder()));
		// Tools.debug(makeSelectSetopr().toString(new StringBuilder()));
		// Tools.debug(maekSelectValuesMaria().toString(new StringBuilder()));
		// Tools.debug(maekSelectValuesOracle().toString(new StringBuilder()));
	}

	public static Select makeSelectReferFunction()
	{
		DEPT d = null;

		Select sel = $.from(d = $.table(DEPT.class, "D")) //
				.select(d.COMP_ID, //
						$.func(STACK.class, $.val(2), $.val("ID"), d.DEPT_ID, $.val("NAME"), d.DEPT_NAME).as("KEY",
								"VAL"));

		Select sel1 = $.from(sel.alias("V")) //
				.select(sel.$("COMP_ID"), sel.$("KEY"));

		return sel1;
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

	public static Select makeSelectPartitioned()
	{
		DEPT d = null;
		return $.from(d = $.table(DEPT.class).partition("ff")) //
				.where(d.COMP_ID.ge($.expr("1"))) //
				.select(d.COMP_ID);
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
				.as("t") //
		;

		return $.from(sel) //
				.select(sel.ref("name").as("nm")) //
				.orderBy(sel.$("COMP_ID").ascend(), sel.$("name").descend(), sel.$("name").plus($.val(1)).ascend()) //
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

	public static Select makeSelectSetopr()
	{
		DEPT d = null;

		Select s = $.from(d = $.table(DEPT.class)) //
				.select(d.COMP_ID, d.DEPT_ID);
		Select t = null;

		COMP c = null;
		Select u = $.from(c = $.table(COMP.class)) //
				.where(c.COMP_ID.in($.from(t = s.as("T")).select(t.$("COMP_ID")))) //
				.select(c.COMP_ID);

		return $.from(s).select(s.$("COMP_ID")) //
				.unionAll(u);
	}

	public static Select makeSelectSubquery()
	{
		STAF s = null;

		Select sub = $.from(s = $.table(STAF.class, "s")) //
				.select(s.STAF_ID.as("id"), //
						s.STAF_NAME.as("name") //
				).as("sub");

		return $.from(sub) //
				.select(sub.$("id"), //
						sub.$("name") //
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
