package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.semi.dml.cond.AbstractComparisonCondition;

public class MariaComparisonCondition extends AbstractComparisonCondition
{
	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.leftExpr instanceof Items)
		{
			buffer.append('(');
		}
		this.leftExpr.toStringExpress(buffer);
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
		this.rightExpr.toStringExpress(buffer);
		if (this.rightExpr instanceof Items)
		{
			buffer.append(')');
		}
		return buffer;
	}
}
