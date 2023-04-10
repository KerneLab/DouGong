package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.DUAL;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;
import org.kernelab.dougong.orcl.OracleColumn;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.AbstractWindowFunction;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class TestSelect
{
	public static class SUM extends AbstractWindowFunction
	{
	}

	public static SQL $ = new SQL(new MariaProvider());
	// public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectAliased());
		Tools.debug(makeSelectAliased1());
		Tools.debug(makeSelectHint());
		Tools.debug(makeSelectExists());
		Tools.debug(makeSelectPartitioned());
		Tools.debug(makeSelectExists());
		Tools.debug(makeSelectSubquery());
		Tools.debug(makeSelectReferece());
		Tools.debug(makeSelectReferFunction());
		Tools.debug(makeSelectAllColumns());
		Tools.debug(makeSelectHierarchy());
		if ($.provider() instanceof OracleProvider)
		{
			Tools.debug(makeSelectUsingColumnsFromSubquery());
			Tools.debug(makeSelectValuesOracle());
		}
		if ($.provider() instanceof MariaProvider)
		{
			Tools.debug(makeSelectValuesMaria());
		}
		Tools.debug(makeSelectSetopr());
	}

	public static Select makeSelectAliasByMeta()
	{
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(d = $.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.STAF_ID, //
						s.STAF_NAME.joint(s.STAF_ROLE).as("jj"), //
						s.STAF_NAME.as("name") //
				) //
				.to(AbstractSelect.class).fillAliasByMeta() //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

	public static Select makeSelectAliased()
	{
		STAF s;

		Select sel = $.from(s = $.table(STAF.class)) //
				.where(s.STAF_ID.isNotNull()) //
				.select(s.STAF_ID.as("id"), s.STAF_NAME, s.STAF_SALARY.negative().negative().as("nn"),
						(s.STAF_SALARY.multiply($.v(100))).negative().as("n2"));

		Tools.debug($.from(sel = sel.as("T")).select(sel.ref("id"), sel.ref("STAF_NAME")));

		return $.from(sel = sel.as("R")).select(sel.$("id"), sel.$("STAF_NAME"));
	}

	public static Select makeSelectAliased1()
	{
		STAF s = $.table(STAF.class, "S");

		Select sel = $.from(s) //
				.where(s.STAF_ID.isNotNull()) //
				.select(s.STAF_ID.as("id"), s.STAF_NAME, s.STAF_SALARY);

		Tools.debug(sel);

		return $.from(s = s.as("T")) //
				.where(s.STAF_ID.isNotNull()) //
				.select(s.STAF_ID.as("id"), s.STAF_NAME, s.STAF_SALARY);
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
						d.DEP_NAME.joint($.param("nm").plus($.expr("1"))).as("j"), //
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
		return $.from(d = $.view(DEPT.class, "D")) //
				.where($.notExists($.from(s = $.view(STAF.class, "S")) //
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

	public static Select makeSelectHierarchy()
	{
		DEPT d = null;
		STAF s = null;

		Select select = $.from(d = $.table(DEPT.class, "d")) //
				.innerJoin(s = $.table(STAF.class, "s"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID.as("comp"), //
						s.DEPT_ID.as("dept"), //
						s.STAF_ID.as("staf"), //
						s.STAF_SALARY.multiply($.expr("0.1")).as("tax"), //
						$.func(SUM.class, d.COMP_ID).partitionBy(d.COMP_ID).orderBy(s.STAF_ID)
								.range(d.COMP_ID, d.DEP_NAME).as("s_") //
				) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(s.STAF_ID);

		select.startWith(d.COMP_ID.eq($.expr("1"))) //
				.connectBy($.prior(d.COMP_ID).eq(s.COMP_ID));

		return select;
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
		return $.from(d = $.table(DEPT.class).partition("ff").as("t")) //
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
				.orderBy(sel.$("COMP_ID").asc(), sel.$("name").desc(), sel.$("name").plus($.val(1)).desc()) //
		;
	}

	public static Select makeSelectReferFunction()
	{
		DEPT d = null;

		Select sel = $.from(d = $.table(DEPT.class, "D")) //
				.select(d.COMP_ID, //
						$.func(STACK.class, $.val(2), $.val("ID"), d.DEPT_ID, $.val("NAME"), d.DEP_NAME).as("KEY",
								"VAL"));

		Select sel1 = $.from(sel.alias("V")) //
				.select(sel.$("COMP_ID"), sel.$("KEY"));

		return sel1;
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

		Select sub = $.from(s = $.table(STAF.class, "s`")) //
				.select($.func(TO_CHAR.class, s.STAF_ID).as("id"), //
						s.STAF_NAME.as("nam`e") //
				).as("sub");

		return $.from(sub) //
				.select(sub.$("id"), //
						sub.$("nam`e") //
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

	public static Select makeSelectUsingColumnsFromSubquery()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		Select sel = $.from(c = $.table(COMP.class, "C")) //
				.select($.func(TO_CHAR.class, c.COMP_ID).as("COMP_ID"), c.COM_NAME);

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(sel.alias("E"), sel.$("COMP_ID")) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.COMP_ID, d.DEPT_ID) //
				.select(sel.$("COMP_ID"), d.DEP_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt($.val(0))) //
				.orderBy(d.COMP_ID.to(OracleColumn.class).nullsFirst()) //
		;
	}

	public static Select makeSelectValuesMaria()
	{
		return $.from($.table(DUAL.class)) //
				.select($.val("hey'").as("a"), //
						$.val(123.56).as("b"), //
						$.func(STR_TO_DATE.class, $.val("20211231"), $.formatDT("yyyyMMdd")).as("c") //
				);
	}

	public static Select makeSelectValuesOracle()
	{
		return $.from($.table(DUAL.class)) //
				.select($.val("hey'").as("a"), //
						$.val(123.56).as("b"), //
						$.func(TO_DATE.class, $.val("20211231"), $.formatDT("yyyyMMdd")).as("c") //
				);
	}

	public static Select makeSelectWithOrderByUsingColumns()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.COMP_ID, d.DEPT_ID) //
				.select(c.COMP_ID, d.DEP_NAME, s.STAF_NAME) //
				.where(d.COMP_ID.gt($.val(0))) //
				.orderBy(d.COMP_ID.to(OracleColumn.class).nullsFirst()) //
		;
	}
}
