package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestUpdate
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeUpdate().toString(new StringBuilder()));
	}

	public static Update makeUpdate()
	{
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.update() //
				.set(s.STAF_NAME, SQL.param("name")) //
				.where(s.STAF_ID.eq(SQL.param("id"))) //
		;
	}
}
