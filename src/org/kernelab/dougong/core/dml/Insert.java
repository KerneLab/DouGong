package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;

public interface Insert extends DML
{
	public Insert columns(Column... columns);

	public Insert into(Insertable target);

	public Insert values(Expression... values);

	public Insert values(Source source);
}
