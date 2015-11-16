package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractDelete;

public class OracleDelete extends AbstractDelete
{
	@Override
	public OracleDelete provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}
}
