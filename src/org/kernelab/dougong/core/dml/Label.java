package org.kernelab.dougong.core.dml;

public interface Label
{
	/**
	 * Get the label of item.<br />
	 * The label should be alias name if specified. Otherwise, its raw name
	 * should be returned.
	 * 
	 * @return
	 */
	public String label();
}
