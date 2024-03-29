package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.WithDefinition;

public class AbstractPrimitive extends AbstractJoinable implements Primitive
{
	@Override
	public AbstractPrimitive anti()
	{
		super.anti();
		return this;
	}

	@Override
	public AbstractPrimitive antiJoin(View view, Condition on)
	{
		super.antiJoin(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive antiJoin(View view, ForeignKey rels)
	{
		return antiJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractPrimitive antiJoin(View view, Item... using)
	{
		super.antiJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive cross()
	{
		super.cross();
		return this;
	}

	@Override
	public AbstractPrimitive crossJoin(View view, Condition on)
	{
		super.crossJoin(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive crossJoin(View view, ForeignKey rels)
	{
		return crossJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractPrimitive crossJoin(View view, Item... using)
	{
		super.crossJoin(view, using);
		return this;
	}

	@Override
	public Delete delete(Table... targets)
	{
		return this.provider().provideDelete() //
				.recursive(this.recursive()).withs(this.withs()) //
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
	public AbstractPrimitive full()
	{
		super.full();
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
	public AbstractPrimitive fullJoin(View view, Item... using)
	{
		super.fullJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive inner()
	{
		super.inner();
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
	public AbstractPrimitive innerJoin(View view, Item... using)
	{
		super.innerJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive join(View view, Condition on)
	{
		super.join(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive join(View view, ForeignKey rels)
	{
		return join(view, rels.joinCondition());
	}

	@Override
	public AbstractPrimitive join(View view, Item... using)
	{
		super.join(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive left()
	{
		super.left();
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
	public AbstractPrimitive leftJoin(View view, Item... using)
	{
		super.leftJoin(view, using);
		return this;
	}

	@Override
	public AbstractPrimitive natural()
	{
		super.natural();
		return this;
	}

	@Override
	public AbstractPrimitive outer()
	{
		super.outer();
		return this;
	}

	@Override
	public AbstractPrimitive right()
	{
		super.right();
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

	@Override
	public AbstractPrimitive rightJoin(View view, Item... using)
	{

		super.rightJoin(view, using);
		return this;
	}

	@Override
	public Select select(Expression... exprs)
	{
		return this.provider().provideSelect() //
				.recursive(this.recursive()).withs(this.withs()) //
				.from(this.from()).joins(this.joins()).where(this.where()) //
				.select(exprs);
	}

	@Override
	public Select selectOver(Expression... exprs)
	{
		return this.provider().provideSelect() //
				.recursive(this.recursive()).withs(this.withs()) //
				.from(this.from()).joins(this.joins()).where(this.where()) //
				.selectOver(exprs);
	}

	@Override
	public AbstractPrimitive semi()
	{
		super.semi();
		return this;
	}

	@Override
	public AbstractPrimitive semiJoin(View view, Condition on)
	{
		super.semiJoin(view, on);
		return this;
	}

	@Override
	public AbstractPrimitive semiJoin(View view, ForeignKey rels)
	{
		return semiJoin(view, rels.joinCondition());
	}

	@Override
	public AbstractPrimitive semiJoin(View view, Item... using)
	{
		super.semiJoin(view, using);
		return this;
	}

	@Override
	public Update update()
	{
		return this.provider().provideUpdate() //
				.recursive(this.recursive()).withs(this.withs()) //
				.from(this.from()).joins(this.joins()).where(this.where());
	}

	@Override
	public AbstractPrimitive where(Condition cond)
	{
		super.where(cond);
		return this;
	}

	@Override
	public AbstractPrimitive withs(boolean recursive, WithDefinition... withs)
	{
		super.recursive(recursive).withs(withs);
		return this;
	}
}
