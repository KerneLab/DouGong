package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;

public class AbstractSetopr implements Setopr
{
	private byte	type;

	private Select	select;

	protected String getOprName(byte type)
	{
		return OPRS[type];
	}

	@Override
	public Select select()
	{
		return select;
	}

	@Override
	public AbstractSetopr setopr(byte type, Select select)
	{
		this.type = type;
		this.select = select;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(' ');
		buffer.append(getOprName(type()));
		buffer.append(" (");
		select().toString(buffer);
		buffer.append(')');
		return buffer;
	}

	@Override
	public byte type()
	{
		return type;
	}

	@Override
	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		buffer.append(' ');
		buffer.append(getOprName(type()));
		buffer.append(" (");
		select().toStringScoped(buffer);
		buffer.append(')');
		return buffer;
	}
}
