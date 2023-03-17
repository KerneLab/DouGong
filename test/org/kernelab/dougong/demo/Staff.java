package org.kernelab.dougong.demo;

import java.util.Collection;
import java.util.LinkedList;

import org.kernelab.basis.Canal;
import org.kernelab.basis.Mapper;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.JoinDefine;
import org.kernelab.dougong.core.meta.JoinMeta;
import org.kernelab.dougong.core.meta.ManyToOneMeta;
import org.kernelab.dougong.core.meta.OneToManyMeta;

@EntityMeta(entity = STAF.class)
public class Staff
{
	@ManyToOneMeta(model = Department.class, key = STAF.FK_STAF)
	private Department		department;

	@DataMeta(alias = "rowid")
	private String			rowid;

	@DataMeta(alias = "compId", serialize = false)
	private String			compId;

	@DataMeta(alias = "deptId", serialize = false)
	private String			deptId;

	@DataMeta(alias = "stafId")
	private String			id;

	@DataMeta(alias = "stafName")
	private String			name;

	@DataMeta(alias = "stafSalary")
	private Double			salary;

	@DataMeta(alias = "stafRole")
	private String			roleId;

	@OneToManyMeta(model = Job.class, key = ROLE_JOB.FK_ROLE_JOB_JOB, referred = true)
	@JoinMeta({ @JoinDefine(entity = ROLE_LIST.class, key = STAF.FK_STAF_ROLE, referred = true),
			@JoinDefine(entity = ROLE_JOB.class, key = ROLE_JOB.FK_ROLE_JOB_ROLE, referred = false) })
	private Collection<Job>	jobs	= new LinkedList<Job>();

	public Staff()
	{
	}

	public Staff(Department dept, String name, Double salary, Job job)
	{
		this.setDepartment(dept);
		this.setName(name);
		this.setSalary(salary);
	}

	public String getCompId()
	{
		return this.getDepartment() != null ? this.getDepartment().getCompId() : compId;
	}

	public Department getDepartment()
	{
		return department;
	}

	public String getDeptId()
	{
		return this.getDepartment() != null ? this.getDepartment().getId() : deptId;
	}

	public String getId()
	{
		return id;
	}

	public Collection<Job> getJobs()
	{
		return jobs;
	}

	public String getName()
	{
		return name;
	}

	public String getRoleId()
	{
		return roleId;
	}

	public String getRowid()
	{
		return rowid;
	}

	public Double getSalary()
	{
		return salary;
	}

	public void setCompId(String compId)
	{
		this.compId = compId;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}

	public void setDeptId(String deptId)
	{
		this.deptId = deptId;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setJobs(Collection<Job> jobs)
	{
		this.jobs = jobs;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}

	public void setRowid(String rowid)
	{
		this.rowid = rowid;
	}

	public void setSalary(Double salary)
	{
		this.salary = salary;
	}

	@Override
	public String toString()
	{
		return "Staff: rowid=" + this.getRowid() + " id=" + this.getId() + ", name=" + this.getName() + ", salary="
				+ this.getSalary() + ", jobs=" + Canal.of(this.getJobs()).map(new Mapper<Job, String>()
				{
					@Override
					public String map(Job el) throws Exception
					{
						return el.getName();
					}
				}).toString(",", "[", "]");
	}
}
