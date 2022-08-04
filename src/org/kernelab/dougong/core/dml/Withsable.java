package org.kernelab.dougong.core.dml;

import java.util.List;

public interface Withsable
{
	public List<WithDefinition> withs();

	public Withsable withs(List<WithDefinition> views);

	public Withsable withs(WithDefinition... withs);
}
