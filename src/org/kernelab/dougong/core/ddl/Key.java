package org.kernelab.dougong.core.ddl;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;

public interface Key
{
	public Column[] columns();

	public View view();
}
