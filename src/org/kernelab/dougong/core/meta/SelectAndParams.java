package org.kernelab.dougong.core.meta;

import java.util.Map;

import org.kernelab.dougong.core.dml.Select;

public class SelectAndParams
{
	private Select				select;

	private Map<String, Object>	params;

	public SelectAndParams()
	{
		super();
	}

	public SelectAndParams(Select select, Map<String, Object> params)
	{
		this();
		this.setSelect(select);
		this.setParams(params);
	}

	public Map<String, Object> getParams()
	{
		return params;
	}

	public Select getSelect()
	{
		return select;
	}

	public void setParams(Map<String, Object> params)
	{
		this.params = params;
	}

	public void setSelect(Select select)
	{
		this.select = select;
	}
}
