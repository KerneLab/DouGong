package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.View;

public interface Merge extends DML, Providable, Hintable
{
	public Merge hint(String hint);

	public Merge into(View target);

	public Merge using(View source);

	public Merge on(Condition conditions);

	public Merge whenMatched();

	public Merge update();

	public Merge set(Column column, Expression value);

	public Merge sets(Expression... columnValuePairs);

	public Merge whenNotMatched();

	public Merge insert(Column... columns);

	public Merge values(Expression... values);

	public Merge inserts(Expression... columnValuePairs);
}
