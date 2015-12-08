package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractColumn;

public class MariaColumn extends AbstractColumn
{
	public MariaColumn(View view, String name)
	{
		super(view, name);
	}

	@Override
	protected AbstractColumn replicate()
	{
		return new MariaColumn(view(), name());
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		String alias = this.view().provider().provideAliasLabel(view().alias());
		if (alias != null)
		{
			buffer.append(alias);
			buffer.append('.');
		}
		buffer.append(name());
		return buffer;
	}

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		toString(buffer);
		String alias = this.view().provider().provideAliasLabel(this.alias());
		if (alias != null)
		{
			buffer.append(' ');
			buffer.append(alias);
		}
		return buffer;
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
