package org.kernelab.dougong.core.dml.opr;

import org.kernelab.dougong.core.Expression;

public interface JointOperable
{
	public Result joint(Expression... operands);
}
