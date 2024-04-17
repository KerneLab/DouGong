package org.kernelab.dougong.core.dml;

import org.kernelab.basis.Castable;
import org.kernelab.dougong.core.Scope;

public interface StringItem extends Sortable, Scope, Castable
{
	public String getString();

	public StringItem setString(String item);
}
