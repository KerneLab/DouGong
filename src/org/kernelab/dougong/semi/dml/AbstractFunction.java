package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public class AbstractFunction extends AbstractSingleItem implements Function
{
	private Provider		provider;

	private Expression[]	arguments;

	private boolean			order		= true;

	private boolean			usingSchema	= false;

	public Expression[] args()
	{
		return arguments;
	}

	public AbstractFunction ascend()
	{
		return ascend(true);
	}

	public AbstractFunction ascend(boolean ascend)
	{
		this.order = ascend;
		return this;
	}

	public boolean ascending()
	{
		return order;
	}

	public AbstractFunction call(Expression... arguments)
	{
		this.arguments = arguments;
		return this;
	}

	public AbstractFunction descend()
	{
		return ascend(false);
	}

	@Override
	protected ComparisonCondition provideComparisonCondition()
	{
		return provider().provideComparisonCondition();
	}

	@Override
	protected LikeCondition provideLikeCondition()
	{
		return provider().provideLikeCondition();
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return provider().provideMembershipCondition();
	}

	@Override
	protected NullCondition provideNullCondition()
	{
		return provider().provideNullCondition();
	}

	public Provider provider()
	{
		return provider;
	}

	public AbstractFunction provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@Override
	protected RangeCondition provideRangeCondition()
	{
		return provider().provideRangeCondition();
	}

	@Override
	protected AbstractFunction replicate()
	{
		return new AbstractFunction().provider(this.provider()).call(this.args()) //
				.ascend(this.ascending()).usingSchema(this.usingSchema());
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(provider().provideFunctionText(this));
		return buffer;
	}

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		this.toString(buffer);
		this.provider().provideOutputAlias(buffer, this);
		return buffer;
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		this.toString(buffer);
		this.provider().provideOutputOrder(buffer, this);
		return buffer;
	}

	public boolean usingSchema()
	{
		return usingSchema;
	}

	public AbstractFunction usingSchema(boolean using)
	{
		this.usingSchema = using;
		return this;
	}
}
