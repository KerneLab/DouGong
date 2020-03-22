package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.meta.MemberMeta;
import org.kernelab.dougong.core.meta.NameMeta;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.semi.AbstractTable;

@MemberMeta()
public class JOBS extends AbstractTable
{
	@NameMeta(name = "JOB_ID")
	@DataMeta(alias = "jobId")
	@PrimaryKeyMeta(ordinal = 1)
	public Column	JOB_ID;

	@NameMeta(name = "JOB_NAME")
	@DataMeta(alias = "jobName")
	public Column	JOB_NAME;
}
