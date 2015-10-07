package org.kernelab.dougong.orcl.dml;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractColumn;

public class OracleColumn extends AbstractColumn
{
	public OracleColumn(View view, String name)
	{
		super(view, name);
	}

	@Override
	protected AbstractColumn copy()
	{
		try
		{
			return (AbstractColumn) this.clone();
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}

	@Override
	protected OracleColumn replicate()
	{
		return new OracleColumn(this.view(), this.name());
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		if (Tools.notNullOrWhite(view().alias()))
		{
			buffer.append(view().alias());
			buffer.append('.');
		}
		buffer.append(name());
		return buffer;
	}

	public StringBuilder toStringAliased(StringBuilder buffer)
	{
		toString(buffer);
		if (Tools.notNullOrWhite(this.alias()))
		{
			buffer.append(' ');
			buffer.append(this.alias());
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
