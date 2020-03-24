package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;

@EntityMeta(entity = JOBS.class)
public class Job
{
	@DataMeta(alias = "jobId")
	private String	id;

	@DataMeta(alias = "jobName")
	private String	name;

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setId(String jobId)
	{
		this.id = jobId;
	}

	public void setName(String jobName)
	{
		this.name = jobName;
	}
}
