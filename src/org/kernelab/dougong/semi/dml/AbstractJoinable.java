package org.kernelab.dougong.semi.dml;

import java.util.ArrayList;
import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Joinable;
import org.kernelab.dougong.core.util.Utils;

public class AbstractJoinable extends AbstractFilterable implements Joinable
{
	private List<View>	froms	= new ArrayList<View>();

	private List<Join>	joins	= new ArrayList<Join>();

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

	protected List<View> froms()
	{
		return froms;
	}

	@Override
	public Joinable fullJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.FULL_JOIN, view, view.alias()).using(using));
		return this;
	}

	@Override
	public Joinable fullJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.FULL_JOIN, view, view.alias()).on(on));
		return this;
	}

	@Override
	public Joinable fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
	}

	protected View getLastFrom()
	{
		return froms().isEmpty() ? null : froms().get(froms().size() - 1);
	}

	protected Join getLastJoin()
	{
		return joins().isEmpty() ? null : joins().get(joins().size() - 1);
	}

	@Override
	public Joinable innerJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.INNER_JOIN, view, view.alias()).using(using));
		return this;
	}

	@Override
	public Joinable innerJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.INNER_JOIN, view, view.alias()).on(on));
		return this;
	}

	@Override
	public Joinable innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	protected List<Join> joins()
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
				if (join.on() != null)
				{
					switch (join.type())
					{
						case Join.INNER_JOIN:
							this.innerJoin(join.view(), join.on());
							break;
						case Join.LEFT_JOIN:
							this.leftJoin(join.view(), join.on());
							break;
						case Join.RIGHT_JOIN:
							this.rightJoin(join.view(), join.on());
							break;
						case Join.FULL_JOIN:
							this.fullJoin(join.view(), join.on());
							break;
					}
				}
				else if (join.using() != null)
				{
					switch (join.type())
					{
						case Join.INNER_JOIN:
							this.innerJoin(join.view(), join.using());
							break;
						case Join.LEFT_JOIN:
							this.leftJoin(join.view(), join.using());
							break;
						case Join.RIGHT_JOIN:
							this.rightJoin(join.view(), join.using());
							break;
						case Join.FULL_JOIN:
							this.fullJoin(join.view(), join.using());
							break;
					}
				}
			}
		}

		return this;
	}

	@Override
	public Joinable leftJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.LEFT_JOIN, view, view.alias()).using(using));
		return this;
	}

	@Override
	public Joinable leftJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.LEFT_JOIN, view, view.alias()).on(on));
		return this;
	}

	@Override
	public Joinable leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	@Override
	public Joinable rightJoin(View view, Column... using)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.RIGHT_JOIN, view, view.alias()).using(using));
		return this;
	}

	@Override
	public Joinable rightJoin(View view, Condition on)
	{
		joins().add(provider().provideJoin() //
				.join(getLastFrom(), getLastJoin(), AbstractJoin.RIGHT_JOIN, view, view.alias()).on(on));
		return this;
	}

	@Override
	public Joinable rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
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
}
