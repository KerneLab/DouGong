package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractComparisonCondition;

public class OracleComparisonCondition extends AbstractComparisonCondition
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
		return new OracleLogicalCondition();
	}
}
