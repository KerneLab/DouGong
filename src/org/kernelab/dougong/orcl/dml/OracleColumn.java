package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractColumn;

public class OracleColumn extends AbstractColumn
{
	public OracleColumn(View view, String name)
	{
		super(view, name);
	}

	@Override
	protected OracleColumn replicate()
	{
		return new OracleColumn(view(), name());
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		String alias = this.view().provider().provideAliasLabel(view().alias());
		if (alias != null && !isUsingByJoin())
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
		return this.view().provider().provideOutputAlias(buffer, this);
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		toString(buffer);
		return this.view().provider().provideOutputOrder(buffer, this);
	}
}
