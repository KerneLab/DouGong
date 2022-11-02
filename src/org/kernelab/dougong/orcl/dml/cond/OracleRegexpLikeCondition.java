package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractRegexpLikeCondition;

public class OracleRegexpLikeCondition extends AbstractRegexpLikeCondition
{
	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.not)
		{
			buffer.append(" " + LogicalCondition.NOT);
		}
		buffer.append(" REGEXP_LIKE(");
		this.expr.toStringExpress(buffer);
		buffer.append(',');
		this.pattern.toStringExpress(buffer);
		buffer.append(')');
		return buffer;
	}
}
