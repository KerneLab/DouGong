package org.kernelab.dougong.demo;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;

public class DemoDDL
{
	public static SQL $ = Config.SQL;

	public static void main(String[] args)
	{
		Tools.debug($.provider().provideDropTable().table($.table(COMP.class)));
		Tools.debug($.provider().provideCreateTable().table($.table(COMP.class)).toString());
	}
}
