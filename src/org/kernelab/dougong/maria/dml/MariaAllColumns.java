package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractAllColumns;

public class MariaAllColumns extends AbstractAllColumns
{
	public MariaAllColumns(View view)
	{
		super(view);
	}

	@Override
	protected MariaAllColumns replicate()
	{
		return new MariaAllColumns(view());
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
