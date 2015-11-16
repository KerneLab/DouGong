package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractUpdate;

public class OracleUpdate extends AbstractUpdate
{
	@Override
	public OracleUpdate provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
