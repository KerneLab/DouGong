package org.kernelab.dougong.demo;

import java.util.Collection;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.OneToManyMeta;

@EntityMeta(entity = COMP.class)
public class Company
{
	@DataMeta(alias = "compId")
	private String					id;

	@DataMeta(alias = "compName")
	private String					name;

	@OneToManyMeta(model = Department.class, key = DEPT.FRN_DEPT, referred = false)
	private Collection<Department>	departments;

	// @OneToManyMeta(model = Staff.class, key = "FRN_STAF", referred = false)
	// @JoinMeta(joins = { //
	// @JoinDefine(entity = DEPT.class, key = "FRN_DEPT", referred = false) })
	// private Collection<Staff> staffs;

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

	// public Collection<Staff> getStaffs()
	// {
	// return staffs;
	// }

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

	// public void setStaffs(Collection<Staff> staffs)
	// {
	// this.staffs = staffs;
	// }

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

		// if (this.getStaffs() != null)
		// {
		// str += "\n[Staffs]";
		// for (Staff s : this.getStaffs())
		// {
		// str += "\n " + s.toString();
		// }
		// }

		return str;
	}
}
