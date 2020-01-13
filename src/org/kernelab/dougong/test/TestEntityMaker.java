package org.kernelab.dougong.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.kernelab.basis.sql.DataBase;
import org.kernelab.basis.sql.DataBase.OracleClient;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.util.EntityMaker;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestEntityMaker
{
	public static void main(String[] args) throws SQLException, FileNotFoundException
	{
		DataBase db = null;
		db = new OracleClient("orcl", "test", "TEST");
		// db = new MariaDB("localhost", 3316, "test", "test", "test");

		SQLKit kit = db.getSQLKit();

		SQL sql = new SQL(new OracleProvider());

		File base = new File("E:\\project\\dougong\\src");

		EntityMaker.makeTable(kit, "COMP", "org.kernelab.dougong.test", base, "", null);

		EntityMaker.makeView(kit, sql.subquery(TestView.class), "org.kernelab.dougong.demo", base, "", null);
	}
}
