package org.kernelab.dougong.semi.dml.opr;

import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.dml.AbstractSortable;

public abstract class AbstractResult extends AbstractSortable implements Result
{
	@Override
	protected abstract AbstractResult replicate();

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), this.toStringExpress(buffer), this);
	}
}
