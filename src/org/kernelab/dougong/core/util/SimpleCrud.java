package org.kernelab.dougong.core.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.kernelab.basis.Canal;
import org.kernelab.basis.Canal.PairCanal;
import org.kernelab.basis.Canal.Tuple;
import org.kernelab.basis.Canal.Tuple2;
import org.kernelab.basis.Filter;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Reducer;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.basis.sql.Sequel;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;

public abstract class SimpleCrud
{
	protected static Map<String, Object> args(Collection<Object> values)
	{
		return Canal.of(values).zipWithIndex().filter(new Filter<Tuple2<Object, Integer>>()
		{
			@Override
			public boolean filter(Tuple2<Object, Integer> t) throws Exception
			{
				return !(t._1 instanceof Expression);
			}
		}).mapToPair(new Mapper<Tuple2<Object, Integer>, Tuple2<String, Object>>()
		{
			@Override
			public Tuple2<String, Object> map(Tuple2<Object, Integer> t) throws Exception
			{
				return Tuple.of("p" + t._2, t._1);
			}
		}).collectAsMap();
	}

	protected static Map<String, Object> concat(Map<String, Object>... maps)
	{
		PairCanal<String, Object> c = null;

		for (Map<String, Object> map : maps)
		{
			if (c == null)
			{
				c = Canal.of(map);
			}
			else
			{
				c = c.union(Canal.of(map));
			}
		}

		return c.collectAsMap();
	}

	public static int delete(SQLKit kit, Table table, Map<Expression, Object> where) throws SQLException
	{
		return kit.update(makeDelete(table, where), args(where.values()));
	}

	public static boolean exists(SQLKit kit, Table table, Map<Expression, Object> where) throws SQLException
	{
		return kit.exists(makeExists(table, where), args(where.values()));
	}

	public static int insert(SQLKit kit, Table table, Map<Column, Object> pairs) throws SQLException
	{
		return kit.update(makeInsert(table, pairs), args(pairs.values()));
	}

	protected static String makeCond(Map<Expression, Object> where)
	{
		return Canal.of(where).zipWithIndex().map(new Mapper<Tuple2<Tuple2<Expression, Object>, Integer>, String>()
		{
			@Override
			public String map(Tuple2<Tuple2<Expression, Object>, Integer> t) throws Exception
			{
				StringBuilder b = new StringBuilder();
				t._1._1.toString(b);
				b.append('=');
				if (t._1._2 instanceof Expression)
				{
					((Expression) t._1._2).toStringExpress(b);
				}
				else
				{
					b.append("?p").append(t._2).append('?');
				}
				return b.toString();
			}
		}).toString(" AND ");
	}

	protected static String makeDelete(Table table, Map<Expression, Object> where)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		table.toStringDeletable(sql);
		sql.append(" WHERE ");
		sql.append(makeCond(where));
		return sql.toString();
	}

	protected static String makeExists(Table table, Map<Expression, Object> where)
	{
		final Provider p = table.provider();

		Condition cond = Canal.of(where).zipWithIndex()
				.map(new Mapper<Tuple2<Tuple2<Expression, Object>, Integer>, ComposableCondition>()
				{
					@Override
					public ComposableCondition map(Tuple2<Tuple2<Expression, Object>, Integer> t) throws Exception
					{
						if (t._1._2 instanceof Expression)
						{
							return t._1._1.eq((Expression) t._1._2);
						}
						else
						{
							return t._1._1.eq(p.provideParameter("p" + t._2));
						}
					}
				}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
				{
					@Override
					public ComposableCondition reduce(ComposableCondition a, ComposableCondition b) throws Exception
					{
						return a.and(b);
					}
				}).get();

		Item one = p.provideStringItem("1");

		return p.providePrimitive().from(table).where(cond).select(one.as("X")).limit(one).toString();
	}

	protected static String makeInsert(Table table, Map<Column, Object> pairs)
	{
		final Provider p = table.provider();

		StringBuilder[] buf = Canal.of(pairs).zipWithIndex().fold(
				new StringBuilder[] { new StringBuilder(), new StringBuilder() },
				new Reducer<Tuple2<Tuple2<Column, Object>, Integer>, StringBuilder[]>()
				{
					@Override
					public StringBuilder[] reduce(StringBuilder[] res, Tuple2<Tuple2<Column, Object>, Integer> dat)
							throws Exception
					{
						if (res[0].length() > 0)
						{
							res[0].append(',');
						}
						p.provideOutputColumnInsert(res[0], dat._1._1);

						if (res[1].length() > 0)
						{
							res[1].append(',');
						}
						if (dat._1._2 instanceof Expression)
						{
							((Expression) dat._1._2).toStringExpress(res[1]);
						}
						else
						{
							res[1].append("?p").append(dat._2).append('?');
						}

						return res;
					}
				});

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		table.toStringInsertable(sql);
		sql.append(" (");
		sql.append(buf[0]);
		sql.append(") VALUES (");
		sql.append(buf[1]);
		sql.append(")");
		return sql.toString();
	}

	protected static String makeSelect(Table table, Map<Expression, Object> where, Iterable<Expression> select)
	{
		String sel = Canal.of(select).map(new Mapper<Expression, String>()
		{
			@Override
			public String map(Expression c) throws Exception
			{
				StringBuilder b = new StringBuilder();
				c.toStringSelected(b);
				return b.toString();
			}
		}).toString(",");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(sel);
		sql.append(" FROM ");
		table.toStringViewed(sql);
		sql.append(" WHERE ");
		sql.append(makeCond(where));
		return sql.toString();
	}

	protected static String makeUpdate(Table table, Map<Expression, Object> where, Map<Column, Object> update)
	{
		String set = Canal.of(update).zipWithIndex().map(new Mapper<Tuple2<Tuple2<Column, Object>, Integer>, String>()
		{
			@Override
			public String map(Tuple2<Tuple2<Column, Object>, Integer> t) throws Exception
			{
				StringBuilder b = new StringBuilder();
				t._1._1.toString(b);
				b.append('=');
				if (t._1._2 instanceof Expression)
				{
					((Expression) t._1._2).toStringExpress(b);
				}
				else
				{
					b.append("?p").append(t._2).append('?');
				}
				return b.toString();
			}
		}).toString(",");

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		table.toStringUpdatable(sql);
		sql.append(" SET ");
		sql.append(set);
		sql.append(" WHERE ");
		sql.append(makeCond(where));
		return sql.toString();
	}

	public static Map<Expression, Object> pairs(Object... pairs)
	{
		Map<Expression, Object> map = new LinkedHashMap<Expression, Object>();

		for (int i = 0; i < pairs.length - 1; i += 2)
		{
			map.put((Expression) pairs[i], pairs[i + 1]);
		}

		return map;
	}

	public static ResultSet query(SQLKit kit, Table table, Map<Expression, Object> where, Iterable<Expression> select)
			throws SQLException
	{
		return kit.query(makeSelect(table, where, select), args(where.values()));
	}

	public static Sequel select(SQLKit kit, Table table, Map<Expression, Object> where, Iterable<Expression> select)
			throws SQLException
	{
		return kit.execute(makeSelect(table, where, select), args(where.values()));
	}

	@SuppressWarnings("unchecked")
	public static int update(SQLKit kit, Table table, Map<Expression, Object> where, Map<Column, Object> update)
			throws SQLException
	{
		return kit.update(makeUpdate(table, where, update), concat(args(where.values()), args(update.values())));
	}
}
