package org.kernelab.dougong.core.util;

import java.util.List;
import java.util.Map;

import org.kernelab.basis.Pair;
import org.kernelab.basis.sql.SQLKit;

public interface KeysFetcher
{
	/**
	 * Return {@code Pair<RefTable,KeyName> -> List<Columns>}
	 * 
	 * @param kit
	 * @param table
	 * @param schema
	 * @return
	 * @throws Exception
	 */
	public Map<Pair<String, String>, List<String>> foreignKeys(SQLKit kit, String table, String schema)
			throws Exception;

	public Map<String, Integer> primaryKey(SQLKit kit, String table, String schema) throws Exception;
}
