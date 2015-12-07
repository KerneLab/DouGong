package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Alias;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.View;

public interface Select extends DML, Text, Alias, Scope, Providable, Filterable
{
	public Select select(Expression... exprs);

	// //////////////////////////////////////////////////////

	public Select distinct(boolean distinct);

	// //////////////////////////////////////////////////////

	public Select from(View view);

	// //////////////////////////////////////////////////////

	public Select join(View view, Condition cond);

	public Select leftJoin(View view, Condition cond);

	public Select rightJoin(View view, Condition cond);

	public Select fullJoin(View view, Condition cond);

	public Select join(View view, Column... using);

	public Select leftJoin(View view, Column... using);

	public Select rightJoin(View view, Column... using);

	public Select fullJoin(View view, Column... using);

	// //////////////////////////////////////////////////////

	public Select where(Condition cond);

	// //////////////////////////////////////////////////////

	public Select groupBy(Expression... exprs);

	// //////////////////////////////////////////////////////

	public Select having(Condition cond);

	// //////////////////////////////////////////////////////

	public Select intersect(Select select);

	public Select minus(Select select);

	public Select union(Select select);

	public Select unionAll(Select select);

	// //////////////////////////////////////////////////////

	public Select orderBy(Expression... exprs);

	// //////////////////////////////////////////////////////

	public Select alias(String alias);

	public Select as(String alias);
}
