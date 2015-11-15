package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.View;

public interface Delete extends DML, Filterable
{
	public Delete from(View view);

	// //////////////////////////////////////////////////////

	public Delete where(Condition cond);
}
