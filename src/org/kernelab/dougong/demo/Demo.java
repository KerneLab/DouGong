package org.kernelab.dougong.demo;

import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.meta.DataReflector;
import org.kernelab.dougong.core.meta.Entitys;

public class Demo
{
	public static void main(String[] args)
	{
		try
		{
			Company company = Entitys.selectObject(Config.getSQLKit(), Config.SQL, Company.class,
					new JSON().attr("compId", "1"));

			Tools.debug(company.toString());

			Map<Class<?>, Object> reflects = DataReflector.register(Company.class, Department.class, Staff.class);

			Tools.debug(JSON.Reflect(reflects, company));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
