package org.kernelab.dougong.core.dml;

import org.kernelab.basis.Castable;

public interface Withable extends Alias, Castable
{
	public StringBuilder toStringWith(StringBuilder buffer);

	public WithDefinition with();

	public Withable with(WithDefinition define);
}
