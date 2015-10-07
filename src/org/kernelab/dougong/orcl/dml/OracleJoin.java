package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.semi.dml.AbstractJoin;

public class OracleJoin extends AbstractJoin
{
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(JOINS[super.type]);
		buffer.append(" JOIN ");
		super.view.toString(buffer);
		buffer.append(" ON ");
		super.cond.toString(buffer);
		return buffer;
	}
}
