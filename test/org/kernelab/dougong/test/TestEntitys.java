package org.kernelab.dougong.test;

import java.sql.SQLException;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.demo.Config;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.Department;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestEntitys
{
	// public static SQL SQL = new SQL(new MariaProvider());

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		try
		{
			Tools.debug(Entitys.existsObject(Config.getSQLKit(), SQL, Department.class, "1", "12"));
			Tools.debug(Entitys.existsObject(Config.getSQLKit(), SQL, DEPT.class, "1", "12"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
