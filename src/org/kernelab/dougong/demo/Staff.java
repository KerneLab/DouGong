package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.ManyToOneMeta;

@EntityMeta(entity = STAF.class)
public class Staff
{
	@ManyToOneMeta
	private Department	department;

	@DataMeta(alias = "compId")
	private String		compId;

	@DataMeta(alias = "deptId")
	private String		deptId;

	@DataMeta(alias = "stafId")
	private String		id;

	@DataMeta(alias = "stafName")
	private String		name;

	@DataMeta(alias = "stafSalary")
	private Double		salary;

	public String getCompId()
	{
		return this.getDepartment().getCompId();
	}

	public Department getDepartment()
	{
		return department;
	}

	public String getDeptId()
	{
		return this.getDepartment().getId();
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Double getSalary()
	{
		return salary;
	}

	public void setCompId(String compId)
	{
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}

	public void setDeptId(String deptId)
	{
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setSalary(Double salary)
	{
		this.salary = salary;
	}

	public String toString()
	{
		return "Staff: id=" + this.getId() + ", name=" + this.getName() + ", salary=" + this.getSalary();
	}
}
