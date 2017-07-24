package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;

public class AbstractDelete extends AbstractFilterable implements Delete
{
	@Override
	public AbstractDelete from(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractDelete provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	public void textOfFrom(StringBuilder buffer)
	{
		buffer.append(" FROM ");
		from().toStringDeletable(buffer);
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("DELETE");
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfFrom(buffer);
		this.textOfWhere(buffer);
		return buffer;
	}

	@Override
	public AbstractDelete where(Condition cond)
	{
		super.where(cond);
		return this;
	}
}
