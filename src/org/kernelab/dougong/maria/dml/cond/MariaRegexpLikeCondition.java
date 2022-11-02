package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractRegexpLikeCondition;

public class MariaRegexpLikeCondition extends AbstractRegexpLikeCondition
{
	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.expr.toStringExpress(buffer);
		if (this.not)
		{
			buffer.append(" " + LogicalCondition.NOT);
		}
		buffer.append(" RLIKE ");
		this.pattern.toStringExpress(buffer);
		return buffer;
	}
}
