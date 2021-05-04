package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestDelete
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeDelete().toString(new StringBuilder()));
	}

	public static Delete makeDelete()
	{
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.where(s.COMP_ID.gt(SQL.expr("0"))) //
				.delete() //
		;
	}
}
