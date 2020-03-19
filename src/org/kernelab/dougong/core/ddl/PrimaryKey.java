package org.kernelab.dougong.core.ddl;

import java.util.Map;

import org.kernelab.dougong.core.Column;

public interface PrimaryKey extends DDL, Key
{
	public <T> Map<Column, Object> mapValues(T object);
}
