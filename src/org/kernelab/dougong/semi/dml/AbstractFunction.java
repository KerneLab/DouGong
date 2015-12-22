package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public class AbstractFunction extends AbstractSingleItem implements Function
{
	private Provider		provider;

	private String			name;

	private Expression[]	arguments;

	private boolean			order	= true;

	private String			schema	= null;

	public Expression[] args()
	{
		return arguments;
	}

	@Override
	public AbstractFunction as(String alias)
	{
		return this.replicate() //
				.alias(alias);
	}

	@Override
	public AbstractFunction alias(String alias)
	{
		return (AbstractFunction) super.alias(alias);
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

	protected void initFunction()
	{
		this.schema(Utils.getSchemaFromMember(this));
		this.name(Utils.getNameFromNamed(this));
	}

	public String name()
	{
		return name;
	}

	protected AbstractFunction name(String name)
	{
		this.name = name;
		return this;
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
		this.initFunction();
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
		return new AbstractFunction() //
				.schema(this.schema()) //
				.name(this.name()) //
				.call(this.args()) //
				.ascend(this.ascending()) //
				.provider(this.provider());
	}

	public String schema()
	{
		return schema;
	}

	protected AbstractFunction schema(String schema)
	{
		this.schema = schema;
		return this;
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
}
