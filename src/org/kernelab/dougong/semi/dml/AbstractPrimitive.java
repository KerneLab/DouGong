package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.Withable;

public class AbstractPrimitive extends AbstractJoinable implements Primitive
{
	public Delete delete(Table... targets)
	{
		return this.provider().provideDelete().with(this.with()) //
				.from(this.from()).joins(this.joins()).where(this.where()) //
				.delete(targets);
	}

	@Override
	public AbstractPrimitive from(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractPrimitive fullJoin(View view, Column... using)
	{
		super.fullJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive fullJoin(View view, Condition on)
	{
		super.fullJoin(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractPrimitive innerJoin(View view, Column... using)
	{
		super.innerJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive innerJoin(View view, Condition on)
	{
		super.innerJoin(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractPrimitive leftJoin(View view, Column... using)
	{
		super.leftJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive leftJoin(View view, Condition on)
	{
		super.leftJoin(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractPrimitive rightJoin(View view, Column... using)
	{

		super.rightJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive rightJoin(View view, Condition on)
	{
		super.rightJoin(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
	}

	public Select select(Expression... exprs)
	{
		return this.provider().provideSelect().with(this.with()) //
				.from(this.from()).joins(this.joins()).where(this.where()) //
				.select(exprs);
	}

	public Update update()
	{
		return this.provider().provideUpdate().with(this.with()) //
				.from(this.from()).joins(this.joins()).where(this.where());
	}

	@Override
	public AbstractPrimitive where(Condition cond)
	{
		super.where(cond);
		return this;
	}

	@Override
	public AbstractPrimitive with(Withable... withs)
	{
		super.with(withs);
		return this;
	}
}
