package org.kernelab.dougong.maria.dml;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.dml.Item;
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
		for (Item i : this.using())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			Column c = Tools.as(i, Column.class);
			this.view().provider().provideOutputNameText(buffer, c != null ? c.name() : i.label());
		}
		buffer.append(')');
		return buffer;
	}
}
