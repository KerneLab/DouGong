package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Withable;
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
	public AbstractDelete fullJoin(View view, Column... using)
	{
		super.fullJoin(view, using);
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

	protected String hint()
	{
		return hint;
	}

	public AbstractDelete hint(String hint)
	{
		this.hint = hint;
		return this;
	}

	@Override
	public AbstractDelete innerJoin(View view, Column... using)
	{
		super.innerJoin(view, using);
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

	public AbstractDelete joins(List<Join> joins)
	{
		super.joins(joins);
		return this;
	}

	@Override
	public AbstractDelete leftJoin(View view, Column... using)
	{
		super.leftJoin(view, using);
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
	public AbstractDelete provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	public AbstractDelete rightJoin(View view, Column... using)
	{
		super.rightJoin(view, using);
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
	public AbstractDelete with(List<Withable> with)
	{
		super.with(with);
		return this;
	}
}
