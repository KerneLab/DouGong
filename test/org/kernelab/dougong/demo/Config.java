package org.kernelab.dougong.demo;

import java.sql.SQLException;

import org.kernelab.basis.sql.DataBase;
import org.kernelab.basis.sql.DataBase.OracleClient;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.maria.MariaProvider;
import org.kernelab.dougong.orcl.OracleProvider;

public class Config
{
	public static final Provider	ORACLE_PROVIDER	= new OracleProvider();

	public static final Provider	MARIA_PROVIDER	= new MariaProvider();

	public static final Provider	PROVIDER		= MARIA_PROVIDER;

	public static final SQL			SQL				= new SQL(PROVIDER);

	private static final DataBase	DB_TEST			= new OracleClient("orcl", "test", "TEST");

	// private static final DataBase DB_TEST = new MariaDB("localhost", 3306,
	// "test", "test", "test");

	private static final DataBase	DB				= DB_TEST;

	public static SQLKit getSQLKit()
	{
		try
		{
			return DB.getSQLKit();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
