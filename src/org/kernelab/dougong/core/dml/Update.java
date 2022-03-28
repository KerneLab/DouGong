package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Update extends DML, Providable, Filterable, Joinable, Hintable, Withsable
{
	public Update update(View view);

	public Update from(View view);

	// //////////////////////////////////////////////////////

	public Update joins(List<Join> join);

	public Update innerJoin(View view, Condition cond);

	public Update leftJoin(View view, Condition cond);

	public Update rightJoin(View view, Condition cond);

	public Update fullJoin(View view, Condition cond);

	public Update innerJoin(View view, Column... using);

	public Update leftJoin(View view, Column... using);

	public Update rightJoin(View view, Column... using);

	public Update fullJoin(View view, Column... using);

	public Update innerJoin(View view, ForeignKey rels);

	public Update leftJoin(View view, ForeignKey rels);

	public Update rightJoin(View view, ForeignKey rels);

	public Update fullJoin(View view, ForeignKey rels);

	// //////////////////////////////////////////////////////

	public Update set(Column column, Expression value);

	public Update sets(Expression... columnValuePairs);

	// //////////////////////////////////////////////////////

	public Update where(Condition cond);

	public Update hint(String hint);

	public Update with(List<Withable> with);
}
