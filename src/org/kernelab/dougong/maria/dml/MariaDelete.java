package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractDelete;

public class MariaDelete extends AbstractDelete
{
	@Override
	public MariaDelete provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
