package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractComparisonCondition;

public class OracleComparisonCondition extends AbstractComparisonCondition
{
	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.leftExpr instanceof Items)
		{
			buffer.append('(');
		}
		this.leftExpr.toStringExpressed(buffer);
		if (this.leftExpr instanceof Items)
		{
			buffer.append(')');
		}
		buffer.append(this.compType);
		if (this.groupQual != null)
		{
			buffer.append(' ');
			buffer.append(this.groupQual);
			buffer.append(' ');
		}
		if (this.rightExpr instanceof Items)
		{
			buffer.append('(');
		}
		this.rightExpr.toStringExpressed(buffer);
		if (this.rightExpr instanceof Items)
		{
			buffer.append(')');
		}
		return buffer;
	}

	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}
}
