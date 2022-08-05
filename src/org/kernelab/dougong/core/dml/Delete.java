package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Delete extends DML, Providable, Filterable, Joinable, Hintable, Withsable
{
	public Delete delete(Table... targets);

	public Delete from(View view);

	// //////////////////////////////////////////////////////

	public Delete joins(List<Join> joins);

	public Delete innerJoin(View view, Condition cond);

	public Delete leftJoin(View view, Condition cond);

	public Delete rightJoin(View view, Condition cond);

	public Delete fullJoin(View view, Condition cond);

	public Delete innerJoin(View view, Column... using);

	public Delete leftJoin(View view, Column... using);

	public Delete rightJoin(View view, Column... using);

	public Delete fullJoin(View view, Column... using);

	public Delete innerJoin(View view, ForeignKey rels);

	public Delete leftJoin(View view, ForeignKey rels);

	public Delete rightJoin(View view, ForeignKey rels);

	public Delete fullJoin(View view, ForeignKey rels);

	// //////////////////////////////////////////////////////

	public Delete where(Condition cond);

	public Delete hint(String hint);

	public Delete recursive(boolean recursive);

	public Delete withs(List<WithDefinition> with);
}
