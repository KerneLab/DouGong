package org.kernelab.dougong.semi;

import org.kernelab.dougong.core.Castable;

public class AbstractCastable implements Castable
{
	public <T> T as(Class<T> cls)
	{
		try
		{
			return cls.cast(this);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
