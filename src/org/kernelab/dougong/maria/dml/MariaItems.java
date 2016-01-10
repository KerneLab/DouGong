package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.semi.dml.AbstractItems;

public class MariaItems extends AbstractItems
{
	@Override
	protected MariaItems replicate()
	{
		return (MariaItems) new MariaItems().provider(this.provider()).list(this.list());
	}
}
