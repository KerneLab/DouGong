package org.kernelab.dougong.core.ddl;

import java.util.Map;

import org.kernelab.dougong.core.Column;

/**
 * An AbsoluteKey should be an unchangeable unique key which refers to one
 * record row in its table. A typically AbsoluteKey is such column like
 * auto_increment or ROWID.
 * 
 * @author King
 *
 */
public interface AbsoluteKey extends Key
{
	public <T> Map<Column, Object> mapValues(T object);
}
