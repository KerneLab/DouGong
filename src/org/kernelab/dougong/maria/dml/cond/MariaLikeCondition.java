package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.Items;
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
		if (this.expr instanceof Items)
		{
			buffer.append('(');
		}
		this.expr.toStringExpressed(buffer);
		if (this.expr instanceof Items)
		{
			buffer.append(')');
		}
		if (this.not)
		{
			buffer.append(" NOT");
		}
		return buffer.append(" LIKE ").append(this.pattern);
	}
}
