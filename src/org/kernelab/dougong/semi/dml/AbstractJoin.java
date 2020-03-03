package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Join;

public abstract class AbstractJoin implements Join
{
	private View		leading;

	private Join		former;

	private byte		type;

	private View		view;

	private Condition	on;

	private Column[]	using;

	protected Join former()
	{
		return former;
	}

	public AbstractJoin join(View leading, Join former, byte type, View view, String alias)
	{
		this.leading = leading;
		this.former = former;
		this.type = type;
		this.view = view.alias(alias);
		return this;
	}

	protected View leading()
	{
		return leading;
	}

	protected Condition on()
	{
		return on;
	}

	public Join on(Condition condition)
	{
		this.on = condition;
		this.using = null;
		return this;
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.toStringJoinTable(buffer);

		if (this.on() != null)
		{
			this.toStringOnCondition(buffer);
		}
		else if (this.using() != null)
		{
			this.toStringUsingColumns(buffer);
		}

		return buffer;
	}

	protected abstract StringBuilder toStringJoinTable(StringBuilder buffer);

	protected abstract StringBuilder toStringOnCondition(StringBuilder buffer);

	protected abstract StringBuilder toStringUsingColumns(StringBuilder buffer);

	protected byte type()
	{
		return type;
	}

	protected Column[] using()
	{
		return using;
	}

	public Join using(Column... columns)
	{
		if (this.on() == null && this.using() == null)
		{
			this.using = columns;
		}
		this.on = null;

		if (columns != null)
		{
			Item item = null;

			for (Column col : columns)
			{
				if (col != null)
				{
					item = view().item(col.name());

					if (item != null)
					{
						((Column) item).usingByJoin(true);
					}
				}
			}

			if (former() != null)
			{
				former().using(columns);
			}
			else if (leading() != null && leading() instanceof Entity)
			{
				for (Column col : columns)
				{
					if (col != null)
					{
						item = leading().itemsMap().get(col.name());

						if (item instanceof Column)
						{
							((Column) item).usingByJoin(true);
						}
					}
				}
			}
		}

		return this;
	}

	public View view()
	{
		return view;
	}
}
