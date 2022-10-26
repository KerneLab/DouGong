package org.kernelab.dougong.semi.dml.cond;

import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.semi.AbstractProvidable;

public abstract class AbstractCondition extends AbstractProvidable implements Text
{
	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}
}
