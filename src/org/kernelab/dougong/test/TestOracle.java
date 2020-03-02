package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.orcl.dml.OracleSelect;
import org.kernelab.dougong.semi.AbstractWindowFunction;

public class TestOracle
{
	public static class SUM extends AbstractWindowFunction
	{
	}

	public final static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelect().toString(new StringBuilder()));
	}

	public static Select makeSelect()
	{
		DEPT d = null;
		STAF s = null;

		OracleProvider orcl = (OracleProvider) SQL.provider();

		OracleSelect select = (OracleSelect) SQL.from(d = SQL.table(DEPT.class, "d")) //
				.join(s = SQL.table(STAF.class, "s"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID.as("comp"), //
						s.DEPT_ID.as("dept"), //
						s.STAF_ID.as("staf"), //
						s.STAF_SALARY.multiply(SQL.expr("0.1")).as("tax"), //
						SQL.function(SUM.class, d.COMP_ID).partitionBy(d.COMP_ID).orderBy(s.STAF_ID)
								.range(d.COMP_ID, d.DEPT_NAME).as("s_") //
				) //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(s.STAF_ID);

		select.startWith(d.COMP_ID.eq(SQL.expr("1"))) //
				.connectBy(orcl.prior(d.COMP_ID).eq(s.COMP_ID));

		return select;
	}
}
