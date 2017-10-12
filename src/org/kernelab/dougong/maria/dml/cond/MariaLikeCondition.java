package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractLikeCondition;

public class MariaLikeCondition extends AbstractLikeCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new MariaLogicalCondition();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.expr.toStringExpress(buffer);
		if (this.not)
		{
			buffer.append(" NOT");
		}
		buffer.append(" LIKE ");
		return this.pattern.toStringExpress(buffer);
	}
}
