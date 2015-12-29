package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.test.ComparisonTestable;
import org.kernelab.dougong.core.dml.test.LikeTestable;
import org.kernelab.dougong.core.dml.test.MembershipTestable;
import org.kernelab.dougong.core.dml.test.NullTestable;
import org.kernelab.dougong.core.dml.test.RangeTestable;

public interface Subquery extends View, Scope, Expression, ComparisonTestable, LikeTestable, MembershipTestable,
		NullTestable, RangeTestable
{
	public Subquery alias(String alias);

	public <T extends Subquery> T as(String alias);

	public Select select();

	public Subquery select(Select select);
}
