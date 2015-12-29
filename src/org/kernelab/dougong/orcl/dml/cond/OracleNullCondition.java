package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractNullCondition;

public class OracleNullCondition extends AbstractNullCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.expr.toString(buffer);
		buffer.append(" IS");
		if (this.not)
		{
			buffer.append(" NOT");
		}
		return buffer.append(" NULL");
	}
}
