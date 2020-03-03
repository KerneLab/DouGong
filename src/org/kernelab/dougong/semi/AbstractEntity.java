package org.kernelab.dougong.semi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.meta.PrimaryKeyMeta;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractEntity extends AbstractView implements Entity
{
	protected Column getColumnByField(Field field)
	{
		String name = Utils.getNameFromField(field);

		Column column = Tools.as(itemsMap().get(name), Column.class);

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
					items().add(col);
					itemsMap().put(col.name(), col);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
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

	protected ForeignKey foreignKey(Entity ref, Column... columns)
	{
		PrimaryKey pk = ref.primaryKey();

		if (pk == null)
		{
			return null;
		}
		else
		{
			return this.provider().provideForeignKey(pk, this, columns);
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
