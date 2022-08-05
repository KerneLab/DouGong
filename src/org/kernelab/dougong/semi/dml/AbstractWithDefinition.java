package org.kernelab.dougong.semi.dml;

import java.util.LinkedHashMap;
import java.util.Map;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.semi.AbstractProvidable;

public class AbstractWithDefinition extends AbstractProvidable implements WithDefinition
{
	private String					name;

	private String[]				columns;

	private Withable				select;

	private Map<String, Integer>	colmap;

	public AbstractWithDefinition(String name, String... columns)
	{
		this.name = name;
		this.columns = columns == null || columns.length == 0 ? null : columns;
		if (this.columns != null)
		{
			this.colmap = new LinkedHashMap<String, Integer>();
			for (int i = 0; i < this.columns.length; i++)
			{
				this.colmap.put(this.columns[i], i);
			}
		}
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public WithDefinition as(Withable select)
	{
		this.select = select;
		this.select.with(this);
		return this;
	}

	@Override
	public String[] columns()
	{
		return columns;
	}

	@Override
	public Reference ref(String refer)
	{
		if (this.colmap == null)
		{
			return null;
		}

		if (this.colmap.get(refer) != null)
		{
			return this.provider().provideReference((View) this.select(), refer);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Withable select()
	{
		return select;
	}
}
