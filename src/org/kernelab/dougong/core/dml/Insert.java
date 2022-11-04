package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;

public interface Insert extends DML, Providable, Hintable
{
	public Insert columns(Column... columns);

	@Override
	public Insert hint(String hint);

	public Insert into(Insertable target);

	public Insert pair(Column column, Expression value);

	public Insert pairs(Expression... columnValuePairs);

	public Insert select(Source source);

	public Insert values(Expression... values);
}
