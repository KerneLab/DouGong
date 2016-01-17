package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractAllItems;

public class MariaAllItems extends AbstractAllItems
{
	public MariaAllItems(View view)
	{
		super(view);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.view() != null)
		{
			String alias = this.view().provider().provideAliasLabel(view().alias());
			if (alias != null)
			{
				buffer.append(alias);
				buffer.append('.');
			}
		}
		buffer.append(ALL_COLUMNS);
		return buffer;
	}
}
