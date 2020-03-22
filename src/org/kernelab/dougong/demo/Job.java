package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;

@EntityMeta(entity = JOBS.class)
public class Job
{
	@DataMeta(alias = "jobId")
	private String	jobId;

	@DataMeta(alias = "jobName")
	private String	jobName;

	public String getJobId()
	{
		return jobId;
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}
}
