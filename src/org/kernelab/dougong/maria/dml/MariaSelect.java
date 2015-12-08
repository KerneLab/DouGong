package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class MariaSelect extends AbstractSelect
{
	@Override
	public MariaSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
