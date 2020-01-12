package org.kernelab.dougong.semi.dml;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Select;

public abstract class PredeclaredView extends AbstractView
{
	public AllItems all()
	{
		return this.provider().provideAllItems(this);
	}

	public abstract JSON parameters();

	public Select select()
	{
		return this.select(this.provider().provideSQL());
	}

	protected abstract Select select(SQL sql);

	public StringBuilder toString(StringBuilder buffer)
	{
		return this.select().toString(buffer);
	}

	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return this.select().toStringDeletable(buffer);
	}

	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		return this.select().toStringUpdatable(buffer);
	}

	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		buffer.append('(');
		this.toString(buffer);
		buffer.append(')');
		return buffer;
	}
}
