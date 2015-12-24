package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.semi.dml.AbstractJoin;

public class MariaJoin extends AbstractJoin
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
		for (Column c : this.using())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			c.view().provider().provideOutputNameText(buffer, c.name());
		}
		buffer.append(')');
		return buffer;
	}

	@Override
	protected StringBuilder toStringJoinTable(StringBuilder buffer)
	{
		buffer.append(JOINS[type()]);
		buffer.append(" JOIN ");
		super.view().toStringAliased(buffer);
		return buffer;
	}
}
