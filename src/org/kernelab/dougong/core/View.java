package org.kernelab.dougong.core;

import java.util.Map;

/**
 * The interface which could be selected from or joined.
 */
public interface View extends Text, Alias, Providable
{
	public View alias(String alias);

	/**
	 * Return the <name, Column> map.
	 * 
	 * @return
	 */
	public Map<String, Column> columns();
}
