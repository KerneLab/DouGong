package org.kernelab.dougong.semi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
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

	public Delete delete()
	{
		return this.provider().provideDelete().from(this);
	}

	protected Column getColumnByField(Field field)
	{
		String name = Utils.getNameFromField(field);

		Column column = Tools.as(items().get(name), Column.class);

		if (column == null)
		{
			column = provider().provideColumn(this, name, field);
		}

		return column;
	}

	protected List<Field> getColumnFields()
	{
		List<Field> fields = new LinkedList<Field>();

		int mod = 0;

		for (Field field : this.getClass().getFields())
		{
			mod = field.getModifiers();

			try
			{
				if (Tools.isSubClass(field.getType(), Column.class) //
						&& Modifier.isPublic(mod) //
						&& !Modifier.isStatic(mod) //
						&& !Modifier.isFinal(mod))
				{
					fields.add(field);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return fields;
	}

	protected void initColumns()
	{
		Column col = null;

		for (Field field : this.getColumnFields())
		{
			try
			{
				if (field.get(this) == null)
				{
					col = this.getColumnByField(field);
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

	public Insert insert()
	{
		return this.provider().provideInsert().into((Insertable) this);
	}

	public Item item(String refer)
	{
		return items().get(refer);
	}

	public Map<String, Item> items()
	{
		return items;
	}

	public PrimaryKey primaryKey()
	{
		TreeMap<Integer, Column> keys = new TreeMap<Integer, Column>();

		for (Field field : this.getColumnFields())
		{
			PrimaryKeyMeta meta = field.getAnnotation(PrimaryKeyMeta.class);
			if (meta != null)
			{
				keys.put(meta.ordinal(), this.getColumnByField(field));
			}
		}

		if (keys.isEmpty())
		{
			return null;
		}
		else
		{
			return provider().providePrimaryKey(this, keys.values().toArray(new Column[keys.size()]));
		}
	}

	@Override
	public AbstractView provider(Provider provider)
	{
		super.provider(provider);
		this.initColumns();
		return this;
	}

	public Select select()
	{
		return this.provider().provideSelect().from(this);
	}

	public Update update()
	{
		return this.provider().provideUpdate().from(this);
	}
}
