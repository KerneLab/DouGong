package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractVarinaryResult extends AbstractResult implements Providable
{
	private Provider		provider;

	private String			operator;

	private Expression[]	operands;

	public AbstractVarinaryResult(String operator, Expression... operands)
	{
		this.operator = operator;
		this.operands = operands;
	}

	public Expression[] operands()
	{
		return operands;
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
	public Providable provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		if (operands() != null && operands().length > 0)
		{
			boolean first = true;

			for (Expression expr : operands())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(operator());
				}
				Utils.outputExpr(buffer, expr);
			}
			return buffer;
		}
		else
		{
			return buffer.append(SQL.NULL);
		}
	}
}
