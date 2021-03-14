package org.kernelab.dougong.demo;

import java.sql.SQLException;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.orcl.OracleProvider;

public class DemoSave
{
	public final static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		try
		{
			demoSave();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void demoSave() throws SQLException
	{
		SQLKit kit = Config.getSQLKit();
		SQL sql = Config.SQL;

		Department dept = Entitys.selectObject(kit, sql, Department.class,
				new JSON().attr("compId", "1").attr("deptId", "12"));

		dept.getStaffs().remove(0);

		Tools.debug(dept);

		Entitys.saveObjectCascade(kit, sql, dept, null);
	}
}
