package org.kernelab.dougong.semi.dml;

import java.util.HashSet;
import java.util.Set;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Join;

public abstract class AbstractJoin implements Join
{
	private View		leading;

	private Join		former;

	private boolean		natural;

	private byte		type;

	private View		view;

	private Condition	on;

	private Item[]		using;

	protected void collectFormerLabels(Set<String> set)
	{
		for (Item col : this.view().items())
		{
			set.add(col.label());
		}

		if (former() != null)
		{
			((AbstractJoin) former()).collectFormerLabels(set);
		}
		else if (leading() != null)
		{
			for (Item col : leading().items())
			{
				set.add(col.label());
			}
		}
	}

	protected Join former()
	{
		return former;
	}

	@Override
	public AbstractJoin join(View leading, Join former, boolean natural, byte type, View view, String alias)
	{
		this.leading = leading;
		this.former = former;
		this.natural = natural;
		this.type = type;
		this.view = view.alias(alias);
		return this;
	}

	protected View leading()
	{
		return leading;
	}

	@Override
	public boolean natural()
	{
		return natural;
	}

	@Override
	public Condition on()
	{
		return on;
	}

	@Override
	public Join on(Condition condition)
	{
		if (!this.natural() //
				&& this.on() == null && this.using() == null //
				&& condition != null)
		{
			this.on = condition;
			this.using = null;
		}
		return this;
	}

	@Override
	public Join spread(Item... items)
	{
		if (items != null && items.length > 0)
		{
			Item item = null;

			for (Item col : items)
			{
				if (col != null)
				{
					item = view().item(col.label());

					if (item != null)
					{
						item.usingByJoin(true);
					}
				}
			}

			if (former() != null)
			{
				former().spread(items);
			}
			else if (leading() != null)
			{
				for (Item col : items)
				{
					if (col != null)
					{
						item = leading().item(col.label());

						if (item != null)
						{
							item.usingByJoin(true);
						}
					}
				}
			}
		}

		return this;
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.toStringJoinTable(buffer);

		if (!this.natural())
		{
			if (this.on() != null)
			{
				this.toStringOnCondition(buffer);
			}
			else if (this.using() != null)
			{
				this.toStringUsingColumns(buffer);
			}
		}

		return buffer;
	}

	protected StringBuilder toStringJoinTable(StringBuilder buffer)
	{
		if (this.natural())
		{
			buffer.append(NATURAL + " ");
		}
		buffer.append(JOINS[type()]);
		buffer.append(" JOIN ");
		this.view().toStringViewed(buffer);
		return buffer;
	}

	protected abstract StringBuilder toStringOnCondition(StringBuilder buffer);

	protected abstract StringBuilder toStringUsingColumns(StringBuilder buffer);

	@Override
	public byte type()
	{
		return type;
	}

	@Override
	public Item[] using()
	{
		return using;
	}

	@Override
	public Join using(Item... items)
	{
		if (!this.natural() //
				&& this.on() == null && this.using() == null //
				&& items != null && items.length > 0)
		{
			this.using = items;
			this.on = null;
		}

		if (!this.natural())
		{
			this.spread(items);
		}
		else
		{
			Set<String> formerCols = new HashSet<String>();

			if (former() != null)
			{
				((AbstractJoin) former()).collectFormerLabels(formerCols);
			}
			else if (leading() != null)
			{
				for (Item col : leading().items())
				{
					formerCols.add(col.label());
				}
			}

			Set<Item> naturing = new HashSet<Item>();
			for (Item col : this.view().items())
			{
				if (formerCols.contains(col.label()))
				{
					naturing.add(col);
				}
			}

			this.spread(naturing.toArray(new Item[0]));
		}

		return this;
	}

	@Override
	public View view()
	{
		return view;
	}
}
