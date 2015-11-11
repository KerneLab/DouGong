package org.kernelab.dougong.demo;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.orcl.OracleProvider;

public class Demo
{
	public static SQL	SQL	= new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		USER_INFO t;

		Select s = SQL.from(t = SQL.table(USER_INFO.class).as("t")) //
				.select(t.NAME.as("Title")) //
				.where(t.ID.eq(SQL.item("?")));

		System.out.println(s);
	}

}
