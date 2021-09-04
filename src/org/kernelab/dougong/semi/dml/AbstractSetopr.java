package org.kernelab.dougong.semi.dml;

import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;

public class AbstractSetopr implements Setopr
{
	private byte	type;

	private Select	select;

	@Override
	public Select select()
	{
		return select;
	}

	public AbstractSetopr setopr(byte type, Select select)
	{
		this.type = type;
		this.select = select;
		return this;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(' ');
		buffer.append(OPRS[type()]);
		buffer.append(' ');
		select().toStringScoped(buffer);
		return buffer;
	}

	@Override
	public byte type()
	{
		return type;
	}
}
