package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.util.AccessGather;
import org.kernelab.dougong.core.util.AccessGather.Access;

public class AbstractDelete extends AbstractJoinable implements Delete
{
	private Table[]	targets;

	private String	hint;

	protected Table[] delete()
	{
		return this.targets;
	}

	@Override
	public AbstractDelete delete(Table... targets)
	{
		this.targets = targets;
		return this;
	}

	@Override
	public AbstractDelete from(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractDelete full()
	{
		super.full();
		return this;
	}

	@Override
	public AbstractDelete fullJoin(View view, Condition on)
	{
		super.fullJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractDelete fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractDelete fullJoin(View view, Item... using)
	{
		super.fullJoin(view, using);
		return this;
	}

	protected String hint()
	{
		return hint;
	}

	@Override
	public AbstractDelete hint(String hint)
	{
		this.hint = hint;
		return this;
	}

	@Override
	public AbstractDelete inner()
	{
		super.inner();
		return this;
	}

	@Override
	public AbstractDelete innerJoin(View view, Condition on)
	{
		super.innerJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractDelete innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractDelete innerJoin(View view, Item... using)
	{
		super.innerJoin(view, using);
		return this;
	}

	@Override
	public AbstractDelete join(View view, Condition on)
	{
		super.join(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractDelete join(View view, ForeignKey rels)
	{
		return join(view, rels.joinCondition());
	}

	@Override
	public AbstractDelete join(View view, Item... using)
	{
		super.join(view, using);
		return this;
	}

	@Override
	public AbstractDelete joins(List<Join> joins)
	{
		super.joins(joins);
		return this;
	}

	@Override
	public AbstractDelete left()
	{
		super.left();
		return this;
	}

	@Override
	public AbstractDelete leftJoin(View view, Condition on)
	{
		super.leftJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractDelete leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractDelete leftJoin(View view, Item... using)
	{
		super.leftJoin(view, using);
		return this;
	}

	@Override
	public AbstractDelete provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	public AbstractDelete recursive(boolean recursive)
	{
		super.recursive(recursive);
		return this;
	}

	@Override
	public AbstractDelete right()
	{
		super.right();
		return this;
	}

	@Override
	public AbstractDelete rightJoin(View view, Condition on)
	{
		super.rightJoin(view, on);
		AccessGather.gather(this, Access.TYPE_JOIN, on);
		return this;
	}

	@Override
	public AbstractDelete rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractDelete rightJoin(View view, Item... using)
	{
		super.rightJoin(view, using);
		return this;
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("DELETE");
	}

	protected void textOfHint(StringBuilder buffer)
	{
		String hint = this.provider().provideHint(this.hint());
		if (hint != null)
		{
			buffer.append(' ').append(hint);
		}
	}

	protected void textOfTargets(StringBuilder buffer)
	{
		if (this.delete() == null || this.delete().length == 0)
		{
			return;
		}

		boolean first = true;

		for (Table target : this.delete())
		{
			if (first)
			{
				first = false;
				buffer.append(' ');
			}
			else
			{
				buffer.append(',');
			}
			this.provider().provideOutputNameText(buffer, target.alias() != null ? target.alias() : target.name());
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
		this.textOfTargets(buffer);
		this.textOfFrom(buffer);
		this.textOfJoin(buffer);
		this.textOfWhere(buffer);
		return buffer;
	}

	@Override
	public AbstractDelete where(Condition cond)
	{
		super.where(cond);
		AccessGather.gather(this, Access.TYPE_WHERE, cond);
		return this;
	}

	@Override
	public AbstractDelete withs(List<WithDefinition> withs)
	{
		super.withs(withs);
		return this;
	}
}
