package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Joinable
{
	/**
	 * Hint ANTI word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable anti();

	/**
	 * Declare ANTI-join.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable antiJoin(View view, Condition on);

	/**
	 * Declare ANTI-join.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable antiJoin(View view, ForeignKey rels);

	/**
	 * Declare ANTI-join.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable antiJoin(View view, Item... using);

	/**
	 * Hint CROSS word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable cross();

	/**
	 * Declare CROSS-join.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable crossJoin(View view, Condition on);

	/**
	 * Declare CROSS-join.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable crossJoin(View view, ForeignKey rels);

	/**
	 * Declare CROSS-join.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable crossJoin(View view, Item... using);

	/**
	 * Hint FULL word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable full();

	/**
	 * Declare FULL-join.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable fullJoin(View view, Condition on);

	/**
	 * Declare FULL-join.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable fullJoin(View view, ForeignKey rels);

	/**
	 * Declare FULL-join.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable fullJoin(View view, Item... using);

	/**
	 * Hint INNER word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable inner();

	/**
	 * Declare INNER-join.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable innerJoin(View view, Condition on);

	/**
	 * Declare INNER-join.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable innerJoin(View view, ForeignKey rels);

	/**
	 * Declare INNER-join.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable innerJoin(View view, Item... using);

	/**
	 * Declare join to this object according to the given hint words.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable join(View view, Condition on);

	/**
	 * Declare join to this object according to the given hint words.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable join(View view, ForeignKey rels);

	/**
	 * Declare join to this object according to the given hint words.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable join(View view, Item... using);

	public Joinable joins(List<Join> joins);

	/**
	 * Hint LEFT word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable left();

	/**
	 * Declare LEFT-join.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable leftJoin(View view, Condition on);

	/**
	 * Declare LEFT-join.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable leftJoin(View view, ForeignKey rels);

	/**
	 * Declare LEFT-join.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable leftJoin(View view, Item... using);

	/**
	 * Hint NATURAL word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable natural();

	/**
	 * Hint OUTER word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable outer();

	/**
	 * Hint RIGHT word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable right();

	/**
	 * Declare RIGHT-join.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable rightJoin(View view, Condition on);

	/**
	 * Declare RIGHT-join.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable rightJoin(View view, ForeignKey rels);

	/**
	 * Declare RIGHT-join.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable rightJoin(View view, Item... using);

	/**
	 * Hint SEMI word for next join to be declared.
	 * 
	 * @return
	 */
	public Joinable semi();

	/**
	 * Declare SEMI-join.
	 * 
	 * @param view
	 * @param on
	 * @return
	 */
	public Joinable semiJoin(View view, Condition on);

	/**
	 * Declare SEMI-join.
	 * 
	 * @param view
	 * @param rels
	 * @return
	 */
	public Joinable semiJoin(View view, ForeignKey rels);

	/**
	 * Declare SEMI-join.
	 * 
	 * @param view
	 * @param using
	 * @return
	 */
	public Joinable semiJoin(View view, Item... using);
}
