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

	public static void demoWithSelect()
	{
		Select va, vb;
		View q = null;

		Select sel = $.with( //
				$.with("va").as(va = $.from($.view(COMP.class).as("T")).select($.all())), //
				$.with("vb").as(vb = $.from(va = va.as("a")).select(va.all())) //
		).from(q = vb.as("c")) //
				.select(q.all(), $.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSelectNoAlias()
	{
		Select va, vb;

		Select sel = $.with( //
				$.with("va").as(va = $.from($.view(COMP.class).as("T")).select($.all())), //
				$.with("vb").as(vb = $.from(va = va.as("a")).select(va.all())) //
		).from(vb) //
				.select(vb.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSelectRecursive()
	{
		Select va;
		View vc;
		View q = null;

		Select sel = $.withRecursive( //
				$.with("va", "hh").as(va = $.from(vc = $.self().as("z")).select() //
						.select(vc.$("ff").as("gg"))) //
		).from(q = va.as("c")) //
				.select(q.$("hh"), q.all(), $.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSelectRecursive1()
	{
		Select va;
		View vc;
		View q = null;

		Select sel = $.withRecursive( //
				$.with("va").as(va = $.from(vc = $.self().as("z")).select() //
						.select(vc.$("ff").as("gg"))) //
		).from(q = va.as("c")) //
				.select(q.$("gg"), q.all(), $.all());

		Tools.debug(sel.toString());
	}

	public static void demoWithSubquery()
	{
		AbstractSubquery va = null;
		AbstractSubquery vb = null;
		View q = null;

		Select sel = $.with( //
				$.with("va").as(va = $.from($.view(COMP.class).as("T")) //
						.select($.all()).to(AbstractSubquery.class)),
				$.with("vb").as(vb = $.from(va).select(va.$("COMP_ID")) //
						.to(AbstractSubquery.class)) //
		).from(q = vb.as("c")) //
				.select(q.$("COMP_ID"), q.all(), $.all());

		Tools.debug(sel.toString());
	}

	public static void main(String[] args)
	{
		demoWithSubquery();
		demoWithSelect();
		demoWithSelectNoAlias();
		demoWithSelectRecursive();
		demoWithSelectRecursive1();
	}
}
