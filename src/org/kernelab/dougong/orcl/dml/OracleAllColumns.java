package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractAllColumns;

public class OracleAllColumns extends AbstractAllColumns
{
	public OracleAllColumns(View view)
	{
		super(view);
	}

	@Override
	protected OracleAllColumns replicate()
	{
		return new OracleAllColumns(view());
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
