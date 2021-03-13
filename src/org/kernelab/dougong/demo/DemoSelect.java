package org.kernelab.dougong.demo;

import java.sql.SQLException;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.orcl.OracleProvider;

public class DemoSelect
{
	public final static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		try
		{
			demoSelectByClass();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static Select makeSelectByKey()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(d = SQL.table(DEPT.class, "d")) //
				.innerJoin(s = SQL.table(STAF.class, "s"), s.FK_STAF(d)) //
				.select(d.COMP_ID.as("comp"), //
						s.DEPT_ID.as("dept"), //
						s.STAF_ID.as("staf"), //
						s.STAF_NAME.as("stafName"), //
						s.STAF_SALARY.as("salary") //
				) //
				.orderBy(s.STAF_ID);
	}

	public static void demoSelectByClass() throws SQLException
	{
		// Company company = Entitys.selectObject(Config.getSQLKit(),
		// Config.SQL, Company.class,
		// new JSON().attr("compId", "1"));
		//
		// System.out.println(company);

		Staff staff = Entitys.selectObject(Config.getSQLKit(), Config.SQL, Staff.class,
				new JSON().attr("compId", "1").attr("deptId", "12").attr("stafId", "a21"));

		System.out.println(staff);
	}
}
