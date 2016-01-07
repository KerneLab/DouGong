package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.semi.dml.AbstractItems;

public class MariaItems extends AbstractItems
{
	@Override
	protected ComparisonCondition provideComparisonCondition()
	{
		return this.provider().provideComparisonCondition();
	}

	@Override
	protected LikeCondition provideLikeCondition()
	{
		return this.provider().provideLikeCondition();
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return this.provider().provideMembershipCondition();
	}

	@Override
	protected NullCondition provideNullCondition()
	{
		return this.provider().provideNullCondition();
	}

	@Override
	protected RangeCondition provideRangeCondition()
	{
		return this.provider().provideRangeCondition();
	}

	@Override
	protected MariaItems replicate()
	{
		return (MariaItems) new MariaItems().provider(this.provider()).list(this.list());
	}
}
