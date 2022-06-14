package org.kernelab.dougong.demo;

import java.util.HashMap;
import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.param.IntParam;
import org.kernelab.dougong.core.dml.param.JSONParam;
import org.kernelab.dougong.core.dml.param.MapParam;
import org.kernelab.dougong.core.dml.param.Name;
import org.kernelab.dougong.core.dml.param.ObjectParam;
import org.kernelab.dougong.core.dml.param.StringParam;
import org.kernelab.dougong.core.meta.AgentMeta;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.AbstractProvidable;

public class DemoDaoAgent
{
	@AgentMeta(DemoDaoImpl.class)
	public static interface DemoDao
	{
		public void selectObject(Company comp);

		public void selectObject(JSON json);

		public void selectObject(Map<String, Object> map);

		public void selectObject(@Name("t") String text, @Name("v") int val);
	}

	public static class DemoDaoImpl extends AbstractProvidable
	{
		private SQL $;

		@Override
		public AbstractProvidable provider(Provider p)
		{
			super.provider(p);
			$ = new SQL(p);
			return this;
		}

		public void selectObject(JSONParam json)
		{
			Tools.debug(json.value());
			Tools.debug(json.$("a") + ": " + json.$("a").value());
			Tools.debug(json.$("b") + ": " + json.$("b").value());
		}

		public void selectObject(MapParam<String, Object> comp)
		{
			Tools.debug(comp.value().size());
			Tools.debug(comp.value().get("a"));
		}

		public void selectObject(ObjectParam<Company> comp)
		{
			Tools.debug(comp.value());
			Tools.debug(comp.$("id"));
		}

		public void selectObject(StringParam text, IntParam val)
		{
			Tools.debug(text.name() + ":" + text.value());
			Tools.debug(val.name() + ":" + val.value());
			COMP c;
			Select s = $.from(c = $.table(COMP.class)) //
					.where(c.COM_NAME.eq(text)) //
					.select($.all());
			Tools.debug(s);
		}
	}

	public static void main(String[] args)
	{
		DemoDao demo = new OracleProvider().provideDao(DemoDao.class);

		demo.selectObject("hey", 1);
		demo.selectObject(new Company());
		demo.selectObject(new HashMap<String, Object>());
		demo.selectObject(new JSON().attr("a", 1));
	}
}
