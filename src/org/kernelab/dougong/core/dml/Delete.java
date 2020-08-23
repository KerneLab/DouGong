package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.View;

public interface Delete extends DML, Filterable, Hintable, Withsable
{
	public Delete from(View view);

	// //////////////////////////////////////////////////////

	public Delete where(Condition cond);

	public Delete hint(String hint);

	public Delete with(List<Withable> with);
}
