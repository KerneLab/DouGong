package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Condition;

public interface LogicalCondition extends Condition
{
	public static final String	AND	= "AND";

	public static final String	NOT	= "NOT";

	public static final String	OR	= "OR";

	/**
	 * Add a new condition to this LogicalCondition with AND relation.
	 * 
	 * @param cond
	 * @return
	 */
	public LogicalCondition and(Condition cond);

	/**
	 * Reset the LogicalCondition and set the initial condition with NOT logic.
	 * 
	 * @param cond
	 * @return
	 */
	public LogicalCondition not(Condition cond);

	/**
	 * Add a new condition to this LogicalCondition with OR relation.
	 * 
	 * @param cond
	 * @return
	 */
	public LogicalCondition or(Condition cond);

	/**
	 * Reset the LogicalCondition and set the initial condition.
	 * 
	 * @param cond
	 * @return
	 */
	public LogicalCondition set(Condition cond);
}
