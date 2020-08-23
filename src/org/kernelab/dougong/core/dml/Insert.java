package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;

public interface Insert extends DML, Hintable
{
	public Insert columns(Column... columns);

	public Insert into(Insertable target);

	public Insert values(Expression... values);

	public Insert values(Source source);

	public Insert hint(String hint);
}
