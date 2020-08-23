package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Hintable;
import org.kernelab.dougong.semi.AbstractProvidable;

public class AbstractHintable extends AbstractProvidable implements Hintable
{
	private String hint;

	protected String hint()
	{
		return hint;
	}

	public AbstractHintable hint(String hint)
	{
		this.hint = hint;
		return this;
	}

	protected void textOfHint(StringBuilder buffer)
	{
		String hint = this.provider().provideHint(this.hint());
		if (hint != null)
		{
			buffer.append(' ').append(hint);
		}
	}
}
