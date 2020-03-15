package org.kernelab.dougong.semi.ddl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.util.Utils;

public abstract class AbstractForeignKey extends AbstractKey implements ForeignKey
{
	/**
	 * Get a map which contains columns against corresponding values of in the
	 * object.
	 * 
	 * @param object
	 * @param columns
	 * @return
	 */
	public static Map<Column, Object> mapObjectValuesOfColumns(Object object, Column... columns)
	{
		Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass());

		Map<Column, Object> map = new HashMap<Column, Object>();

		for (Column column : columns)
		{
			try
			{
				map.put(column, Tools.access(object, fields.get(Utils.getDataLabelFromField(column.field()))));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return map;
	}

	public static Map<Column, Object> mapSourceToTargetColumns(Map<Column, Object> map, Column[] source,
			Column[] target)
	{
		Map<Column, Object> res = new HashMap<Column, Object>();
		for (int i = 0; i < target.length; i++)
		{
			res.put(target[i], map.get(source[i]));
		}
		return res;
	}

	public static <T> Map<Column, Object> mapValuesToReference(T object, ForeignKey key)
	{
		if (object.getClass().getAnnotation(EntityMeta.class).entity().isInstance(key.reference().entity()))
		{ // object is related to referrer entity
			return mapObjectValuesOfColumns(object, key.reference().columns());
		}
		else
		{ // object is related to reference entity
			return mapSourceToTargetColumns(mapObjectValuesOfColumns(object, key.columns()), key.columns(),
					key.reference().columns());
		}
	}

	public static <T> Map<Column, Object> mapValuesToReferrer(T object, ForeignKey key)
	{
		if (object.getClass().getAnnotation(EntityMeta.class).entity().isInstance(key.entity()))
		{ // object is related to referrer entity
			return mapObjectValuesOfColumns(object, key.columns());
		}
		else
		{ // object is related to reference entity
			return mapSourceToTargetColumns(mapObjectValuesOfColumns(object, key.reference().columns()),
					key.reference().columns(), key.columns());
		}
	}

	private PrimaryKey reference;

	public AbstractForeignKey(PrimaryKey reference, Entity entity, Column... columns)
	{
		super(entity, columns);
		this.reference = reference;
	}

	public ComposableCondition joinCondition()
	{
		Column[] columns = this.columns();

		Column[] refers = this.reference().columns();

		ComposableCondition c = null;

		for (int i = 0; i < columns.length; i++)
		{
			if (c == null)
			{
				c = columns[i].eq(refers[i]);
			}
			else
			{
				c = c.and(columns[i].eq(refers[i]));
			}
		}

		return c;
	}

	public <T> Map<Column, Object> mapValuesToReference(T object)
	{
		return mapValuesToReference(object, this);
	}

	public <T> Map<Column, Object> mapValuesToReferrer(T object)
	{
		return mapValuesToReferrer(object, this);
	}

	public Condition queryCondition()
	{
		Column[] columns = new Column[this.reference().columns().length];
		System.arraycopy(this.columns(), 0, columns, 0, columns.length);
		return queryCondition(columns);
	}

	public PrimaryKey reference()
	{
		return reference;
	}
}
