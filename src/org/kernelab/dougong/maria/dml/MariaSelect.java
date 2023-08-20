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
			rows().toStringExpress(buffer);
		}
	}

	protected void textOfOffset(StringBuilder buffer)
	{
		if (skip() != null)
		{
			buffer.append(" OFFSET ");
			skip().toStringExpress(buffer);
		}
	}

	@Override
	protected void toString(AbstractSelect select, StringBuilder buffer)
	{
		super.toString(select, buffer);
		this.textOfLimit(buffer);
		this.textOfOffset(buffer);
	}

	@Override
	protected void toStringScoped(AbstractSelect select, StringBuilder buffer)
	{
		super.toStringScoped(select, buffer);
		this.textOfLimit(buffer);
		this.textOfOffset(buffer);
	}

	@Override
	protected void toStringSourceOfBody(AbstractSelect select, StringBuilder buffer)
	{
		super.toStringSourceOfBody(select, buffer);
		this.textOfLimit(buffer);
		this.textOfOffset(buffer);
	}
}
