package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.semi.dml.AbstractPivot;

public class MariaPivot extends AbstractPivot
{
	@Override
	protected String makeLabelOfPivotItem(Item item, Function func)
	{
		String label = item.alias() != null ? item.alias() : item.toStringExpress(new StringBuilder()).toString();
		if (func.alias() != null && this.pivotAggs().length > 1)
		{
			label += "_" + func.alias();
		}
		return label;
	}
}
