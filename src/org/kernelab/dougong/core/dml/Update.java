package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;

public interface Update extends DML, Filterable, Hintable, Withsable
{
	public Update update(View view);

	public Update from(View view);

	// //////////////////////////////////////////////////////

	public Update set(Column column, Expression value);

	public Update sets(Expression... columnValuePairs);

	// //////////////////////////////////////////////////////

	public Update where(Condition cond);

	public Update hint(String hint);

	public Update with(List<Withable> with);
}
