package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.semi.dml.AbstractSetopr;

public class OracleSetopr extends AbstractSetopr
{
	@Override
	protected String getOprName(byte type)
	{
		if (type == EXCEPT || type == EXCEPT_ALL)
		{
			return OPRS[type].replace("EXCEPT", "MINUS");
		}
		else
		{
			return OPRS[type];
		}
	}
}
