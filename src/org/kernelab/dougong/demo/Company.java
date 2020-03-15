package org.kernelab.dougong.demo;

import java.util.Collection;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.JoinDefine;
import org.kernelab.dougong.core.meta.JoinMeta;
import org.kernelab.dougong.core.meta.OneToManyMeta;

@EntityMeta(entity = COMP.class)
public class Company
{
	@DataMeta(alias = "compId")
	private String					id;

	@DataMeta(alias = "compName")
	private String					name;

	@OneToManyMeta(model = Department.class, foreignKey = "FRN_DEPT")
	private Collection<Department>	departments;

	@OneToManyMeta(model = Staff.class, foreignKey = "")
	@JoinMeta(joins = { //
			@JoinDefine(entity = DEPT.class, key = "FRN_DEPT", referred = false),
			@JoinDefine(entity = STAF.class, key = "FRN_STAF", referred = false) })
	private Collection<Staff>		staffs;

	public Collection<Department> getDepartments()
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

	public void setDepartments(Collection<Department> departments)
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

	public String toString()
	{
		String str = "Company: id=" + this.getId() + ", name=" + this.getName();

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
