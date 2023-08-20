package org.kernelab.dougong.semi.ddl;

import java.util.Map;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.ForeignKeyMeta;

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

	private PrimaryKey	reference;

	private byte		onDelete	= ForeignKeyMeta.RESTRICT;

	private byte		onUpdate	= ForeignKeyMeta.RESTRICT;

	public AbstractForeignKey(PrimaryKey reference, Entity entity, Column... columns)
	{
		super(entity, columns);
		this.reference = reference;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
		{
			return false;
		}

		if (!(obj instanceof ForeignKey))
		{
			return false;
		}

		return equals(this.reference(), ((ForeignKey) obj).reference());
	}

	@Override
	public int hashCode()
	{
		return super.hashCode() * 31 + hashCode(reference());
	}

	@Override
	public boolean inPrimaryKey()
	{
		PrimaryKey pk = this.entity().primaryKey();
		return pk != null && pk.containsAll(this.columns());
	}

	@Override
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

	@Override
	public <T> Map<Column, Object> mapValuesTo(T object, Entity entity)
	{
		return entity() == entity ? mapValuesToReferrer(object) : mapValuesToReference(object);
	}

	@Override
	public <T> Map<Column, Object> mapValuesToReference(T object)
	{
		return mapValuesToReference(object, this);
	}

	@Override
	public <T> Map<Column, Object> mapValuesToReferrer(T object)
	{
		return mapValuesToReferrer(object, this);
	}

	@Override
	public byte onDelete()
	{
		return onDelete;
	}

	public AbstractForeignKey onDelete(byte onDelete)
	{
		this.onDelete = onDelete;
		return this;
	}

	@Override
	public byte onUpdate()
	{
		return onUpdate;
	}

	public AbstractForeignKey onUpdate(byte onUpdate)
	{
		this.onUpdate = onUpdate;
		return this;
	}

	@Override
	public Condition queryCondition()
	{
		Column[] columns = new Column[this.reference().columns().length];
		System.arraycopy(this.columns(), 0, columns, 0, columns.length);
		return queryCondition(columns);
	}

	@Override
	public PrimaryKey reference()
	{
		return reference;
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		buffer.append(entity().toString() + "(");

		for (int i = 0; i < columns().length; i++)
		{
			if (i > 0)
			{
				buffer.append(",");
			}
			buffer.append(columns()[i].toString());
		}

		buffer.append(") -> " + reference().entity() + "(");

		for (int i = 0; i < reference().columns().length; i++)
		{
			if (i > 0)
			{
				buffer.append(",");
			}
			buffer.append(reference().columns()[i].toString());
		}

		buffer.append(")");

		return buffer;
	}
}
