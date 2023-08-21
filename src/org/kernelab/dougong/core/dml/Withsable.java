package org.kernelab.dougong.core.dml;

import java.util.List;

public interface Withsable
{
	public Withable findWith(String name);

	public boolean recursive();

	public Withsable recursive(boolean recursive);

	public List<WithDefinition> withs();

	public Withsable withs(List<WithDefinition> views);

	public Withsable withs(WithDefinition... withs);
}
