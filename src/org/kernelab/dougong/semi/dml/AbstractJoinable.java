package org.kernelab.dougong.semi.dml;

import java.util.ArrayList;
import java.util.List;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Joinable;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.util.Utils;

public class AbstractJoinable extends AbstractFilterable implements Joinable
{
	private List<View>	froms	= new ArrayList<View>();

	private List<Join>	joins	= new ArrayList<Join>();

	private boolean		natural	= false;

	private byte		direct	= AbstractJoin.DEFAULT;

	private byte		type	= AbstractJoin.DEFAULT;

	@Override
	public Joinable anti()
	{
		this.type = AbstractJoin.ANTI;
		return this;
	}

	@Override
	public Joinable antiJoin(View view, Condition on)
	{
		return this.setDirect(AbstractJoin.DEFAULT).setType(AbstractJoin.ANTI).join(view, on);
	}

	@Override
	public Joinable antiJoin(View view, ForeignKey rels)
	{
		return antiJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable antiJoin(View view, Item... using)
	{
		return this.setDirect(AbstractJoin.DEFAULT).setType(AbstractJoin.ANTI).join(view, using);
	}

	@Override
	protected AbstractJoinable clone() throws CloneNotSupportedException
	{
		AbstractJoinable clone = (AbstractJoinable) super.clone();

		if (this.froms != null)
		{
			clone.froms = Utils.copy(this.froms, new ArrayList<View>());
		}

		if (this.joins != null)
		{
			clone.joins = Utils.copy(this.joins, new ArrayList<Join>());
		}

		return clone;
	}

	@Override
	public Joinable cross()
	{
		this.direct = AbstractJoin.CROSS;
		this.type = AbstractJoin.DEFAULT;
		return this;
	}

	@Override
	public Joinable crossJoin(View view, Condition on)
	{
		return this.setDirect(AbstractJoin.CROSS).setType(AbstractJoin.DEFAULT).join(view, on);
	}

	@Override
	public Joinable crossJoin(View view, ForeignKey rels)
	{
		return crossJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable crossJoin(View view, Item... using)
	{
		return this.setDirect(AbstractJoin.CROSS).setType(AbstractJoin.DEFAULT).join(view, using);
	}

	/**
	 * Get the first view specified by from.
	 * 
	 * @return
	 */
	@Override
	public View from()
	{
		return froms().isEmpty() ? null : froms().get(0);
	}

	/**
	 * Add a view to the views' set.
	 * 
	 * @param view
	 * @return
	 */
	@Override
	public AbstractJoinable from(View view)
	{
		if (view != null)
		{
			froms().add(view);
			if (view instanceof ViewSelf && this instanceof View)
			{
				((ViewSelf) view).self((View) this);
			}
		}
		return this;
	}

	public List<View> froms()
	{
		return froms;
	}

	@Override
	public Joinable full()
	{
		this.direct = AbstractJoin.FULL;
		return this;
	}

	@Override
	public Joinable fullJoin(View view, Condition on)
	{
		return this.setDirect(AbstractJoin.FULL).setType(AbstractJoin.DEFAULT).join(view, on);
	}

	@Override
	public Joinable fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable fullJoin(View view, Item... using)
	{
		return this.setDirect(AbstractJoin.FULL).setType(AbstractJoin.DEFAULT).join(view, using);
	}

	protected byte getDirect()
	{
		return direct;
	}

	protected View getLastFrom()
	{
		return froms().isEmpty() ? null : froms().get(froms().size() - 1);
	}

	protected Join getLastJoin()
	{
		return joins().isEmpty() ? null : joins().get(joins().size() - 1);
	}

	protected byte getType()
	{
		return type;
	}

	@Override
	public Joinable inner()
	{
		this.direct = AbstractJoin.INNER;
		this.type = AbstractJoin.DEFAULT;
		return this;
	}

	@Override
	public Joinable innerJoin(View view, Condition on)
	{
		return this.setDirect(AbstractJoin.INNER).setType(AbstractJoin.DEFAULT).join(view, on);
	}

	@Override
	public Joinable innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable innerJoin(View view, Item... using)
	{
		return this.setDirect(AbstractJoin.INNER).setType(AbstractJoin.DEFAULT).join(view, using);
	}

	protected boolean isNatural()
	{
		return natural;
	}

	@Override
	public Joinable join(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), isNatural(), getDirect(), getType(), view, view.alias()).on(on));
		return reset();
	}

	@Override
	public Joinable join(View view, ForeignKey rels)
	{
		return join(view, rels.joinCondition());
	}

	@Override
	public Joinable join(View view, Item... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), isNatural(), getDirect(), getType(), view, view.alias())
				.using(using));
		return reset();
	}

	public List<Join> joins()
	{
		return joins;
	}

	@Override
	public Joinable joins(List<Join> joins)
	{
		this.joins.clear();

		if (joins != null)
		{
			for (Join join : joins)
			{
				this.setNatural(join.natural());
				this.setDirect(join.direct());
				this.setType(join.type());

				if (join.on() != null)
				{
					this.join(join.view(), join.on());
				}
				else if (join.using() != null)
				{
					this.join(join.view(), join.using());
				}
				else
				{
					this.join(join.view());
				}
			}
		}

		return this;
	}

	@Override
	public Joinable left()
	{
		this.direct = AbstractJoin.LEFT;
		return this;
	}

	@Override
	public Joinable leftJoin(View view, Condition on)
	{
		return this.setDirect(AbstractJoin.LEFT).setType(AbstractJoin.DEFAULT).join(view, on);
	}

	@Override
	public Joinable leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable leftJoin(View view, Item... using)
	{
		return this.setDirect(AbstractJoin.LEFT).setType(AbstractJoin.DEFAULT).join(view, using);
	}

	@Override
	public AbstractJoinable natural()
	{
		this.natural = true;
		return this;
	}

	@Override
	public Joinable outer()
	{
		this.type = AbstractJoin.OUTER;
		return this;
	}

	protected AbstractJoinable reset()
	{
		this.setNatural(false);
		this.setDirect(AbstractJoin.DEFAULT);
		this.setType(AbstractJoin.DEFAULT);
		return this;
	}

	@Override
	public Joinable right()
	{
		this.direct = AbstractJoin.RIGHT;
		return this;
	}

	@Override
	public Joinable rightJoin(View view, Condition on)
	{
		return this.setDirect(AbstractJoin.RIGHT).setType(AbstractJoin.DEFAULT).join(view, on);
	}

	@Override
	public Joinable rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable rightJoin(View view, Item... using)
	{
		return this.setDirect(AbstractJoin.RIGHT).setType(AbstractJoin.DEFAULT).join(view, using);
	}

	@Override
	public Joinable semi()
	{
		this.type = AbstractJoin.SEMI;
		return this;
	}

	@Override
	public Joinable semiJoin(View view, Condition on)
	{
		return this.setDirect(AbstractJoin.DEFAULT).setType(AbstractJoin.SEMI).join(view, on);
	}

	@Override
	public Joinable semiJoin(View view, ForeignKey rels)
	{
		return semiJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable semiJoin(View view, Item... using)
	{
		return this.setDirect(AbstractJoin.DEFAULT).setType(AbstractJoin.SEMI).join(view, using);
	}

	protected AbstractJoinable setDirect(byte direct)
	{
		this.direct = direct;
		return this;
	}

	protected AbstractJoinable setNatural(boolean natural)
	{
		this.natural = natural;
		return this;
	}

	protected AbstractJoinable setType(byte type)
	{
		this.type = type;
		return this;
	}

	@Override
	protected void textOfFrom(StringBuilder buffer)
	{
		buffer.append(" FROM");

		boolean first = true;

		for (View fr : froms())
		{
			if (first)
			{
				buffer.append(' ');
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			fr.toStringViewed(buffer);
		}
	}

	protected void textOfJoin(StringBuilder buffer)
	{
		for (Join join : joins())
		{
			buffer.append(' ');
			join.toString(buffer);
		}
	}

	public Primitive toPrimitive()
	{
		Primitive p = this.provider().providePrimitive();
		p.recursive(this.recursive()).withs(this.withs());
		for (View from : this.froms())
		{
			p.from(from);
		}
		p.joins(this.joins());
		p.where(this.where());
		return p;
	}
}
