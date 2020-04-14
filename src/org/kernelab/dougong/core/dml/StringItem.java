package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Scope;

public interface StringItem extends Item, Scope, Sortable
{
	public String getString();

	public StringItem setString(String item);
}
