package org.kernelab.dougong.core.meta;

import java.util.Map;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;

public class Queryable
{
	private SQL					sql;

	private Select				select;

	private Map<String, Object>	params;

	public Queryable(Select select, Map<String, Object> params)
	{
		this.setSelect(select);
		this.setParams(params);
	}

	public Queryable(SQL sql)
	{
		this.setSql(sql);
	}

	public Map<String, Object> getParams()
	{
		return params;
	}

	public Select getSelect()
	{
		return select;
	}

	public SQL getSql()
	{
		return sql;
	}

	public Queryable setParams(Map<String, Object> params)
	{
		this.params = params;
		return this;
	}

	public Queryable setSelect(Select select)
	{
		this.select = select;
		return this;
	}

	public Queryable setSql(SQL sql)
	{
		this.sql = sql;
		return this;
	}
}
