package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;

public interface Insert extends DML, Hintable
{
	public Insert columns(Column... columns);

	public Insert hint(String hint);

	public Insert into(Insertable target);

	public Insert pair(Column column, Expression value);

	public Insert values(Expression... values);

	public Insert values(Source source);
}
