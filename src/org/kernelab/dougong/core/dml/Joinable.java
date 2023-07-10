package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Joinable
{
	public Joinable anti();

	@Deprecated
	public Joinable antiJoin(View view, Condition on);

	@Deprecated
	public Joinable antiJoin(View view, ForeignKey rels);

	@Deprecated
	public Joinable antiJoin(View view, Item... using);

	public Joinable cross();

	@Deprecated
	public Joinable crossJoin(View view, Condition on);

	@Deprecated
	public Joinable crossJoin(View view, ForeignKey rels);

	@Deprecated
	public Joinable crossJoin(View view, Item... using);

	public Joinable full();

	@Deprecated
	public Joinable fullJoin(View view, Condition on);

	@Deprecated
	public Joinable fullJoin(View view, ForeignKey rels);

	@Deprecated
	public Joinable fullJoin(View view, Item... using);

	public Joinable inner();

	@Deprecated
	public Joinable innerJoin(View view, Condition on);

	@Deprecated
	public Joinable innerJoin(View view, ForeignKey rels);

	@Deprecated
	public Joinable innerJoin(View view, Item... using);

	public Joinable join(View view, Condition on);

	public Joinable join(View view, ForeignKey rels);

	public Joinable join(View view, Item... using);

	public Joinable joins(List<Join> joins);

	public Joinable left();

	@Deprecated
	public Joinable leftJoin(View view, Condition on);

	@Deprecated
	public Joinable leftJoin(View view, ForeignKey rels);

	@Deprecated
	public Joinable leftJoin(View view, Item... using);

	public Joinable natural();

	public Joinable outer();

	public Joinable right();

	@Deprecated
	public Joinable rightJoin(View view, Condition on);

	@Deprecated
	public Joinable rightJoin(View view, ForeignKey rels);

	@Deprecated
	public Joinable rightJoin(View view, Item... using);

	public Joinable semi();

	@Deprecated
	public Joinable semiJoin(View view, Condition on);

	@Deprecated
	public Joinable semiJoin(View view, ForeignKey rels);

	@Deprecated
	public Joinable semiJoin(View view, Item... using);
}
