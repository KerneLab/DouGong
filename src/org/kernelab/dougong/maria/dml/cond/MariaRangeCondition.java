package org.kernelab.dougong.maria.dml.cond;

import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractRangeCondition;

public class MariaRangeCondition extends AbstractRangeCondition
{
	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new MariaLogicalCondition();
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
		buffer.append(" AND ");
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
