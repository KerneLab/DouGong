package org.kernelab.dougong.core.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Map;

import org.kernelab.basis.Mapper;

public class ResultSetMapper<T> implements Mapper<ResultSet, T>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4833838041224384666L;

	private Class<T>			cls;

	private Map<Field, String>	map;

	public Class<T> getCls()
	{
		return cls;
	}

	public Map<Field, String> getMap()
	{
		return map;
	}

	public T map(ResultSet rs)
	{
		try
		{
			return Utils.mapResultSetToObject(rs, cls.newInstance(), map);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public ResultSetMapper<T> setCls(Class<T> cls)
	{
		this.cls = cls;
		return this;
	}

	public ResultSetMapper<T> setMap(Map<Field, String> map)
	{
		this.map = map;
		return this;
	}
}
