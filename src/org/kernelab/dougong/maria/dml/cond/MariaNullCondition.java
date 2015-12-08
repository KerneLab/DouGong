package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractNullCondition;

public class MariaNullCondition extends AbstractNullCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new MariaLogicalCondition();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.item.toString(buffer);
		buffer.append(" IS");
		if (this.not)
		{
			buffer.append(" NOT");
		}
		return buffer.append(" NULL");
	}
}
