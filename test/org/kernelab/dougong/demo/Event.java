package org.kernelab.dougong.demo;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;

@EntityMeta(entity = EVNT.class)
public class Event
{
	@DataMeta(alias = "id")
	private String	id;

	@DataMeta(alias = "name")
	private String	name;

	@DataMeta(alias = "nextId")
	private String	nextId;

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getNextId()
	{
		return nextId;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setNextId(String nextId)
	{
		this.nextId = nextId;
	}
}
