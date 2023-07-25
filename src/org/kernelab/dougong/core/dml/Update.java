package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Update extends DML, Providable, Filterable, Joinable, Hintable, Withsable
{
	public Update update(View view);

	@Override
	public Update from(View view);

	// //////////////////////////////////////////////////////

	@Override
	public Update joins(List<Join> join);

	@Override
	public Update full();

	@Override
	public Update fullJoin(View view, Condition cond);

	@Override
	public Update fullJoin(View view, ForeignKey rels);

	@Override
	public Update fullJoin(View view, Item... using);

	@Override
	public Update inner();

	@Override
	public Update innerJoin(View view, Condition cond);

	@Override
	public Update innerJoin(View view, ForeignKey rels);

	@Override
	public Update innerJoin(View view, Item... using);

	@Override
	public Update join(View view, Condition on);

	@Override
	public Update join(View view, ForeignKey rels);

	@Override
	public Update join(View view, Item... using);

	@Override
	public Update left();

	@Override
	public Update leftJoin(View view, Condition cond);

	@Override
	public Update leftJoin(View view, ForeignKey rels);

	@Override
	public Update leftJoin(View view, Item... using);

	@Override
	public Update right();

	@Override
	public Update rightJoin(View view, Condition cond);

	@Override
	public Update rightJoin(View view, ForeignKey rels);

	@Override
	public Update rightJoin(View view, Item... using);

	// //////////////////////////////////////////////////////

	public Update set(Column column, Expression value);

	public Update sets(Expression... columnValuePairs);

	// //////////////////////////////////////////////////////

	@Override
	public Update where(Condition cond);

	@Override
	public Update hint(String hint);

	@Override
	public Update recursive(boolean recursive);

	@Override
	public Update withs(List<WithDefinition> with);
}
