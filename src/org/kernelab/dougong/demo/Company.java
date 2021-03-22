package org.kernelab.dougong.demo;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.OneToManyMeta;

@EntityMeta(entity = COMP.class)
public class Company
{
	@DataMeta(alias = "rowid")
	private String				rowid;

	@DataMeta(alias = "compId")
	private String				id;

	@DataMeta(alias = "compName")
	private String				name;

	@OneToManyMeta(model = Department.class, key = DEPT.FK_DEPT, referred = false)
	private List<Department>	departments	= new LinkedList<Department>();

	public Company()
	{
	}

	public Company(String id, String name)
	{
		this.setId(id);
		this.setName(name);
	}

	public List<Department> getDepartments()
	{
		return departments;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getRowid()
	{
		return rowid;
	}

	public void setDepartments(List<Department> departments)
	{
		this.departments = departments;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setRowid(String rowid)
	{
		this.rowid = rowid;
	}

	@Override
	public String toString()
	{
		String str = "Company: rowid=" + this.getRowid() + " id=" + this.getId() + ", name=" + this.getName();

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
