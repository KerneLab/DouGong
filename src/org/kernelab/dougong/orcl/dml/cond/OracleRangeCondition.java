package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.Items;
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
		if (this.expr instanceof Items)
		{
			buffer.append('(');
		}
		this.expr.toStringExpress(buffer);
		if (this.expr instanceof Items)
		{
			buffer.append(')');
		}
		if (this.not)
		{
			buffer.append(" NOT");
		}
		buffer.append(" BETWEEN ");
		if (this.from instanceof Items)
		{
			buffer.append('(');
		}
		this.from.toStringExpress(buffer);
		if (this.from instanceof Items)
		{
			buffer.append(')');
		}
		buffer.append(" TO ");
		if (this.to instanceof Items)
		{
			buffer.append('(');
		}
		this.to.toStringExpress(buffer);
		if (this.to instanceof Items)
		{
			buffer.append(')');
		}
		return buffer;
	}
}
