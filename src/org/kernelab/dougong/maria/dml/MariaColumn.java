package org.kernelab.dougong.maria.dml;

import java.lang.reflect.Field;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractColumn;

public class MariaColumn extends AbstractColumn
{
	public MariaColumn(View view, String name, Field field)
	{
		super(view, name, field);
	}

	@Override
	protected MariaColumn replicate()
	{
		return new MariaColumn(view(), name(), field());
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		String alias = this.view().provider().provideAliasLabel(view().alias());
		if (alias != null)
		{
			buffer.append(alias);
			buffer.append('.');
		}
		this.view().provider().provideOutputNameText(buffer, name());
		return buffer;
	}
}
