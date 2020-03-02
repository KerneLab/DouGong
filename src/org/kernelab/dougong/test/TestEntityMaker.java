package org.kernelab.dougong.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.kernelab.basis.sql.DataBase;
import org.kernelab.basis.sql.DataBase.OracleClient;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.util.EntityMaker;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestEntityMaker
{
	public static void main(String[] args) throws SQLException, FileNotFoundException
	{
		Provider provider = new OracleProvider();
		DataBase db = new OracleClient("orcl", "test", "TEST");

		// Provider provider = new MariaProvider();
		// DataBase db = new MariaDB("localhost", 3316, "test", "test", "test");

		SQL sql = new SQL(provider);
		SQLKit kit = db.getSQLKit();

		File base = new File("E:\\project\\dougong\\src");

		EntityMaker.makeTable(provider, kit, "COMP", "org.kernelab.dougong.demo", base, null, null);

		EntityMaker.makeView(provider, kit, sql.subquery(TestView.class), "org.kernelab.dougong.demo", base, "", null);
	}
}
