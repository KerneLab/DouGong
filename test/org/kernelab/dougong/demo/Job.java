package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;

@EntityMeta(entity = JOBS.class)
public class Job
{
	@DataMeta(alias = "rowid")
	private String	rowid;

	@DataMeta(alias = "jobId")
	private String	id;

	@DataMeta(alias = "jobName")
	private String	name;

	public Job()
	{
	}

	public Job(String id, String name)
	{
		this.setId(id);
		this.setName(name);
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

	public void setId(String jobId)
	{
		this.id = jobId;
	}

	public void setName(String jobName)
	{
		this.name = jobName;
	}

	public void setRowid(String rowid)
	{
		this.rowid = rowid;
	}

	@Override
	public String toString()
	{
		return "Job: id=" + this.getId() + ", name=" + this.getName();
	}
}
