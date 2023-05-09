package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.util.Utils;

public class AbstractBinaryResult extends AbstractResult implements Providable
{
	private Provider	provider;

	private String		operator;

	private Expression	operand1;

	private Expression	operand2;

	public AbstractBinaryResult(String operator, Expression operand1, Expression operand2)
	{
		this.operator = operator;
		this.operand1 = operand1;
		this.operand2 = operand2;
	}

	@Override
	protected AbstractBinaryResult newInstance()
	{
		return this.provider().provideProvider(new AbstractBinaryResult(operator(), operand1(), operand2()));
	}

	public Expression operand1()
	{
		return operand1;
	}

	public Expression operand2()
	{
		return operand2;
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
	public AbstractBinaryResult provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append('(');
		Utils.outputExpr(buffer, operand1());
		buffer.append(operator());
		Utils.outputExpr(buffer, operand2());
		buffer.append(')');
		return buffer;
	}
}
