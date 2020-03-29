package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.Withable;

public class AbstractPrimitive extends AbstractFilterable implements Primitive
{
	public Delete delete()
	{
		return this.provider().provideDelete().with(this.with()).from(this.from()).where(this.where());
	}

	@Override
	public AbstractPrimitive from(View view)
	{
		super.from(view);
		return this;
	}

	public Select fullJoin(View view, Column... using)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).fullJoin(view,
				using);
	}

	public Select fullJoin(View view, Condition cond)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).fullJoin(view,
				cond);
	}

	public Select fullJoin(View view, ForeignKey rels)
	{
		return fullJoin(view, rels.joinCondition());
	}

	public Select innerJoin(View view, Column... using)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).innerJoin(view,
				using);
	}

	public Select innerJoin(View view, Condition cond)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).innerJoin(view,
				cond);
	}

	public Select innerJoin(View view, ForeignKey rels)
	{
		return innerJoin(view, rels.joinCondition());
	}

	public Select leftJoin(View view, Column... using)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).leftJoin(view,
				using);
	}

	public Select leftJoin(View view, Condition cond)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).leftJoin(view,
				cond);
	}

	public Select leftJoin(View view, ForeignKey rels)
	{
		return leftJoin(view, rels.joinCondition());
	}

	public Select rightJoin(View view, Column... using)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).rightJoin(view,
				using);
	}

	public Select rightJoin(View view, Condition cond)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).rightJoin(view,
				cond);
	}

	public Select rightJoin(View view, ForeignKey rels)
	{
		return rightJoin(view, rels.joinCondition());
	}

	public Select select(Expression... exprs)
	{
		return this.provider().provideSelect().with(this.with()).from(this.from()).where(this.where()).select(exprs);
	}

	public Update update()
	{
		return this.provider().provideUpdate().with(this.with()).from(this.from()).where(this.where());
	}

	@Override
	public AbstractPrimitive where(Condition cond)
	{
		super.where(cond);
		return this;
	}

	@Override
	public AbstractPrimitive with(Withable... views)
	{
		super.with(views);
		return this;
	}
}
