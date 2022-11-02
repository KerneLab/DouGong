package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractExpression;

public abstract class OracleExpression extends AbstractExpression
{
	private OracleProvider provider;

	public OracleExpression(OracleProvider provider)
	{
		super();
		this.provider(provider);
	}

	protected OracleProvider provider()
	{
		return provider;
	}

	protected OracleExpression provider(OracleProvider provider)
	{
		this.provider = provider;
		return this;
	}
}
