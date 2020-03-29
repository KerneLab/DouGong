package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.Withsable;
import org.kernelab.dougong.semi.AbstractProvidable;

public class AbstractWithsable extends AbstractProvidable implements Withsable
{
	private List<Withable> with = null;

	public void textOfWith(StringBuilder buffer)
	{
		List<Withable> with = this.with();

		if (with != null && !with.isEmpty())
		{
			buffer.append("WITH ");

			boolean first = true;

			for (Withable view : with)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				buffer.append(this.provider().provideAliasLabel(view.withName()));
				buffer.append(" AS ");
				view.toStringWith(buffer);
			}

			buffer.append(' ');
		}
	}

	public List<Withable> with()
	{
		return with;
	}

	public AbstractWithsable with(List<Withable> with)
	{
		this.with = with;
		return this;
	}

	public AbstractWithsable with(Withable... views)
	{
		this.with = new LinkedList<Withable>();

		for (Withable view : views)
		{
			view.withName(view.alias());
			view.alias(null);
			this.with.add(view);
		}

		return this;
	}
}
