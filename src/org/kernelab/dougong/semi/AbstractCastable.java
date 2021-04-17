package org.kernelab.dougong.semi;

import org.kernelab.basis.Castable;

public class AbstractCastable implements Castable
{
	public <T> T to(Class<T> cls)
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
