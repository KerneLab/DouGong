package org.kernelab.dougong.core.dml;

import java.util.List;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;

public interface Joinable
{
	public Joinable joins(List<Join> joins);

	public Joinable innerJoin(View view, Condition on);

	public Joinable leftJoin(View view, Condition on);

	public Joinable rightJoin(View view, Condition on);

	public Joinable fullJoin(View view, Condition on);

	public Joinable innerJoin(View view, Column... using);

	public Joinable leftJoin(View view, Column... using);

	public Joinable rightJoin(View view, Column... using);

	public Joinable fullJoin(View view, Column... using);

	public Joinable innerJoin(View view, ForeignKey rels);

	public Joinable leftJoin(View view, ForeignKey rels);

	public Joinable rightJoin(View view, ForeignKey rels);

	public Joinable fullJoin(View view, ForeignKey rels);
}
