package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractMembershipCondition;

public class MariaMembershipCondition extends AbstractMembershipCondition
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
		buffer.append(" IN (");
		this.scope.toString(buffer);
		return buffer.append(')');
	}
}
