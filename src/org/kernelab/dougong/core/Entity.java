package org.kernelab.dougong.core;

import java.util.Map;

/**
 * The interface in whose sub classes Column members would be defined.
 */
public interface Entity
{
	/**
	 * Return the <name, Column> map.
	 * 
	 * @return
	 */
	public Map<String, Column> columns();
}
