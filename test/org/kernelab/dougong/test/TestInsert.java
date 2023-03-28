package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;
import org.kernelab.dougong.maria.dml.MariaInsert;
import org.kernelab.dougong.orcl.dml.OracleInsert;

public class TestInsert
{
	public static SQL $ = new SQL(new MariaProvider());

	// public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeInsert().toString(new StringBuilder()));
		Tools.debug(makeInsert1().toString(new StringBuilder()));
		Tools.debug(makeInsertByPairs().toString(new StringBuilder()));
		Tools.debug(makeInsertByMetaMap().toString(new StringBuilder()));
		Tools.debug(makeSelect().toString(new StringBuilder()));
		Tools.debug(makeSelect1().toString(new StringBuilder()));
		Tools.debug(makeSelectPairs().toString(new StringBuilder()));
		Tools.debug(makeSelectPairs2().toString(new StringBuilder()));
		Tools.debug(makeSelectWithSelect().toString(new StringBuilder()));
		Tools.debug(makeSelectWithFirst());
		Tools.debug(makeSelectWithReturn().toString(new StringBuilder()));
	}

	public static Insert makeInsert()
	{
		STAF s = null;

		return $.insert(s = $.table(STAF.class, "s"), s.COMP_ID, s.DEPT_ID) //
				.values($.param("comp"), $.param("dept")) //
				.hint("append") //
		;
	}

	public static Insert makeInsert1()
	{
		STAF s = $.table(STAF.class, "s");

		return s.insert().columns(s.COMP_ID, s.DEPT_ID) //
				.values($.param("comp"), $.param("dept")) //
		;
	}

	public static Insert makeInsertByMetaMap()
	{
		return $.table(STAF.class, "s").insertByMetaMap(null);
	}

	public static Insert makeInsertByPairs()
	{
		STAF s = null;

		return $.insert(s = $.table(STAF.class, "s")) //
				.pair(s.COMP_ID, $.param("comp")) //
				.pair(s.DEPT_ID, $.param("dept")) //
				.hint("append") //
		;
	}

	public static Insert makeSelect()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), c.COMP_ID) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.DEPT_ID) //
				.select(d.DEPT_ID, c.COMP_ID) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
				.insert(s) //
				.columns(s.DEPT_ID, s.COMP_ID) //
		;
	}

	public static Insert makeSelect1()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), c.COMP_ID.eq(s.COMP_ID)) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.DEPT_ID.eq(s.DEPT_ID)) //
				.select(d.DEPT_ID, c.COMP_ID) //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
				.insert(s) //
				.columns(s.DEPT_ID, s.COMP_ID) //
		;
	}

	public static Insert makeSelectPairs()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), c.COMP_ID.eq(s.COMP_ID)) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.DEPT_ID.eq(s.DEPT_ID)) //
				.select() //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
				.insert(s) //
				.pairs(s.DEPT_ID, d.DEPT_ID, //
						s.COMP_ID, d.COMP_ID) //
		;
	}

	public static Insert makeSelectPairs2()
	{
		COMP c = null;
		DEPT d = null;
		STAF s = null;

		return $.from(s = $.table(STAF.class, "s")) //
				.innerJoin(c = $.table(COMP.class, "c"), c.COMP_ID.eq(s.COMP_ID)) //
				.innerJoin(d = $.table(DEPT.class, "d"), d.DEPT_ID.eq(s.DEPT_ID)) //
				.select() //
				.where(d.COMP_ID.gt($.expr("0"))) //
				.orderBy(d.COMP_ID.descend()) //
				.insert(s) //
				.pair(s.DEPT_ID, d.DEPT_ID) //
				.pair(s.COMP_ID, d.COMP_ID) //
		;
	}

	public static String makeSelectWithFirst()
	{
		STAF s = null;
		Select va = null;
		Insert ins = $.With($.with("va").as(va = $.from(s = $.table(STAF.class)).select(s.all()))) //
				.from(va).select(va.$("STAF_ID"), va.$("STAF_NAME")).insert(s, s.STAF_ID, s.STAF_NAME);

		Tools.debug(ins.to(MariaInsert.class).toStringWithFirst());

		if (ins instanceof MariaInsert)
		{
			return ((MariaInsert) ins).toStringWithFirst();
		}
		else
		{
			return ins.toString();
		}

		// return ins.toString();
	}

	public static Insert makeSelectWithReturn()
	{
		STAF s = null;
		Insert insert = $.insert(s = $.table(STAF.class, "s")) //
				.pair(s.COMP_ID, $.param("comp")) //
				.pair(s.DEPT_ID, $.param("dept")) //
				.pair(s.STAF_ID, $.param("stafId"));

		if (insert instanceof OracleInsert)
		{
			insert = insert.to(OracleInsert.class) //
					.returning(s.COMP_ID, s.STAF_ID) //
			;
		}

		return insert;
	}

	public static Insert makeSelectWithSelect()
	{
		STAF s = null;
		Select va = null;
		return $.With($.with("va").as(va = $.from(s = $.table(STAF.class)).select(s.all()))) //
				.from(va).select(va.$("STAF_ID"), va.$("STAF_NAME")).insert(s, s.STAF_ID, s.STAF_NAME);
	}
}
