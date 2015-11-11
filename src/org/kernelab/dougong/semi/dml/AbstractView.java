package org.kernelab.dougong.semi.dml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;

public abstract class AbstractView extends AbstractProvidable implements View
{
	private String	alias	= null;

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

		for (Field field : this.getClass().getFields())
		{
			mod = field.getModifiers();

			try
			{
				if (Tools.isSubClass(field.getType(), Column.class) //
						&& Modifier.isPublic(mod) && !Modifier.isStatic(mod) && !Modifier.isFinal(mod) //
						&& field.get(this) == null)
				{
					field.set(this, provider().provideColumn(this, field.getName()));
				}
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public AbstractView provider(Provider provider)
	{
		super.provider(provider);
		this.initColumns();
		return this;
	}
}
