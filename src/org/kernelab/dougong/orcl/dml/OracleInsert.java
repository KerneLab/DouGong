package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AdvancedInsert;

public class OracleInsert extends AdvancedInsert
{
	@Override
	public OracleInsert provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	protected StringBuilder textOfReturningClause(StringBuilder buffer)
	{
		if (values() != null)
		{
			this.provider().to(OracleProvider.class).provideOutputReturningClause(buffer, returning());
		}
		return buffer;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		super.toString(buffer);
		this.textOfReturningClause(buffer);
		return buffer;
	}
}
