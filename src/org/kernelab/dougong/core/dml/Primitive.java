package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.View;

public interface Primitive extends Filterable
{
	/**
	 * Create a Delete using from and where information according to this
	 * object.
	 * 
	 * @return
	 */
	public Delete delete();

	public Primitive from(View view);

	/**
	 * Create a Select which full-joining the given view with the using columns
	 * using from and where information according to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Select fullJoin(View view, Column... using);

	/**
	 * Create a Select which full-joining the given view with the condition
	 * using from and where information according to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Select fullJoin(View view, Condition cond);

	/**
	 * Create a Select which joining the given view with the using columns using
	 * from and where information according to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Select join(View view, Column... using);

	/**
	 * Create a Select which joining the given view with the condition using
	 * from and where information according to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Select join(View view, Condition cond);

	/**
	 * Create a Select which left-joining the given view with the using columns
	 * using from and where information according to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Select leftJoin(View view, Column... using);

	/**
	 * Create a Select which left-joining the given view with the condition
	 * using from and where information according to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Select leftJoin(View view, Condition cond);

	/**
	 * Create a Select which right-joining the given view with the using columns
	 * using from and where information according to this object.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Select rightJoin(View view, Column... using);

	/**
	 * Create a Select which right-joining the given view with the condition
	 * using from and where information according to this object.
	 * 
	 * @param view
	 * @param cond
	 * @return
	 */
	public Select rightJoin(View view, Condition cond);

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
}
