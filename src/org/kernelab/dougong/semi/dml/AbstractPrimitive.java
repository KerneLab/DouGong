package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;

public class AbstractPrimitive extends AbstractFilterable implements Primitive
{
	public Delete delete()
	{
		return this.provider().provideDelete().from(this.from()).where(this.where());
	}

	@Override
	public AbstractPrimitive from(View view)
	{
		super.from(view);
		return this;
	}

	public Select fullJoin(View view, Condition cond)
	{
		return this.provider().provideSelect().from(this.from()).where(this.where()).fullJoin(view, cond);
	}

	public Select join(View view, Condition cond)
	{
		return this.provider().provideSelect().from(this.from()).where(this.where()).join(view, cond);
	}

	public Select leftJoin(View view, Condition cond)
	{
		return this.provider().provideSelect().from(this.from()).where(this.where()).leftJoin(view, cond);
	}

	public Select rightJoin(View view, Condition cond)
	{
		return this.provider().provideSelect().from(this.from()).where(this.where()).rightJoin(view, cond);
	}

	public Select select(Expression... exprs)
	{
		return this.provider().provideSelect().from(this.from()).where(this.where()).select(exprs);
	}

	public Update update()
	{
		return this.provider().provideUpdate().from(this.from()).where(this.where());
	}

	@Override
	public AbstractPrimitive where(Condition cond)
	{
		super.where(cond);
		return this;
	}
}
