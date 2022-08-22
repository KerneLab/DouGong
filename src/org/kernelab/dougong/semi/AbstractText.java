package org.kernelab.dougong.semi;

import org.kernelab.dougong.core.Text;

public abstract class AbstractText extends AbstractProvidable implements Text
{
	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}
}
