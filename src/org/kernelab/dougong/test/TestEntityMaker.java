package org.kernelab.dougong.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.util.EntityMaker;
import org.kernelab.dougong.demo.Config;

public class TestEntityMaker
{
	public static void main(String[] args) throws SQLException, FileNotFoundException
	{
		Provider provider = Config.ORACLE_PROVIDER;
		SQL sql = new SQL(provider);
		SQLKit kit = Config.getSQLKit();

		File base = new File("E:\\project\\dougong\\src");

		EntityMaker.makeTable(provider, kit, "STAF", "org.kernelab.dougong.demo", base, null, null);

		EntityMaker.makeView(provider, kit, sql.subquery(TestView.class), "org.kernelab.dougong.demo", base, "", null);
	}
}
