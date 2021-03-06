package org.kernelab.dougong.semi.ddl;

import java.util.Map;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.meta.EntityMeta;

public abstract class AbstractForeignKey extends AbstractKey implements ForeignKey
{
	public static <T> Map<Column, Object> mapValues(T object, ForeignKey key, boolean toReference)
	{
		if (toReference)
		{
			return mapValuesToReference(object, key);
		}
		else
		{
			return mapValuesToReferrer(object, key);
		}
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

	public boolean inPrimaryKey()
	{
		PrimaryKey pk = this.entity().primaryKey();
		return pk != null && pk.contains(this.columns());
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

	public <T> Map<Column, Object> mapValuesTo(T object, Entity entity)
	{
		return entity() == entity ? mapValuesToReferrer(object) : mapValuesToReference(object);
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
