package org.kernelab.dougong.core.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.DML;
import org.kernelab.dougong.core.dml.cond.BinaryCondition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.cond.TernaryCondition;
import org.kernelab.dougong.semi.dml.cond.AbstractLogicalCondition;

public class AccessGather
{
	public static class Access
	{
		public static final String	TYPE_WHERE	= "WHERE";

		public static final String	TYPE_JOIN	= "JOIN";

		public final DML			dml;

		public final String			type;

		public final Table			table;

		public final Column[]		columns;

		public Access(DML dml, String type, Table table, Column... columns)
		{
			this.dml = dml;
			this.type = type;
			this.table = table;
			this.columns = columns;
		}

		@Override
		public String toString()
		{
			String str = null;
			for (Column col : columns)
			{
				if (str == null)
				{
					str = "(";
				}
				else
				{
					str += ",";
				}
				str += col.name();
			}
			str = type + ":" + table.name() + str + ")";
			return str;
		}
	}

	public static interface Gather
	{
		public void gather(Access access);
	}

	public static Gather GATHER;

	public static void gather(DML dml, String type, Condition cond)
	{
		Gather gather = GATHER;
		if (gather == null)
		{
			return;
		}

		Map<String, List<Column>> map = new HashMap<String, List<Column>>();

		Map<String, Table> dict = new HashMap<String, Table>();

		if (cond instanceof BinaryCondition)
		{
			gather(map, dict, (BinaryCondition) cond);
		}
		else
		{
			if (!(cond instanceof AbstractLogicalCondition))
			{
				return;
			}

			AbstractLogicalCondition conds = (AbstractLogicalCondition) cond;

			for (Object obj : conds.getConditions())
			{
				if (obj instanceof BinaryCondition)
				{
					gather(map, dict, (BinaryCondition) obj);
				}
				else if (obj instanceof ComposableCondition)
				{
					gather(dml, type, (Condition) obj);
				}
			}
		}

		for (Entry<String, List<Column>> entry : map.entrySet())
		{
			gather.gather(new Access(dml, type, dict.get(entry.getKey()),
					entry.getValue().toArray(new Column[entry.getValue().size()])));
		}
	}

	protected static void gather(Map<String, List<Column>> gather, Map<String, Table> dict, BinaryCondition cond)
	{
		if (cond.$_1() instanceof Column)
		{
			gather(gather, dict, (Column) cond.$_1());
		}

		if (!(cond instanceof TernaryCondition))
		{
			if (cond.$_2() instanceof Column)
			{
				gather(gather, dict, (Column) cond.$_2());
			}
		}
	}

	protected static void gather(Map<String, List<Column>> gather, Map<String, Table> dict, Column column)
	{
		if (column.view() instanceof Table)
		{
			Table tab = (Table) column.view();
			String ref = tab.alias() != null ? tab.alias() : tab.name();
			dict.put(ref, tab);
			List<Column> cols = gather.get(ref);
			if (cols == null)
			{
				cols = new LinkedList<Column>();
				gather.put(ref, cols);
			}
			cols.add(column);
		}
	}
}
