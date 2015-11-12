package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.View;

public interface Select extends DML, View, Scope, Filterable
{
	public Select select(Object... items);

	// //////////////////////////////////////////////////////

	public Select distinct(boolean distinct);

	// //////////////////////////////////////////////////////

	public Select from(View view);

	// //////////////////////////////////////////////////////

	public Select join(View view, Condition cond);

	public Select leftJoin(View view, Condition cond);

	public Select rightJoin(View view, Condition cond);

	public Select fullJoin(View view, Condition cond);

	// //////////////////////////////////////////////////////

	public Select where(Condition cond);

	// //////////////////////////////////////////////////////

	public Select groupBy(Object... items);

	// //////////////////////////////////////////////////////

	public Select having(Condition cond);

	// //////////////////////////////////////////////////////

	public Select intersect(Select select);

	public Select minus(Select select);

	public Select union(Select select);

	public Select unionAll(Select select);

	// //////////////////////////////////////////////////////

	public Select orderBy(Object... items);

	// //////////////////////////////////////////////////////

	public Select alias(String alias);

	public Select as(String alias);
}
