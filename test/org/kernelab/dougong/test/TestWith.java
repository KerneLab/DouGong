package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.Config;
import org.kernelab.dougong.semi.dml.AbstractSubquery;

public class TestWith
{
	public static void demoWithSelect(SQL sql)
	{
		Select va, vb;
		View q = null;

		Select sel = Config.SQL.with( //
				va = sql.from(sql.view(COMP.class)).select(sql.all()).with("va"), //
				vb = sql.from(va = va.as("a")).select(va.all()).with("vb") //
		).from(q = vb.as("c")) //
				.select(q.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSelectRecursive(SQL sql)
	{
		Select va;
		View vc;
		View q = null;

		Select sel = Config.SQL.with( //
				va = sql.from(vc = sql.self().as("z")).select().select(vc.$("ff").as("gg")).with("va", "hh") //
		).from(q = va.as("c")) //
				.select(q.all(), sql.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSubquery(SQL sql)
	{
		AbstractSubquery va = null;
		AbstractSubquery vb = null;
		View q = null;

		Select sel = Config.SQL.with( //
				va = sql.from(sql.view(COMP.class)).select(sql.all()).to(AbstractSubquery.class).with("va"), //
				vb = sql.from(va).select(va.all()).to(AbstractSubquery.class).with("vb") //
		).from(q = vb.as("c")) //
				.select(q.all());

		Tools.debug(sel.toString());
	}

	public static void main(String[] args)
	{
		SQL sql = Config.SQL;

		// demoWithSubquery(sql);
		// demoWithSelect(sql);
		demoWithSelectRecursive(sql);
	}
}
