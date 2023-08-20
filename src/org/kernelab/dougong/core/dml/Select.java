package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Select extends DQL, Text, Alias, Item, View, Scope, Insertable, Updatable, Deletable, Source,
		Providable, Filterable, Joinable, Hintable, Withable, Withsable
{
	public Select select(Expression... exprs);

	public Select selectOver(Expression... exprs);

	// //////////////////////////////////////////////////////

	public Select distinct(boolean distinct);

	// //////////////////////////////////////////////////////

	@Override
	public Select from(View view);

	// //////////////////////////////////////////////////////

	@Override
	public Select joins(List<Join> joins);

	@Override
	public Select natural();

	@Override
	public Select anti();

	@Override
	public Select antiJoin(View view, Condition cond);

	@Override
	public Select antiJoin(View view, ForeignKey rels);

	@Override
	public Select antiJoin(View view, Item... using);

	@Override
	public Select cross();

	@Override
	public Select crossJoin(View view, Condition cond);

	@Override
	public Select crossJoin(View view, ForeignKey rels);

	@Override
	public Select crossJoin(View view, Item... using);

	@Override
	public Select full();

	@Override
	public Select fullJoin(View view, Condition cond);

	@Override
	public Select fullJoin(View view, ForeignKey rels);

	@Override
	public Select fullJoin(View view, Item... using);

	@Override
	public Select inner();

	@Override
	public Select innerJoin(View view, Condition cond);

	@Override
	public Select innerJoin(View view, ForeignKey rels);

	@Override
	public Select innerJoin(View view, Item... using);

	@Override
	public Select join(View view, Condition on);

	@Override
	public Select join(View view, ForeignKey rels);

	@Override
	public Select join(View view, Item... using);

	@Override
	public Select left();

	@Override
	public Select leftJoin(View view, Condition cond);

	@Override
	public Select leftJoin(View view, ForeignKey rels);

	@Override
	public Select leftJoin(View view, Item... using);

	@Override
	public Select outer();

	@Override
	public Select right();

	@Override
	public Select rightJoin(View view, Condition cond);

	@Override
	public Select rightJoin(View view, ForeignKey rels);

	@Override
	public Select rightJoin(View view, Item... using);

	@Override
	public Select semi();

	@Override
	public Select semiJoin(View view, Condition cond);

	@Override
	public Select semiJoin(View view, ForeignKey rels);

	@Override
	public Select semiJoin(View view, Item... using);

	// //////////////////////////////////////////////////////

	@Override
	public Select where(Condition cond);

	// //////////////////////////////////////////////////////

	public Select startWith(Condition startWith);

	public Select connectBy(Condition connectBy);

	public Select nocycle(boolean nocycle);

	// //////////////////////////////////////////////////////

	public Select groupBy(Expression... exprs);

	// //////////////////////////////////////////////////////

	public Select having(Condition cond);

	// //////////////////////////////////////////////////////

	public Select except(Select select);

	public Select intersect(Select select);

	public Select union(Select select);

	public Select unionAll(Select select);

	// //////////////////////////////////////////////////////

	public Select orderBy(Expression... exprs);

	// //////////////////////////////////////////////////////

	public <T extends Insertable> Insert insert(T target, Column... columns);

	// //////////////////////////////////////////////////////

	/**
	 * Specify the limit argument.
	 * 
	 * @param rows
	 *            The max number of rows would be returned. Could be
	 *            {@code null} if no limit.
	 * @return
	 */
	public Select limit(Expression rows);

	/**
	 * Specify the limit arguments including offset and rows parameter.
	 * 
	 * @param skip
	 *            The offset rows number. Could be {@code null} if no row need
	 *            to be skipped which is identical to {@code 0}.
	 * @param rows
	 *            The max number of rows would be returned. Could be
	 *            {@code null} if no limit.
	 * @return Select object.
	 */
	public Select limit(Expression skip, Expression rows);

	/**
	 * Specify the offset argument.
	 * 
	 * @param skip
	 *            The offset rows number. Could be {@code null} if no row need
	 *            to be skipped which is identical to {@code 0}.
	 * @return
	 */
	public Select offset(Expression skip);

	// //////////////////////////////////////////////////////

	@Override
	public Select alias(String alias);

	@Override
	public Select as(String alias);

	// //////////////////////////////////////////////////////

	@Override
	public Select hint(String hint);

	// //////////////////////////////////////////////////////

	@Override
	public Select recursive(boolean recursive);

	@Override
	public Select withs(List<WithDefinition> with);

	// //////////////////////////////////////////////////////

	public <T extends Subquery> T to(T subquery);
}
