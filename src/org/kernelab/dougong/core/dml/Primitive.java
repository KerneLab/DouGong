package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Primitive extends Filterable, Joinable, Withsable
{
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

	public Primitive from(View view);

	/**
	 * Add full-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Primitive fullJoin(View view, Column... using);

	/**
	 * Add full-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Primitive fullJoin(View view, Condition cond);

	/**
	 * Add full-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Primitive fullJoin(View view, ForeignKey rels);

	/**
	 * Add inner-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Primitive innerJoin(View view, Column... using);

	/**
	 * Add inner-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Primitive innerJoin(View view, Condition cond);

	/**
	 * Add inner-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Primitive innerJoin(View view, ForeignKey rels);

	/**
	 * Add left-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Primitive leftJoin(View view, Column... using);

	/**
	 * Add left-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Primitive leftJoin(View view, Condition cond);

	/**
	 * Add left-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Primitive leftJoin(View view, ForeignKey rels);

	/**
	 * Add right-join to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Primitive rightJoin(View view, Column... using);

	/**
	 * Add right-join to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Primitive rightJoin(View view, Condition cond);

	/**
	 * Add right-join to this object.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Primitive rightJoin(View view, ForeignKey rels);

	/**
	 * Create a Select which selecting the give expressions using from and where
	 * information according to this object.
	 * 
	 * @param exprs
	 * @return
	 */
	public Select select(Expression... exprs);

	/**
	 * Create an Update using from and where information according to this
	 * object.
	 * 
	 * @return
	 */
	public Update update();

	public Primitive where(Condition cond);

	public Primitive withs(boolean recursive, WithDefinition... withs);
}
