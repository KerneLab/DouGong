package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractMembershipCondition;

public class OracleMembershipCondition extends AbstractMembershipCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.expr instanceof Items)
		{
			buffer.append('(');
		}
		this.expr.toString(buffer);
		if (this.expr instanceof Items)
		{
			buffer.append(')');
		}
		if (this.not)
		{
			buffer.append(" NOT");
		}
		buffer.append(" IN (");
		this.scope.toStringScoped(buffer);
		return buffer.append(')');
	}
}
