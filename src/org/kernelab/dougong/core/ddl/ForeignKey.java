package org.kernelab.dougong.core.ddl;

import java.util.Map;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.dml.Condition;

public interface ForeignKey extends DDL, Key
{
	/**
	 * To judge whether the columns in this foreign key is contained in the
	 * entity's primary key.
	 * 
	 * @return
	 */
	public boolean inPrimaryKey();

	/**
	 * Get the join condition between the referrer and reference side entity of
	 * this foreign key.
	 * 
	 * @return
	 */
	public Condition joinCondition();

	/**
	 * Map the values of given object to the columns on the entity side of this
	 * foreign key.
	 * 
	 * @param object
	 *            The object containing the values to be mapped.
	 * @param entity
	 *            The entity to choose which side of this foreign key to be map.
	 * @return
	 */
	public <T> Map<Column, Object> mapValuesTo(T object, Entity entity);

	/**
	 * Map the values of given object to the columns in reference side entity of
	 * this foreign key.
	 * 
	 * @param object
	 * @return
	 */
	public <T> Map<Column, Object> mapValuesToReference(T object);

	/**
	 * Map the values of given object to the columns in referrer side of this
	 * foreign key.
	 * 
	 * @param object
	 * @return
	 */
	public <T> Map<Column, Object> mapValuesToReferrer(T object);

	/**
	 * Get the reference primary key.
	 * 
	 * @return
	 */
	public PrimaryKey reference();
}
