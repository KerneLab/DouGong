package org.kernelab.dougong.core.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kernelab.basis.Canal;
import org.kernelab.basis.Canal.Tuple2;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Reducer;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.basis.sql.Sequel;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;

public abstract class SimpleCrud
{
	public static int delete(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where)
			throws SQLException
	{
		return kit.update(makeDelete(table, params, where).toString(), params.map());
	}

	public static boolean exists(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where)
			throws SQLException
	{
		return kit.exists(makeExists(table, params, where).toString(), params.map());
	}

	public static int insert(SQLKit kit, Table table, ParamsContext params, Map<Column, Object> pairs)
			throws SQLException
	{
		return kit.update(makeInsert(table, params, pairs).toString(), params.map());
	}

	public static <E> Iterable<E> items(Object... items)
	{
		return Canal.of(items).map(new Mapper<Object, E>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public E map(Object el) throws Exception
			{
				return (E) el;
			}
		});
	}

	public static Condition makeCond(final ParamsContext params, Map<Expression, Object> where)
	{
		return Canal.of(where).map(new Mapper<Tuple2<Expression, Object>, ComposableCondition>()
		{
			@Override
			public ComposableCondition map(Tuple2<Expression, Object> t) throws Exception
			{
				if (t._2 instanceof Expression)
				{
					return t._1.eq((Expression) t._2);
				}
				else
				{
					return t._1.eq(params.nextBy(t._2));
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
	}

	public static Delete makeDelete(Table table, ParamsContext params, Map<Expression, Object> where)
	{
		return table.provider().providePrimitive().from(table).where(makeCond(params, where)).delete();
	}

	public static Select makeExists(Table table, final ParamsContext params, Map<Expression, Object> where)
	{
		Provider p = table.provider();
		Item one = p.provideStringItem("1");
		return p.providePrimitive().from(table).where(makeCond(params, where)).select(one.as("X")).limit(one);
	}

	public static Insert makeInsert(Table table, final ParamsContext params, Map<Column, Object> pairs)
	{
		final Provider p = table.provider();
		return p.provideInsert().into(table).columns(pairs.keySet().toArray(new Column[0]))
				.values(Canal.of(pairs.values()).map(new Mapper<Object, Expression>()
				{
					@Override
					public Expression map(Object el) throws Exception
					{
						if (el instanceof Expression)
						{
							return (Expression) el;
						}
						else
						{
							return params.nextBy(el);
						}
					}
				}).collectAsList().toArray(new Expression[0]));
	}

	public static Select makeSelect(Table table, ParamsContext params, Map<Expression, Object> where,
			Iterable<Expression> select)
	{
		return table.provider().providePrimitive().from(table).where(makeCond(params, where))
				.select(Canal.of(select).collectAsList().toArray(new Expression[0]));
	}

	public static Update makeUpdate(Table table, final ParamsContext params, Map<Expression, Object> where,
			Map<Column, Object> update)
	{
		Update upd = table.provider().providePrimitive().from(table).where(makeCond(params, where)).update();
		for (Entry<Column, Object> pair : update.entrySet())
		{
			if (pair.getValue() instanceof Expression)
			{
				upd.set(pair.getKey(), (Expression) pair.getValue());
			}
			else
			{
				upd.set(pair.getKey(), params.nextBy(pair.getValue()));
			}
		}
		return upd;
	}

	@SuppressWarnings("unchecked")
	public static <K> Map<K, Object> pairs(Object... pairs)
	{
		Map<K, Object> map = new LinkedHashMap<K, Object>();
		for (int i = 0; i < pairs.length - 1; i += 2)
		{
			map.put((K) pairs[i], pairs[i + 1]);
		}
		return map;
	}

	public static ResultSet query(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where,
			Iterable<Expression> select) throws SQLException
	{
		return kit.query(makeSelect(table, params, where, select).toString(), params.map());
	}

	public static Sequel select(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where,
			Iterable<Expression> select) throws SQLException
	{
		return kit.execute(makeSelect(table, params, where, select).toString(), params.map());
	}

	public static int update(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where,
			Map<Column, Object> update) throws SQLException
	{
		return kit.update(makeUpdate(table, params, where, update).toString(), params.map());
	}
}
