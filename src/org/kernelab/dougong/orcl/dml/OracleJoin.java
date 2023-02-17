package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.semi.dml.AbstractJoin;

public class OracleJoin extends AbstractJoin
{
	@Override
	protected StringBuilder toStringOnCondition(StringBuilder buffer)
	{
		buffer.append(" ON ");
		this.on().toString(buffer);
		return buffer;
	}

	@Override
	protected StringBuilder toStringUsingColumns(StringBuilder buffer)
	{
		buffer.append(" USING (");
		boolean first = true;
		for (Item c : this.using())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			this.view().provider().provideOutputNameText(buffer, c.label());
		}
		buffer.append(')');
		return buffer;
	}
}
