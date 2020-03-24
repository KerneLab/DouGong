package org.kernelab.dougong.demo;

import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.meta.DataProjector;
import org.kernelab.dougong.core.meta.DataReflector;
import org.kernelab.dougong.core.meta.Entitys;

public class Demo
{
	public static void main(String[] args)
	{
		try
		{
			Class<?>[] classes = new Class<?>[] { Company.class, Department.class, Staff.class };

			Company company = Entitys.selectObject(Config.getSQLKit(), Config.SQL, Company.class,
					new JSON().attr("compId", "1"));
			Tools.debug(company.toString());

			company.setId("15");
			company.setName("abc inc.");
			Map<Class<?>, Object> reflects = DataReflector.register(classes);
			String text = JSON.Reflect(reflects, company).toString();
			Tools.debug(text);

			JSON json = JSON.Parse(text);
			Map<Class<?>, Object> projects = DataProjector.register(classes);
			Company company1 = json.projects(projects).project(new Company());
			Tools.debug(company1.toString());

			Entitys.saveObject(Config.getSQLKit(), Config.SQL, company1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
