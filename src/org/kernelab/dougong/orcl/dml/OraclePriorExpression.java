package org.kernelab.dougong.orcl.dml;

import java.util.List;

import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.orcl.OracleProvider;

public class OraclePriorExpression extends OracleExpression
{
	public static final String	PRIOR	= "PRIOR";

	private Expression			expression;

	public OraclePriorExpression(OracleProvider provider, Expression expr)
	{
		super(provider);
		this.expression(expr);
	}

	protected Expression expression()
	{
		return expression;
	}

	protected OraclePriorExpression expression(Expression expr)
	{
		this.expression = expr;
		return this;
	}

	@Override
	protected OraclePriorExpression replicate()
	{
		return new OraclePriorExpression(this.provider(), this.expression());
	}

	public List<Item> resolveItems()
	{ // Prior should not be the select item.
		return null;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(PRIOR);
		buffer.append(' ');
		return Utils.outputExpr(buffer, this.expression());
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return toString(buffer);
	}
}
