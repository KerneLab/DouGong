package org.kernelab.dougong.orcl.util;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Pair;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.core.util.KeysFetcher;

public class OracleKeysFetcher implements KeysFetcher
{
	public static void main(String[] args)
	{
		Tools.debug(new OracleKeysFetcher().SQL_QUERY_KEY_WITH_SCHEMA());
		Tools.debug(new OracleKeysFetcher().SQL_QUERY_KEY_WITHOUT_SCHEMA());
	}

	public Map<Pair<String, String>, List<String>> foreignKeys(SQLKit kit, String table, String schema) throws Exception
	{
		String sql = Tools.isNullOrEmpty(schema) ? SQL_QUERY_KEY_WITHOUT_SCHEMA() : SQL_QUERY_KEY_WITH_SCHEMA();

		Map<Pair<String, String>, List<String>> keys = new HashMap<Pair<String, String>, List<String>>();

		String name = null;

		List<String> cols = null;

		for (ResultSet rs : kit.execute(sql, new JSON() //
				.attr("schema", schema) //
				.attr("table", table) //
				.attr("type", "R")))
		{
			if (!Tools.equals(rs.getString(1), name))
			{
				name = rs.getString(1);
				cols = new LinkedList<String>();
				keys.put(new Pair<String, String>(rs.getString(2), name), cols);
			}
			cols.add(rs.getString(3));
		}

		return keys;
	}

	public Map<String, Integer> primaryKey(SQLKit kit, String table, String schema) throws Exception
	{
		String sql = Tools.isNullOrEmpty(schema) ? SQL_QUERY_KEY_WITHOUT_SCHEMA() : SQL_QUERY_KEY_WITH_SCHEMA();

		Map<String, Integer> key = new HashMap<String, Integer>();

		for (ResultSet rs : kit.execute(sql, new JSON() //
				.attr("schema", schema) //
				.attr("table", table) //
				.attr("type", "P")))
		{
			key.put(rs.getString(3), rs.getInt(4));
		}

		return key;
	}

	protected String SQL_QUERY_KEY_WITH_SCHEMA()
	{
		return "SELECT CONSTRAINT_NAME, T.REF_TABLE, C.COLUMN_NAME, C.POSITION" //
				+ "  FROM (SELECT T.OWNER," //
				+ "               T.TABLE_NAME," //
				+ "               T.CONSTRAINT_NAME," //
				+ "               R.TABLE_NAME REF_TABLE" //
				+ "          FROM ALL_CONSTRAINTS T" //
				+ "          LEFT JOIN ALL_CONSTRAINTS R" //
				+ "            ON T.R_OWNER = R.OWNER" //
				+ "           AND T.R_CONSTRAINT_NAME = R.CONSTRAINT_NAME" //
				+ "         WHERE T.OWNER = ?schema?" //
				+ "           AND T.TABLE_NAME = ?table?" //
				+ "           AND T.CONSTRAINT_TYPE = ?type?) T" //
				+ " INNER JOIN ALL_CONS_COLUMNS C" //
				+ " USING (OWNER, TABLE_NAME, CONSTRAINT_NAME)" //
				+ " ORDER BY CONSTRAINT_NAME, C.POSITION";
	}

	protected String SQL_QUERY_KEY_WITHOUT_SCHEMA()
	{
		return "SELECT CONSTRAINT_NAME, T.REF_TABLE, C.COLUMN_NAME, C.POSITION" //
				+ "  FROM (SELECT T.TABLE_NAME, T.CONSTRAINT_NAME, R.TABLE_NAME REF_TABLE" //
				+ "          FROM USER_CONSTRAINTS T" //
				+ "          LEFT JOIN USER_CONSTRAINTS R" //
				+ "            ON T.R_CONSTRAINT_NAME = R.CONSTRAINT_NAME" //
				+ "         WHERE T.TABLE_NAME = ?table?" //
				+ "           AND T.CONSTRAINT_TYPE = ?type?) T" //
				+ " INNER JOIN USER_CONS_COLUMNS C" //
				+ " USING (TABLE_NAME, CONSTRAINT_NAME)" //
				+ " ORDER BY CONSTRAINT_NAME, C.POSITION";
	}
}
