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

	public static void demo1(SQLKit kit, SQL sql) throws SQLException
	{
		Job j = new Job("j4", "boss");

		Entitys.saveObject(kit, sql, j);

		Tools.debug(j.toString());
	}

	public static void demo2(SQLKit kit, SQL sql) throws SQLException
	{
		Job clerk = Entitys.selectObject(kit, sql, Job.class, new JSON().attr("jobId", "J1"));
		Job manager = Entitys.selectObject(kit, sql, Job.class, new JSON().attr("jobId", "J2"));

		Company comp = new Company("3", "CCC");

		Department dept1 = new Department(comp, "d1", "Dept 1");
		comp.getDepartments().add(dept1);
		dept1.getStaffs().add(new Staff(dept1, "mike", 1235.3, clerk));
		dept1.getStaffs().add(new Staff(dept1, "john", 3113.3, manager));
		dept1.getStaffs().add(new Staff(dept1, "rose", 2544.3, clerk));

		Department dept2 = new Department(comp, "d2", "Dept 2");
		comp.getDepartments().add(dept2);
		dept2.getStaffs().add(new Staff(dept2, "peter", 1748.0, clerk));
		dept2.getStaffs().add(new Staff(dept2, "helen", 3114.2, manager));

		Entitys.saveObject(kit, sql, comp);
	}

	public static void demo3(SQLKit kit, SQL sql) throws SQLException
	{
		Company comp = Entitys.selectObject(kit, sql, Company.class, new JSON().attr("compId", "3"));

		comp.getDepartments().get(0).getStaffs().remove(0);
		comp.getDepartments().get(0).getStaffs().get(0).setName("jack");
		comp.getDepartments().get(0).getStaffs().add(new Staff(comp.getDepartments().get(0), "tony", 2132.0, null));
		Tools.debug(comp.toString());

		Tools.debug("=================");

		Entitys.saveObject(kit, sql, comp);
		Tools.debug(comp.toString());
	}

	public static void main(String[] args)
	{
		try
		{
			SQLKit kit = Config.getSQLKit();
			SQL sql = Config.SQL;

			demo3(kit, sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
