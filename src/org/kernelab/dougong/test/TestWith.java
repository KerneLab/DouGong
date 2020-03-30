package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.Config;

public class TestWith
{

	public static void main(String[] args)
	{
		SQL sql = Config.SQL;

		Subquery query = sql.from(sql.view(COMP.class)).select(sql.all()).toSubquery(null);

		View q = null;

		Select sel = Config.SQL.with(query = query.as("vc")) //
				.from(q = query.as("c")) //
				.select(q.all());

		Tools.debug(sel.toString());
	}

}
