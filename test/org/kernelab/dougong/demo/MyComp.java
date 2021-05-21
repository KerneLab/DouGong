package org.kernelab.dougong.demo;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.core.meta.OneToManyMeta;

@EntityMeta(entity = COMP.class)
public class MyComp
{
	public static void main(String[] args)
	{
		try
		{
			MyComp c = Entitys.selectObject(Config.getSQLKit(), Config.SQL, MyComp.class,
					new JSON().attr("compId", "1"));

			// Tools.debug(Entitys.setupObject(Config.getSQLKit(), Config.SQL,
			// c, true).toString());

			Tools.debug(c.toString());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	@DataMeta(alias = "compId")
	private String				id;

	@OneToManyMeta(model = Department.class, key = DEPT.FK_DEPT)
	private List<Department>	departments	= new LinkedList<Department>();

	public List<Department> getDepartments()
	{
		return departments;
	}

	public String getId()
	{
		return id;
	}

	public void setDepartments(List<Department> departments)
	{
		this.departments = departments;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		String str = "Company: id=" + this.getId();

		if (this.getDepartments() != null)
		{
			str += "\n[Departments]";
			for (Department d : this.getDepartments())
			{
				str += "\n  " + d.toString();
			}
		}

		return str;
	}
}
