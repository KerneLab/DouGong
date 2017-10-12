package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractLikeCondition;

public class OracleLikeCondition extends AbstractLikeCondition
{
	private Expression escape = null;

	public Expression escape()
	{
		return escape;
	}

	public OracleLikeCondition escape(Expression escape)
	{
		this.escape = escape;
		return this;
	}

	@Override
	protected LogicalCondition provideLogicalCondition()
	{
		return new OracleLogicalCondition();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.expr.toStringExpress(buffer);
		if (this.not)
		{
			buffer.append(" NOT");
		}
		buffer.append(" LIKE ");
		this.pattern.toStringExpress(buffer);
		if (this.escape() != null)
		{
			buffer.append(" ESCAPE ");
			this.escape().toStringExpress(buffer);
		}
		return buffer;
	}
}
