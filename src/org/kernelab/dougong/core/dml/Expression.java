package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.dml.opr.ArithmeticOperable;
import org.kernelab.dougong.core.dml.opr.CastCaseOperable;
import org.kernelab.dougong.core.dml.opr.JointOperable;
import org.kernelab.dougong.core.dml.test.Testable;

public interface Expression extends Text, Testable, ArithmeticOperable, JointOperable, CastCaseOperable
{
	/**
	 * Get the items represented by this expression being selected. Which means
	 * {@code *} should be resolved as real items.
	 * 
	 * @return
	 */
	public List<Item> resolveItems();

	/**
	 * Get the text of this object as an expression which could be computed and
	 * compared. Alias name suffix MUST NOT appear. Subquery should be
	 * surrounded with brackets but Items should not.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringExpress(StringBuilder buffer);

	/**
	 * Get the text of this object as a selected item which should followed by
	 * the alias name if given.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringSelected(StringBuilder buffer);
}
