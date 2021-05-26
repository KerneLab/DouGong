package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.semi.AbstractColumn;

public abstract class AbstractReference extends AbstractColumn implements Reference
{
	public AbstractReference(View view, String name)
	{
		super(view, name, null);
	}

	@Override
	public AbstractReference as(String alias)
	{
		return (AbstractReference) super.as(alias);
	}

	@Override
	public AbstractColumn name(String name)
	{
		return this;
	}

	protected abstract AbstractReference replicate();

	@Override
	public AbstractReference usingByJoin(boolean using)
	{
		return (AbstractReference) super.usingByJoin(using);
	}
}
