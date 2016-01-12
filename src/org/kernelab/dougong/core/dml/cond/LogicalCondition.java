package org.kernelab.dougong.core.dml.cond;

import org.kernelab.dougong.core.dml.Condition;

public interface LogicalCondition extends ComposableCondition
{
	public static final String	AND	= "AND";

	public static final String	NOT	= "NOT";

	public static final String	OR	= "OR";

	/**
	 * Reset the LogicalCondition and set the initial condition with NOT logic.
	 * 
	 * @param when
	 * @param cond
	 * @return
	 */
	public LogicalCondition not(boolean when, Condition cond);

	/**
	 * Reset the LogicalCondition and set the initial condition with NOT logic.
	 * 
	 * @param cond
	 * @return
	 */
	public LogicalCondition not(Condition cond);

	/**
	 * Reset the LogicalCondition and set the initial condition.
	 * 
	 * @param cond
	 * @return
	 */
	public LogicalCondition set(Condition cond);
}
