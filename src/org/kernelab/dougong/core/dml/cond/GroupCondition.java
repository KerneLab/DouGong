package org.kernelab.dougong.core.dml.cond;

public interface GroupCondition
{
	public static final String	ALL		= "ALL";

	public static final String	ANY		= "ANY";

	public static final String	SOME	= "SOME";

	public GroupCondition all();

	public GroupCondition any();

	public GroupCondition some();
}
