package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.Withsable;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractProvidable;

public abstract class AbstractWithsable extends AbstractProvidable implements Withsable
{
	protected static List<Item> resolveAllItems(View view)
	{
		if (view instanceof Withable && ((Withable) view).with() != null)
		{
			String[] cols = ((Withable) view).with().columns();
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
		List<Item> items = view.items();
		if (items == null || items.isEmpty())
		{
			return items;
		}
		List<Item> list = new LinkedList<Item>();
		for (Item i : items)
		{
			if (i instanceof Column && ((Column) i).isPseudo())
			{
				continue;
			}
			list.add(i);
		}
		return list;
	}

	private boolean					recursive	= false;

	private List<WithDefinition>	with		= null;

	@Override
	protected AbstractWithsable clone() throws CloneNotSupportedException
	{
		AbstractWithsable clone = (AbstractWithsable) super.clone();

		if (this.with != null)
		{
			clone.with = Utils.copy(this.with, new LinkedList<WithDefinition>());
		}

		return clone;
	}

	@Override
	public Withable findWith(String withName)
	{
		if (withName == null || this.with == null)
		{
			return null;
		}
		for (WithDefinition with : with)
		{
			if (withName.equals(with.name()))
			{
				return with.select();
			}
		}
		return null;
	}

	@Override
	public boolean recursive()
	{
		return recursive;
	}

	@Override
	public AbstractWithsable recursive(boolean recursive)
	{
		this.recursive = recursive;
		return this;
	}

	protected void textOfWith(StringBuilder buffer)
	{
		List<WithDefinition> withs = this.withs();

		if (withs != null && !withs.isEmpty())
		{
			buffer.append("WITH ");

			if (this.recursive())
			{
				buffer.append("RECURSIVE ");
			}

			boolean first = true;

			for (WithDefinition with : withs)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				buffer.append(this.provider().provideAliasLabel(with.name()));
				if (with.columns() != null)
				{
					String[] cols = with.columns();
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
				with.select().toStringWith(buffer);
			}

			buffer.append(' ');
		}
	}

	@Override
	public List<WithDefinition> withs()
	{
		return with;
	}

	@Override
	public AbstractWithsable withs(List<WithDefinition> with)
	{
		this.with = with;
		return this;
	}

	@Override
	public AbstractWithsable withs(WithDefinition... withs)
	{
		this.with = new LinkedList<WithDefinition>();

		for (WithDefinition with : withs)
		{
			with.select().alias(null);
			this.with.add(with);
		}

		return this;
	}
}
