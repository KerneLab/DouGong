package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;

public interface Pivot extends View
{
	@SuppressWarnings("unchecked")
	@Override
	public Pivot as(String alias);

	public Function[] pivotAggs();

	public Pivot pivotAggs(Function... aggs);

	public Column[] pivotFor();

	public Pivot pivotFor(Column... fors);

	public Item[] pivotIn();

	public Pivot pivotIn(Item... ins);

	public View pivotOn();

	public Pivot pivotOn(View view);

	/**
	 * Get the text of this pivot itself not including the pivot target view.
	 * 
	 * @param buffer
	 * @return
	 */
	public StringBuilder toStringPivot(StringBuilder buffer);
}
