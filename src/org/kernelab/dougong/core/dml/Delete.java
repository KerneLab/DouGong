package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Delete extends DML, Providable, Filterable, Joinable, Hintable, Withsable
{
	public Delete delete(Table... targets);

	@Override
	public Delete from(View view);

	// //////////////////////////////////////////////////////

	@Override
	public Delete joins(List<Join> joins);

	@Override
	public Delete full();

	@Override
	public Delete fullJoin(View view, Condition cond);

	@Override
	public Delete fullJoin(View view, ForeignKey rels);

	@Override
	public Delete fullJoin(View view, Item... using);

	@Override
	public Delete inner();

	@Override
	public Delete innerJoin(View view, Condition cond);

	@Override
	public Delete innerJoin(View view, ForeignKey rels);

	@Override
	public Delete innerJoin(View view, Item... using);

	@Override
	public Delete join(View view, Condition cond);

	@Override
	public Delete join(View view, ForeignKey rels);

	@Override
	public Delete join(View view, Item... using);

	@Override
	public Delete left();

	@Override
	public Delete leftJoin(View view, Condition cond);

	@Override
	public Delete leftJoin(View view, ForeignKey rels);

	@Override
	public Delete leftJoin(View view, Item... using);

	@Override
	public Delete right();

	@Override
	public Delete rightJoin(View view, Condition cond);

	@Override
	public Delete rightJoin(View view, ForeignKey rels);

	@Override
	public Delete rightJoin(View view, Item... using);

	// //////////////////////////////////////////////////////

	@Override
	public Delete where(Condition cond);

	@Override
	public Delete hint(String hint);

	@Override
	public Delete recursive(boolean recursive);

	@Override
	public Delete withs(List<WithDefinition> with);
}
