package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractComparisonCondition;

public class MariaComparisonCondition extends AbstractComparisonCondition
{
	public StringBuilder toString(StringBuilder buffer)
	{
		this.leftItem.toString(buffer);
		buffer.append(this.compType);
		this.rightItem.toString(buffer);
		return buffer;
	}

	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new MariaLogicalCondition();
	}
}
