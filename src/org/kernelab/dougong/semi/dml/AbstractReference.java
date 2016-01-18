package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Reference;

public abstract class AbstractReference extends AbstractColumn implements Reference
{
	private Expression	refer;

	public AbstractReference(View view, Expression refer)
	{
		super(view, null);
		this.refer(refer);
	}

	@Override
	public AbstractReference as(String alias)
	{
		return (AbstractReference) super.as(alias);
	}

	@Override
	public String name()
	{
		if (super.name() == null)
		{
			super.name(view().provider().provideReferString(this.refer()));
		}
		return super.name();
	}

	@Override
	public AbstractColumn name(String name)
	{
		return this;
	}

	public Expression refer()
	{
		return refer;
	}

	protected AbstractReference refer(Expression refer)
	{
		this.refer = refer;
		return this;
	}

	protected abstract AbstractReference replicate();

	@Override
	public AbstractReference usingByJoin(boolean using)
	{
		return (AbstractReference) super.usingByJoin(using);
	}
}
