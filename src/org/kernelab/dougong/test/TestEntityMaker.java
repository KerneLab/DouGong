package org.kernelab.dougong.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.kernelab.basis.sql.DataBase;
import org.kernelab.basis.sql.DataBase.OracleClient;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.core.util.EntityMaker;

public class TestEntityMaker
{
	public static void main(String[] args) throws SQLException, FileNotFoundException
	{
		DataBase db = null;
		db = new OracleClient("orcl", "test", "test");
		// db = new MariaDB("localhost", 3316, "test", "test", "test");

		SQLKit kit = db.getSQLKit();

		EntityMaker
				.makeTable(kit, "STAF", "org.kernelab.dougong.test", new File("E:\\project\\dougong\\src"), "", null);
	}
}
