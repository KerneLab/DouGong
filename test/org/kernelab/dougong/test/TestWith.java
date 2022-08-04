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
	public static SQL $ = Config.SQL;

	public static void demoWithSelect(SQL sql)
	{
		Select va, vb;
		View q = null;

		Select sel = $.withs( //
				$.with("va").as(va = sql.from(sql.view(COMP.class).as("T")).select(sql.all())), //
				$.with("vb").as(vb = sql.from(va = va.as("a")).select(va.all())) //
		).from(q = vb.as("c")) //
				.select(q.all(), sql.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSelectNoAlias(SQL sql)
	{
		Select va, vb;

		Select sel = $.withs( //
				$.with("va").as(va = sql.from(sql.view(COMP.class).as("T")).select(sql.all())), //
				$.with("vb").as(vb = sql.from(va = va.as("a")).select(va.all())) //
		).from(vb) //
				.select(vb.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSelectRecursive(SQL sql)
	{
		Select va;
		View vc;
		View q = null;

		Select sel = $.withs( //
				$.with("va", "hh").as(va = sql.from(vc = sql.self().as("z")).select().select(vc.$("ff").as("gg"))) //
		).from(q = va.as("c")) //
				.select(q.$("hh"), q.all(), sql.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSelectRecursive1(SQL sql)
	{
		Select va;
		View vc;
		View q = null;

		Select sel = $.withs( //
				$.with("va").as(va = sql.from(vc = sql.self().as("z")).select().select(vc.$("ff").as("gg"))) //
		).from(q = va.as("c")) //
				.select(q.$("gg"), q.all(), sql.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSubquery(SQL sql)
	{
		AbstractSubquery va = null;
		AbstractSubquery vb = null;
		View q = null;

		Select sel = $.withs( //
				$.with("va").as(va = sql.from(sql.view(COMP.class).as("T")) //
						.select(sql.all()).to(AbstractSubquery.class)),
				$.with("vb").as(vb = sql.from(va).select(va.$("COMP_ID")) //
						.to(AbstractSubquery.class)) //
		).from(q = vb.as("c")) //
				.select(q.$("COMP_ID"), q.all(), sql.all());

		Tools.debug(sel.toString());
	}

	public static void main(String[] args)
	{
		SQL sql = Config.SQL;

		demoWithSubquery(sql);
		demoWithSelect(sql);
		demoWithSelectNoAlias(sql);
		demoWithSelectRecursive(sql);
		demoWithSelectRecursive1(sql);
	}
}
