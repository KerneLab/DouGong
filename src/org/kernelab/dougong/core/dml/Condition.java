package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Relation;

public interface Condition extends Relation, Providable
{
	public boolean isEmpty();
}
