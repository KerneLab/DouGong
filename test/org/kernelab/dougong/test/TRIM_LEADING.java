package org.kernelab.dougong.test;

import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractFunction;

public class TRIM_LEADING extends AbstractFunction
{
	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append("TRIM(LEADING ");
		Utils.outputExpr(buffer, this.args()[1]);
		buffer.append(" FROM ");
		Utils.outputExpr(buffer, this.args()[0]);
		buffer.append(")");
		return buffer;
	}
}
