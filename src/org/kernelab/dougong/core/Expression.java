package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.test.ComparisonTestable;
import org.kernelab.dougong.core.dml.test.LikeTestable;
import org.kernelab.dougong.core.dml.test.MembershipTestable;
import org.kernelab.dougong.core.dml.test.NullTestable;
import org.kernelab.dougong.core.dml.test.RangeTestable;

public interface Expression extends Text, ComparisonTestable, LikeTestable, MembershipTestable, NullTestable,
		RangeTestable
{
	/**
	 * Get the text of this object as an expression which could be computed and
	 * compared. Alias name suffix MUST NOT appear. Subquery should be
	 * surrounded with brackets but Items should not.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringExpressed(StringBuilder buffer);

	/**
	 * Get the text of this object as a selected item which should followed by
	 * the alias name if given.
	 * 
	 * @param buffer
	 * @return The given buffer.
	 */
	public StringBuilder toStringSelected(StringBuilder buffer);
}
