package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.semi.dml.AbstractItems;

public class OracleItems extends AbstractItems
{
	@Override
	protected OracleItems replicate()
	{
		return (OracleItems) new OracleItems().provider(this.provider()).list(this.list());
	}
}
