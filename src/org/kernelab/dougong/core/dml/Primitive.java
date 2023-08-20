package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Primitive extends Filterable, Joinable, Withsable
{
	@Override
	public Primitive anti();

	@Override
	public Primitive antiJoin(View view, Condition cond);

	@Override
	public Primitive antiJoin(View view, ForeignKey rels);

	@Override
	public Primitive antiJoin(View view, Item... using);

	@Override
	public Primitive cross();

	@Override
	public Primitive crossJoin(View view, Condition cond);

	@Override
	public Primitive crossJoin(View view, ForeignKey rels);

	@Override
	public Primitive crossJoin(View view, Item... using);

	/**
	 * Create a Delete using from and where information according to this
	 * object.
	 * 
	 * @param targets
	 *            The target tables which would be used when deleting
	 *            multi-tables.
	 * @return
	 */
	public Delete delete(Table... targets);

	@Override
	public Primitive from(View view);

	@Override
	public Primitive full();

	@Override
	public Primitive fullJoin(View view, Condition cond);

	@Override
	public Primitive fullJoin(View view, ForeignKey rels);

	@Override
	public Primitive fullJoin(View view, Item... using);

	@Override
	public Primitive inner();

	@Override
	public Primitive innerJoin(View view, Condition cond);

	@Override
	public Primitive innerJoin(View view, ForeignKey rels);

	@Override
	public Primitive innerJoin(View view, Item... using);

	@Override
	public Primitive join(View view, Condition cond);

	@Override
	public Primitive join(View view, ForeignKey rels);

	@Override
	public Primitive join(View view, Item... using);

	@Override
	public Primitive left();

	@Override
	public Primitive leftJoin(View view, Condition cond);

	@Override
	public Primitive leftJoin(View view, ForeignKey rels);

	@Override
	public Primitive leftJoin(View view, Item... using);

	@Override
	public Primitive natural();

	@Override
	public Primitive outer();

	@Override
	public Primitive right();

	@Override
	public Primitive rightJoin(View view, Condition cond);

	@Override
	public Primitive rightJoin(View view, ForeignKey rels);

	@Override
	public Primitive rightJoin(View view, Item... using);

	/**
	 * Create a Select which selecting the give expressions using from and where
	 * information according to this object.
	 * 
	 * @param exprs
	 * @return
	 */
	public Select select(Expression... exprs);

	/**
	 * Create a Select with given expressions which will overwrite the source
	 * items have the same corresponding labels.
	 * 
	 * @param exprs
	 * @return
	 */
	public Select selectOver(Expression... exprs);

	@Override
	public Primitive semi();

	@Override
	public Primitive semiJoin(View view, Condition cond);

	@Override
	public Primitive semiJoin(View view, ForeignKey rels);

	@Override
	public Primitive semiJoin(View view, Item... using);

	/**
	 * Create an Update using from and where information according to this
	 * object.
	 * 
	 * @return
	 */
	public Update update();

	@Override
	public Primitive where(Condition cond);

	public Primitive withs(boolean recursive, WithDefinition... withs);
}
