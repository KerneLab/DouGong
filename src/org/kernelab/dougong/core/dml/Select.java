package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Select extends DML, Text, Alias, Item, View, Scope, Insertable, Updatable, Deletable, Source,
		Providable, Filterable, Withsable
{
	public Select select(Expression... exprs);

	// //////////////////////////////////////////////////////

	public Select distinct(boolean distinct);

	// //////////////////////////////////////////////////////

	public Select from(View view);

	// //////////////////////////////////////////////////////

	public Select innerJoin(View view, Condition cond);

	public Select leftJoin(View view, Condition cond);

	public Select rightJoin(View view, Condition cond);

	public Select fullJoin(View view, Condition cond);

	public Select innerJoin(View view, Column... using);

	public Select leftJoin(View view, Column... using);

	public Select rightJoin(View view, Column... using);

	public Select fullJoin(View view, Column... using);

	public Select innerJoin(View view, ForeignKey rels);

	public Select leftJoin(View view, ForeignKey rels);

	public Select rightJoin(View view, ForeignKey rels);

	public Select fullJoin(View view, ForeignKey rels);

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

	public <T extends Insertable> Insert insert(T target, Column... column);

	// //////////////////////////////////////////////////////

	/**
	 * Specify the limit condition including skip and rows parameter.
	 * 
	 * @param skip
	 *            The skip rows number. Could be {@code null} if no row need to
	 *            be skipped which is identical to {@code 0}.
	 * @param rows
	 *            The max number of rows would be returned. This parameter must
	 *            NOT be {@code null}.
	 * @return Select object.
	 */
	public Select limit(Expression skip, Expression rows);

	// //////////////////////////////////////////////////////

	public Select alias(String alias);

	public Select as(String alias);

	// //////////////////////////////////////////////////////

	public Select with(List<Withable> with);

	public Subquery toSubquery();
}
