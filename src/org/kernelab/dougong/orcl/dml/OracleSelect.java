package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class OracleSelect extends AbstractSelect
{
	@Override
	public OracleSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
