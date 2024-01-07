package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Relation;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.util.AccessGather;
import org.kernelab.dougong.core.util.AccessGather.Access;
import org.kernelab.dougong.core.util.Utils;

public class AbstractUpdate extends AbstractJoinable implements Update
{
	private List<Relation<Column, Expression>>	sets	= new LinkedList<Relation<Column, Expression>>();

	private String								hint;

	@Override
	public AbstractUpdate from(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractUpdate full()
	{
		super.full();
		return this;
	}

	@Override
	public AbstractUpdate fullJoin(View view, Condition on)
	{
		super.fullJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractUpdate fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractUpdate fullJoin(View view, Item... using)
	{
		super.fullJoin(view, using);
		return this;
	}

	public String hint()
	{
		return hint;
	}

	@Override
	public AbstractUpdate hint(String hint)
	{
		this.hint = hint;
		return this;
	}

	@Override
	public AbstractUpdate inner()
	{
		super.inner();
		return this;
	}

	@Override
	public AbstractUpdate innerJoin(View view, Condition on)
	{
		super.innerJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractUpdate innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractUpdate innerJoin(View view, Item... using)
	{
		super.innerJoin(view, using);
		return this;
	}

	@Override
	public AbstractUpdate join(View view, Condition on)
	{
		super.join(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractUpdate join(View view, ForeignKey rels)
	{
		return join(view, rels.joinCondition());
	}

	@Override
	public AbstractUpdate join(View view, Item... using)
	{
		super.join(view, using);
		return this;
	}

	@Override
	public AbstractUpdate joins(List<Join> joins)
	{
		super.joins(joins);
		return this;
	}

	@Override
	public AbstractUpdate left()
	{
		super.left();
		return this;
	}

	@Override
	public AbstractUpdate leftJoin(View view, Condition on)
	{
		super.leftJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractUpdate leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractUpdate leftJoin(View view, Item... using)
	{
		super.leftJoin(view, using);
		return this;
	}

	@Override
	public AbstractUpdate provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	public AbstractUpdate recursive(boolean recursive)
	{
		super.recursive(recursive);
		return this;
	}

	@Override
	public AbstractUpdate right()
	{
		super.right();
		return this;
	}

	@Override
	public AbstractUpdate rightJoin(View view, Condition on)
	{
		super.rightJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractUpdate rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractUpdate rightJoin(View view, Item... using)
	{
		super.rightJoin(view, using);
		return this;
	}

	@Override
	public AbstractUpdate set(Column column, Expression value)
	{
		sets().add(new Relation<Column, Expression>(column, value));
		return this;
	}

	public List<Relation<Column, Expression>> sets()
	{
		return sets;
	}

	@Override
	public AbstractUpdate sets(Expression... columnValuePairs)
	{
		this.sets().clear();

		if (columnValuePairs == null || columnValuePairs.length == 0)
		{
			return this;
		}

		for (int i = 0; i < columnValuePairs.length; i += 2)
		{
			this.set((Column) columnValuePairs[i], columnValuePairs[i + 1]);
		}

		return this;
	}

	@Override
	protected void textOfFrom(StringBuilder buffer)
	{
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
			fr.toStringUpdatable(buffer);
		}
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("UPDATE");
	}

	protected void textOfHint(StringBuilder buffer)
	{
		String hint = this.provider().provideHint(this.hint());
		if (hint != null)
		{
			buffer.append(' ').append(hint);
		}
	}

	protected void textOfSets(StringBuilder buffer)
	{
		boolean first = true;

		for (Relation<Column, Expression> set : this.sets())
		{
			if (first)
			{
				first = false;
				buffer.append(" SET ");
			}
			else
			{
				buffer.append(',');
			}
			this.provider().provideOutputColumnReference(buffer, set.getKey());
			buffer.append('=');
			Utils.outputExpr(buffer, set.getValue());
		}
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfHint(buffer);
		this.textOfFrom(buffer);
		this.textOfJoin(buffer);
		this.textOfSets(buffer);
		this.textOfWhere(buffer);
		return buffer;
	}

	@Override
	public AbstractUpdate update(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractUpdate where(Condition cond)
	{
		super.where(cond);
		AccessGather.gather(this, Access.TYPE_WHERE, cond);
		return this;
	}

	@Override
	public AbstractUpdate withs(List<WithDefinition> withs)
	{
		super.withs(withs);
		return this;
	}
}
