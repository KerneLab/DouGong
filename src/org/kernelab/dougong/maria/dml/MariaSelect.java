package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class MariaSelect extends AbstractSelect
{
	@Override
	public MariaSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	protected void textOfLimit(StringBuilder buffer)
	{
		if (rows() != null)
		{
			buffer.append(" LIMIT ");

			if (skip() != null)
			{
				skip().toStringExpress(buffer);
				buffer.append(',');
			}

			rows().toStringExpress(buffer);
		}
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		super.toString(buffer);
		this.textOfLimit(buffer);
		return buffer;
	}

	@Override
	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		super.toStringScoped(buffer);
		this.textOfLimit(buffer);
		return buffer;
	}
}
