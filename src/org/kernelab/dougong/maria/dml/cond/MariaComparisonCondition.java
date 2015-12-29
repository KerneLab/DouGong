package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractComparisonCondition;

public class MariaComparisonCondition extends AbstractComparisonCondition
{
	public StringBuilder toString(StringBuilder buffer)
	{
		this.leftExpr.toString(buffer);
		buffer.append(this.compType);
		if (this.groupQual != null)
		{
			buffer.append(' ');
			buffer.append(this.groupQual);
			buffer.append(' ');
		}
		this.rightExpr.toString(buffer);
		return buffer;
	}

	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new MariaLogicalCondition();
	}
}
