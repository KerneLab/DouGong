package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Primitive extends Filterable, Joinable, Withsable
{
	/**
	 * Add anti-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	@Override
	public Primitive antiJoin(View view, Condition cond);

	/**
	 * Add anti-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	@Override
	public Primitive antiJoin(View view, ForeignKey rels);

	/**
	 * Add anti-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	@Override
	public Primitive antiJoin(View view, Item... using);

	/**
	 * Add cross-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	@Override
	public Primitive crossJoin(View view, Condition cond);

	/**
	 * Add cross-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	@Override
	public Primitive crossJoin(View view, ForeignKey rels);

	/**
	 * Add cross-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
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

	/**
	 * Add full-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	@Override
	public Primitive fullJoin(View view, Condition cond);

	/**
	 * Add full-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	@Override
	public Primitive fullJoin(View view, ForeignKey rels);

	/**
	 * Add full-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	@Override
	public Primitive fullJoin(View view, Item... using);

	/**
	 * Add inner-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	@Override
	public Primitive innerJoin(View view, Condition cond);

	/**
	 * Add inner-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	@Override
	public Primitive innerJoin(View view, ForeignKey rels);

	/**
	 * Add inner-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	@Override
	public Primitive innerJoin(View view, Item... using);

	/**
	 * Add left-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	@Override
	public Primitive leftJoin(View view, Condition cond);

	/**
	 * Add left-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	@Override
	public Primitive leftJoin(View view, ForeignKey rels);

	/**
	 * Add left-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	@Override
	public Primitive leftJoin(View view, Item... using);

	/**
	 * Make next join become to a natural join.
	 */
	@Override
	public Primitive natural();

	/**
	 * Add right-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	@Override
	public Primitive rightJoin(View view, Condition cond);

	/**
	 * Add right-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	@Override
	public Primitive rightJoin(View view, ForeignKey rels);

	/**
	 * Add right-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
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
	 * Add semi-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	@Override
	public Primitive semiJoin(View view, Condition cond);

	/**
	 * Add semi-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	@Override
	public Primitive semiJoin(View view, ForeignKey rels);

	/**
	 * Add semi-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
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
