package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractInsert;

public class MariaInsert extends AbstractInsert
{
	@Override
	public MariaInsert provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
