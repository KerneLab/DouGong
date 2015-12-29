package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractRangeCondition;

public class OracleRangeCondition extends AbstractRangeCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.expr.toString(buffer);
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
