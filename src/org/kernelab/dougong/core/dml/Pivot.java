package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;

public interface Pivot extends View
{
	public Pivot as(String alias);

	public Function[] pivotAggs();

	public Pivot pivotAggs(Function... aggs);

	public Column[] pivotFor();

	public Pivot pivotFor(Column... fors);

	public Item[] pivotIn();

	public Pivot pivotIn(Item... ins);

	public View pivotOn();

	public Pivot pivotOn(View view);
}
