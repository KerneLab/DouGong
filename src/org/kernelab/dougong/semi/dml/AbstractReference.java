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
	public boolean isUsingByJoin()
	{
		return this.view().isJoinUsing(this.name());
	}

	@Override
	public AbstractColumn name(String name)
	{
		return this;
	}

	@Override
	protected abstract AbstractReference replicate();
}
