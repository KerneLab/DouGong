package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.orcl.OracleProvider;

public class TestPredeclaredView
{

	public static void main(String[] args)
	{
		SQL sql = new SQL(new OracleProvider());
		Subquery view = sql.subquery(TestView.class);

		Select sel = sql.from(view.as((String) null)).select(sql.all());

		Tools.debug(sel.toString());

	}

}
