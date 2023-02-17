package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Joinable
{
	public Joinable antiJoin(View view, Condition on);

	public Joinable antiJoin(View view, ForeignKey rels);

	public Joinable antiJoin(View view, Item... using);

	public Joinable crossJoin(View view, Condition on);

	public Joinable crossJoin(View view, ForeignKey rels);

	public Joinable crossJoin(View view, Item... using);

	public Joinable fullJoin(View view, Condition on);

	public Joinable fullJoin(View view, ForeignKey rels);

	public Joinable fullJoin(View view, Item... using);

	public Joinable innerJoin(View view, Condition on);

	public Joinable innerJoin(View view, ForeignKey rels);

	public Joinable innerJoin(View view, Item... using);

	public Joinable joins(List<Join> joins);

	public Joinable leftJoin(View view, Condition on);

	public Joinable leftJoin(View view, ForeignKey rels);

	public Joinable leftJoin(View view, Item... using);

	public Joinable natural();

	public Joinable rightJoin(View view, Condition on);

	public Joinable rightJoin(View view, ForeignKey rels);

	public Joinable rightJoin(View view, Item... using);

	public Joinable semiJoin(View view, Condition on);

	public Joinable semiJoin(View view, ForeignKey rels);

	public Joinable semiJoin(View view, Item... using);
}
