package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractRangeCondition;

public class MariaRangeCondition extends AbstractRangeCondition
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
		buffer.append(" BETWEEN ");
		this.from.toString(buffer);
		buffer.append(" TO ");
		return this.to.toString(buffer);
	}
}
