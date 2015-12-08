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
		this.item.toString(buffer);
		if (this.not)
		{
			buffer.append(" NOT");
		}
		return buffer.append(" LIKE ").append(this.pattern);
	}
}
