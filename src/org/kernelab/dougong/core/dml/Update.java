package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;

public interface Update extends DML, Filterable
{
	public Update update(View view);

	public Update from(View view);

	// //////////////////////////////////////////////////////

	public Update set(Column column, Expression expr);

	// //////////////////////////////////////////////////////

	public Update where(Condition cond);
}
