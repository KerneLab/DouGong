package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.util.Utils;

public class AbstractUnaryResult extends AbstractResult implements Providable
{
	private Provider	provider;

	private String		operator;

	private Expression	operand;

	public AbstractUnaryResult(String operator, Expression operand)
	{
		this.operator = operator;
		this.operand = operand;
	}

	@Override
	protected AbstractUnaryResult newInstance()
	{
		return provider().provideProvider(new AbstractUnaryResult(operator(), operand()));
	}

	public Expression operand()
	{
		return operand;
	}

	public String operator()
	{
		return operator;
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public AbstractUnaryResult provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(operator());
		Utils.outputExpr(buffer, operand());
		return buffer;
	}
}
