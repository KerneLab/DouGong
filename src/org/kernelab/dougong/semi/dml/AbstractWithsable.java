package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.Withsable;
import org.kernelab.dougong.semi.AbstractProvidable;

public abstract class AbstractWithsable extends AbstractProvidable implements Withsable
{
	protected static List<Item> resolveAllItems(View view)
	{
		if (view instanceof Withable)
		{
			String[] cols = ((Withable) view).withCols();
			if (cols != null)
			{
				Provider p = view.provider();
				List<Item> items = new LinkedList<Item>();
				for (String col : cols)
				{
					items.add(p.provideReference(view, col));
				}
				return items;
			}
		}
		return view.items();
	}

	private List<Withable> with = null;

	protected void textOfWith(StringBuilder buffer)
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
				if (view.withCols() != null)
				{
					String[] cols = view.withCols();
					buffer.append(" (");
					for (int i = 0; i < cols.length; i++)
					{
						if (i > 0)
						{
							buffer.append(',');
						}
						buffer.append(this.provider().provideAliasLabel(cols[i]));
					}
					buffer.append(')');
				}
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

	public AbstractWithsable with(Withable... withs)
	{
		this.with = new LinkedList<Withable>();

		for (Withable with : withs)
		{
			with.alias(null);
			this.with.add(with);
		}

		return this;
	}
}
