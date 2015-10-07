package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.OrderableItem;
import org.kernelab.dougong.core.dml.SingleItem;

public interface Column extends SingleItem, OrderableItem
{
	public Column as(String alias);

	public String name();

	public View view();
}
