package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Pivot;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.AbstractView;

public class TestPivot
{
	// public static SQL $ = new SQL(new MariaProvider());
	public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectWithPivot().toString(new StringBuilder()));
		Tools.debug(makeSelectWithPivot1().toString(new StringBuilder()));
		Tools.debug(makeSelectWithPivot2().toString(new StringBuilder()));
	}

	public static Select makeSelectWithPivot()
	{
		STAF s = null;
		Pivot p = null;
		return $.from(p = (s = $.table(STAF.class, "S")) //
				.to(AbstractView.class) //
				.pivot($.func(SUM.class, s.STAF_SALARY).as("N"), //
						$.func(MAX.class, s.STAF_SALARY).as("M")) //
				.pivotFor(s.COMP_ID) //
				.pivotIn($.val("1").as("1"), $.val("3").as("3"))) //
				.select(p.all());
	}

	public static Select makeSelectWithPivot1()
	{
		STAF s = null;
		Pivot p = null;
		return $.from(p = (s = $.table(STAF.class, "S")) //
				.to(AbstractView.class) //
				.pivot($.func(SUM.class, s.STAF_SALARY).as("N")) //
				.pivotFor(s.COMP_ID) //
				.pivotIn($.val("1").as("1"), $.val("3").as("3"))) //
				.select(p.all());
	}

	public static Select makeSelectWithPivot2()
	{
		STAF s = null;
		Pivot p = null;
		return $.from(p = (s = $.table(STAF.class, "S")) //
				.to(AbstractView.class) //
				.pivot($.func(SUM.class, s.STAF_SALARY).as("N"), //
						$.func(MAX.class, s.STAF_SALARY).as("M")) //
				.pivotFor(s.COMP_ID, s.DEPT_ID) //
				.pivotIn($.list($.val("1"), $.val("11")).as("1"), //
						$.list($.val("3"), $.val("d2")).as("3"))) //
				.select(p.all());
	}
}
