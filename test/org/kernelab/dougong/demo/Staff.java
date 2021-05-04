package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.ManyToOneMeta;
import org.kernelab.dougong.core.meta.OneToOneMeta;

@EntityMeta(entity = STAF.class)
public class Staff
{
	@ManyToOneMeta(model = Department.class, key = STAF.FK_STAF)
	private Department	department;

	@DataMeta(alias = "rowid")
	private String		rowid;

	@DataMeta(alias = "compId", serialize = false)
	private String		compId;

	@DataMeta(alias = "deptId", serialize = false)
	private String		deptId;

	@DataMeta(alias = "stafId")
	private String		id;

	@DataMeta(alias = "stafName")
	private String		name;

	@DataMeta(alias = "stafSalary")
	private Double		salary;

	@DataMeta(alias = "stafJob")
	private String		jobId;

	@OneToOneMeta(key = STAF.FK_STAF_JOB, model = Job.class, referred = true, serialize = true)
	private Job			job;

	public Staff()
	{
	}

	public Staff(Department dept, String name, Double salary, Job job)
	{
		this.setDepartment(dept);
		this.setName(name);
		this.setSalary(salary);
		this.setJob(job);
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

	public Job getJob()
	{
		return job;
	}

	public String getJobId()
	{
		return this.getJob() != null ? this.getJob().getId() : jobId;
	}

	public String getName()
	{
		return name;
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

	public void setJob(Job job)
	{
		this.job = job;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public void setName(String name)
	{
		this.name = name;
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
				+ this.getSalary() + " job=" + this.getJob();
	}
}
