package org.kernelab.dougong.semi.dml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractView extends AbstractProvidable implements View
{
	private String				alias	= null;

	private Map<String, Item>	items	= new LinkedHashMap<String, Item>();

	public AbstractView()
	{
		super();
	}

	public String alias()
	{
		return alias;
	}

	public AbstractView alias(String alias)
	{
		this.alias = Tools.notNullOrWhite(alias) ? alias : null;
		return this;
	}

	protected void initColumns()
	{
		int mod = 0;

		Column col = null;

		for (Field field : this.getClass().getFields())
		{
			mod = field.getModifiers();

			try
			{
				if (Tools.isSubClass(field.getType(), Column.class) //
						&& Modifier.isPublic(mod) && !Modifier.isStatic(mod) && !Modifier.isFinal(mod) //
						&& field.get(this) == null)
				{
					col = provider().provideColumn(this, Utils.getNameFromField(field));
					field.set(this, col);
					items().put(col.name(), col);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public Map<String, Item> items()
	{
		return items;
	}

	@Override
	public AbstractView provider(Provider provider)
	{
		super.provider(provider);
		this.initColumns();
		return this;
	}
}
