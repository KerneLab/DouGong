package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractInsert;

public class OracleInsert extends AbstractInsert
{
	private Expression[] returning;

	@Override
	public OracleInsert provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	protected Expression[] returning()
	{
		return returning;
	}

	public OracleInsert returning(Expression... exprs)
	{
		this.returning = exprs != null && exprs.length > 0 ? exprs : null;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		super.toString(buffer);

		if (values() != null)
		{
			this.provider().to(OracleProvider.class).provideOutputReturningClause(buffer, returning());
		}

		return buffer;
	}
}
