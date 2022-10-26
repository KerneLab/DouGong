package org.kernelab.dougong.semi.dml.cond;

import java.util.List;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.cond.ExistsCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractExistsCondition extends AbstractNegatableCondition implements ExistsCondition
{
	protected Select select;

	@Override
	public AbstractExistsCondition exists(Select select)
	{
		this.select = select;
		return this;
	}

	@Override
	public Expression operand(int pos)
	{
		return pos == 0 ? this.select : null;
	}

	@Override
	public AbstractExistsCondition operand(int pos, Expression opr)
	{
		if (pos == 0)
		{
			this.select = (Select) opr;
		}
		return this;
	}

	@Override
	public List<Expression> operands()
	{
		return Utils.arrayList((Expression) this.select);
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.not)
		{
			buffer.append(" " + LogicalCondition.NOT);
		}
		buffer.append(" EXISTS (");
		this.select.toStringScoped(buffer);
		return buffer.append(')');
	}
}
