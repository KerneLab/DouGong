package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.semi.dml.AbstractStringItem;

public class MariaStringItem extends AbstractStringItem
{
	private Provider	provider;

	public MariaStringItem(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	protected ComparisonCondition provideComparisonCondition()
	{
		return provider.provideComparisonCondition();
	}

	@Override
	protected LikeCondition provideLikeCondition()
	{
		return provider.provideLikeCondition();
	}

	@Override
	protected MembershipCondition provideMembershipCondition()
	{
		return provider.provideMembershipCondition();
	}

	@Override
	protected NullCondition provideNullCondition()
	{
		return provider.provideNullCondition();
	}

	@Override
	protected RangeCondition provideRangeCondition()
	{
		return provider.provideRangeCondition();
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		toString(buffer);
		buffer.append(' ');
		if (this.ascending())
		{
			buffer.append("ASC");
		}
		else
		{
			buffer.append("DESC");
		}
		return buffer;
	}
}
