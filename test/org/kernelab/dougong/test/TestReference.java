package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.maria.MariaProvider;

public class TestReference
{
	public static SQL $ = new SQL(new MariaProvider());
	// public static SQL $ = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		// Tools.debug(makeSelect());
		Tools.debug(makeSelect1());
	}

	public static Select makeSelect()
	{
		STAF s = $.table(STAF.class, "S");

		return $.from(s = s.as("T")) //
				.where(s.STAF_ID.isNotNull()) //
				.select(s.STAF_ID.as("id"), s.STAF_NAME, s.ref("XX"));
	}

	public static Select makeSelect1()
	{
		STAF s = $.table(STAF.class, "S");

		Select sel = $.from(s = s.as("T")) //
				.where(s.STAF_ID.isNotNull()) //
				.select(s.STAF_ID.as("id"), s.STAF_NAME, s.STAF_SALARY);

		return $.from(sel).select(sel.$("id"), sel.$("STAF_NAME"), sel.$("staf_sal"));
	}
}
