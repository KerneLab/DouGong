package org.kernelab.dougong.semi.dml;

import java.util.List;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Withable;

public class AbstractDelete extends AbstractFilterable implements Delete
{
	private String hint;

	@Override
	public AbstractDelete from(View view)
	{
		super.from(view);
		return this;
	}

	protected String hint()
	{
		return hint;
	}

	public AbstractDelete hint(String hint)
	{
		this.hint = hint;
		return this;
	}

	@Override
	public AbstractDelete provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	protected void textOfFrom(StringBuilder buffer)
	{
		buffer.append(" FROM ");
		from().toStringDeletable(buffer);
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("DELETE");
	}

	protected void textOfHint(StringBuilder buffer)
	{
		String hint = this.provider().provideHint(this.hint());
		if (hint != null)
		{
			buffer.append(' ').append(hint);
		}
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfHint(buffer);
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

	@Override
	public AbstractDelete with(List<Withable> with)
	{
		super.with(with);
		return this;
	}
}
