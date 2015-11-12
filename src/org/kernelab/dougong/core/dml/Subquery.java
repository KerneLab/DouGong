package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.View;

public interface Subquery extends View, Expression
{
	public Subquery alias(String alias);

	public <T extends Subquery> T as(String alias);

	public Select getSelect();

	public Subquery setSelect(Select select);
}
