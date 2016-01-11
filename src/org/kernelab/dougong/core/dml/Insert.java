package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;

public interface Insert extends DML
{
	public Insert into(Insertable target, Column... columns);

	public Insert values(Expression... values);

	public Insert values(Source source);
}
