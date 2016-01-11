package org.kernelab.dougong.core;

import java.util.Map;

import org.kernelab.dougong.core.dml.AllColumns;

/**
 * The interface in whose sub classes Column members would be defined.
 */
public interface Entity
{
	public AllColumns all();

	/**
	 * Return the <name, Column> map.
	 * 
	 * @return
	 */
	public Map<String, Column> columns();
}
