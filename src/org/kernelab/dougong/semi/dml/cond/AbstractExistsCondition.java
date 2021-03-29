package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.cond.ExistsCondition;

public abstract class AbstractExistsCondition extends AbstractNegatableCondition implements ExistsCondition
{
	protected Select select;

	public Expression $_1()
	{
		return select;
	}

	public AbstractExistsCondition exists(Select select)
	{
		this.select = select;
		return this;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.not)
		{
			buffer.append(" NOT");
		}
		buffer.append(" EXISTS (");
		this.select.toStringScoped(buffer);
		return buffer.append(')');
	}
}
