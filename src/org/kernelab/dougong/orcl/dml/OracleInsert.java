package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractInsert;

public class OracleInsert extends AbstractInsert
{
	@Override
	public OracleInsert provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
