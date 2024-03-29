package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractAllItems;

public class OracleAllItems extends AbstractAllItems
{
	public OracleAllItems(View view)
	{
		super(view);
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		if (this.view() != null)
		{
			String alias = this.view().provider().provideAliasLabel(view().provider().provideViewAlias(view()));
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
