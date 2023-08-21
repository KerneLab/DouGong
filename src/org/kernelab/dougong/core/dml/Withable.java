package org.kernelab.dougong.core.dml;

import org.kernelab.basis.Castable;
import org.kernelab.dougong.core.View;

public interface Withable extends Alias, Castable, View
{
	public StringBuilder toStringWith(StringBuilder buffer);

	public WithDefinition with();

	public Withable with(WithDefinition define);
}
