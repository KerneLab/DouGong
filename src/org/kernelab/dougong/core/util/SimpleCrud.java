package org.kernelab.dougong.core.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;

public abstract class SimpleCrud
{
	public static int delete(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where)
			throws SQLException
	{
		return kit.update(makeDelete(table, params, where), params.map());
	}

	public static boolean exists(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where)
			throws SQLException
	{
		return kit.exists(makeExists(table, params, where), params.map());
	}

	public static int insert(SQLKit kit, Table table, ParamsContext params, Map<Column, Object> pairs)
			throws SQLException
	{
		return kit.update(makeInsert(table, params, pairs), params.map());
	}

	protected static String makeCond(final ParamsContext params, Map<Expression, Object> where)
	{
		return Canal.of(where).map(new Mapper<Tuple2<Expression, Object>, String>()
		{
			@Override
			public String map(Tuple2<Expression, Object> t) throws Exception
			{
				StringBuilder b = new StringBuilder();
				t._1.toString(b);
				b.append('=');
				if (t._2 instanceof Expression)
				{
					((Expression) t._2).toStringExpress(b);
				}
				else
				{
					b.append('?').append(params.nextBy(t._2).name()).append('?');
				}
				return b.toString();
			}
		}).toString(" AND ");
	}

	protected static String makeDelete(Table table, ParamsContext params, Map<Expression, Object> where)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		table.toStringDeletable(sql);
		sql.append(" WHERE ");
		sql.append(makeCond(params, where));
		return sql.toString();
	}

	protected static String makeExists(Table table, final ParamsContext params, Map<Expression, Object> where)
	{
		Condition cond = Canal.of(where).map(new Mapper<Tuple2<Expression, Object>, ComposableCondition>()
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

		Provider p = table.provider();

		Item one = p.provideStringItem("1");

		return p.providePrimitive().from(table).where(cond).select(one.as("X")).limit(one).toString();
	}

	protected static String makeInsert(Table table, final ParamsContext params, Map<Column, Object> pairs)
	{
		final Provider p = table.provider();

		StringBuilder[] buf = Canal.of(pairs).fold(new StringBuilder[] { new StringBuilder(), new StringBuilder() },
				new Reducer<Tuple2<Column, Object>, StringBuilder[]>()
				{
					@Override
					public StringBuilder[] reduce(StringBuilder[] res, Tuple2<Column, Object> dat) throws Exception
					{
						if (res[0].length() > 0)
						{
							res[0].append(',');
						}
						p.provideOutputColumnInsert(res[0], dat._1);

						if (res[1].length() > 0)
						{
							res[1].append(',');
						}
						if (dat._2 instanceof Expression)
						{
							((Expression) dat._2).toStringExpress(res[1]);
						}
						else
						{
							res[1].append('?').append(params.nextBy(dat._2).name()).append('?');
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

	protected static String makeSelect(Table table, ParamsContext params, Map<Expression, Object> where,
			Iterable<Expression> select)
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
		sql.append(makeCond(params, where));
		return sql.toString();
	}

	protected static String makeUpdate(Table table, final ParamsContext params, Map<Expression, Object> where,
			Map<Column, Object> update)
	{
		String set = Canal.of(update).map(new Mapper<Tuple2<Column, Object>, String>()
		{
			@Override
			public String map(Tuple2<Column, Object> t) throws Exception
			{
				StringBuilder b = new StringBuilder();
				t._1.toString(b);
				b.append('=');
				if (t._2 instanceof Expression)
				{
					((Expression) t._2).toStringExpress(b);
				}
				else
				{
					b.append('?').append(params.nextBy(t._2).name()).append('?');
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
		sql.append(makeCond(params, where));
		return sql.toString();
	}

	public static ResultSet query(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where,
			Iterable<Expression> select) throws SQLException
	{
		return kit.query(makeSelect(table, params, where, select), params.map());
	}

	public static Sequel select(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where,
			Iterable<Expression> select) throws SQLException
	{
		return kit.execute(makeSelect(table, params, where, select), params.map());
	}

	public static int update(SQLKit kit, Table table, ParamsContext params, Map<Expression, Object> where,
			Map<Column, Object> update) throws SQLException
	{
		return kit.update(makeUpdate(table, params, where, update), params.map());
	}
}
