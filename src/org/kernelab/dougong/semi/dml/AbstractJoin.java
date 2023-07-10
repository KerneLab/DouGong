package org.kernelab.dougong.semi.dml;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Join;

public abstract class AbstractJoin implements Join
{
	private View		leading;

	private Join		former;

	private boolean		natural;

	private byte		direct;

	private byte		type;

	private View		view; 

	private Condition	on;

	private Item[]		using;

	protected void collectLabels(Set<String> set, Item it)
	{
		if (it instanceof Column)
		{
			if (((Column) it).isPseudo())
			{
				return;
			}
		}
		set.add(it.label());
	}

	protected void collectLabelsRecursive(Set<String> set)
	{
		for (Item it : this.view().items())
		{
			collectLabels(set, it);
		}

		if (former() != null)
		{
			((AbstractJoin) former()).collectLabelsRecursive(set);
		}
		else if (leading() != null)
		{
			for (Item it : leading().items())
			{
				collectLabels(set, it);
			}
		}
	}

	@Override
	public byte direct()
	{
		return direct;
	}

	protected Join former()
	{
		return former;
	}

	@Override
	public AbstractJoin join(View leading, Join former, boolean natural, byte direct, byte type, View view,
			String alias)
	{
		this.leading = leading;
		this.former = former;
		this.natural = natural;
		this.direct = direct;
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
		if (this.on() == null && this.using() == null //
				&& condition != null)
		{
			this.on = condition;
			this.using = null;
		}
		return this;
	}

	@Override
	public Join spread(String... labels)
	{
		if (labels != null && labels.length > 0)
		{
			this.view().joinUsing(labels);

			if (former() != null)
			{
				former().spread(labels);
			}
			else if (leading() != null)
			{
				this.leading().joinUsing(labels);
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
		if (direct() > DEFAULT)
		{
			buffer.append(DIRECTS.get(direct()) + " ");
		}
		if (type() > DEFAULT)
		{
			buffer.append(TYPES.get(type()) + " ");
		}
		buffer.append("JOIN ");
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
		if (this.on() == null && this.using() == null //
				&& items != null && items.length > 0)
		{
			this.using = items;
			this.on = null;
		}

		if (!this.natural())
		{
			if (items != null && items.length > 0)
			{
				Set<String> labels = new LinkedHashSet<String>();

				for (Item item : items)
				{
					labels.add(item.label());
				}

				this.spread(labels.toArray(new String[0]));
			}
		}
		else
		{
			Set<String> formerCols = new HashSet<String>();

			if (former() != null)
			{
				((AbstractJoin) former()).collectLabelsRecursive(formerCols);
			}
			else if (leading() != null)
			{
				for (Item it : leading().items())
				{
					collectLabels(formerCols, it);
				}
			}

			Set<String> naturing = new LinkedHashSet<String>();
			for (Item it : this.view().items())
			{
				if (formerCols.contains(it.label()))
				{
					naturing.add(it.label());
				}
			}

			this.spread(naturing.toArray(new String[0]));
		}

		return this;
	}

	@Override
	public View view()
	{
		return view;
	}

	@Override
	public boolean viewSelectable()
	{
		return type() != SEMI && type() != ANTI;
	}
}
