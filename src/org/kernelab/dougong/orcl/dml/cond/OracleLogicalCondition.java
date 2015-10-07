package org.kernelab.dougong.orcl.dml.cond;

import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractLogicalCondition;

public class OracleLogicalCondition extends AbstractLogicalCondition
{
	public StringBuilder toString(StringBuilder buffer)
	{
		boolean first = true;
		for (Object o : this.conds)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(' ');
			}
			if (o instanceof LogicalCondition)
			{
				buffer.append('(');
			}
			Utils.text(buffer, o);
			if (o instanceof LogicalCondition)
			{
				buffer.append(')');
			}
		}
		return buffer;
	}
}
