package org.kernelab.dougong.core.dml.cond;

import java.util.List;

import org.kernelab.dougong.core.dml.Condition;

public interface LogicalCondition extends ComposableCondition, ReflectiveCondition
{
	public static final String	AND	= "AND";

	public static final String	OR	= "OR";

	public static final String	NOT	= "NOT";

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
	 * Get a list of composed conditions, including conditions and logical
	 * operators(e.g. AND/OR/NOT).
	 * 
	 * @return
	 */
	public List<Object> reflects();

	/**
	 * Reset the LogicalCondition and set the initial condition.
	 * 
	 * @param cond
	 * @return
	 */
	public LogicalCondition set(Condition cond);
}
