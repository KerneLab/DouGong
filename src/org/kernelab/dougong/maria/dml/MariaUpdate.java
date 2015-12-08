package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractUpdate;

public class MariaUpdate extends AbstractUpdate
{
	@Override
	public MariaUpdate provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
