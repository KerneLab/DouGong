package org.kernelab.dougong.core.meta;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.kernelab.basis.Canal;
import org.kernelab.basis.Canal.Action;
import org.kernelab.basis.Canal.Tuple;
import org.kernelab.basis.Canal.Tuple2;
import org.kernelab.basis.Canal.Tuple3;
import org.kernelab.basis.Filter;
import org.kernelab.basis.HashedEquality;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Pair;
import org.kernelab.basis.Reducer;
import org.kernelab.basis.Relation;
import org.kernelab.basis.Tools;
import org.kernelab.basis.WrappedHashMap;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Scope;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractColumn;
import org.kernelab.dougong.semi.ddl.AbstractForeignKey;
import org.kernelab.dougong.semi.dml.AbstractDelete;
import org.kernelab.dougong.semi.dml.AbstractInsert;
import org.kernelab.dougong.semi.dml.AbstractUpdate;

public class MetaContext
{
	public static final HashedEquality<Column>		COLUMN_EQUAL	= new HashedEquality<Column>()
																	{
																		@Override
																		public boolean equals(Column a, Column b)
																		{
																			return AbstractColumn.equals(a, b);
																		}

																		@Override
																		public int hashCode(Column c)
																		{
																			return AbstractColumn.hashCode(c);
																		}
																	};

	public static final HashedEquality<Table>		TABLE_EQUAL		= new HashedEquality<Table>()
																	{
																		@Override
																		public boolean equals(Table a, Table b)
																		{
																			return a.getClass() == b.getClass();
																		}

																		@Override
																		public int hashCode(Table c)
																		{
																			return c.getClass().hashCode();
																		}
																	};

	protected static final ReentrantReadWriteLock	CONTEXT_LOCK	= new ReentrantReadWriteLock(true);

	protected static MetaContext					CONTEXT			= null;

	public static void check(SQLKit kit, Delete delete, Map<String, Object> params) throws SQLException
	{
		if (delete == null || params == null)
		{
			return;
		}
		MetaContext meta = getContext();
		if (meta == null)
		{
			return;
		}
		for (Tuple3<ForeignKey, String, Boolean> check : meta.checkForeignKey(delete))
		{
			check(kit, params, check);
		}
	}

	public static void check(SQLKit kit, Insert insert, Map<String, Object> params) throws SQLException
	{
		if (insert == null || params == null)
		{
			return;
		}
		MetaContext meta = getContext();
		if (meta == null)
		{
			return;
		}
		for (Tuple3<ForeignKey, String, Boolean> check : meta.checkForeignKey(insert))
		{
			check(kit, params, check);
		}
	}

	public static void check(SQLKit kit, Map<String, Object> params, ForeignKey key, String select, boolean referrer)
			throws SQLException
	{
		if (kit.exists(select, params))
		{
			if (referrer)
			{
				throw ForeignKeyException.missingReference(key, params);
			}
			else
			{
				throw ForeignKeyException.existingReference(key, params);
			}
		}
	}

	public static void check(SQLKit kit, Map<String, Object> params, Tuple3<ForeignKey, String, Boolean> check)
			throws SQLException
	{
		check(kit, params, check._1, check._2, check._3);
	}

	public static void check(SQLKit kit, Update update, Map<String, Object> params) throws SQLException
	{
		if (update == null || params == null)
		{
			return;
		}
		MetaContext meta = getContext();
		if (meta == null)
		{
			return;
		}
		for (Tuple3<ForeignKey, String, Boolean> check : meta.checkForeignKey(update))
		{
			check(kit, params, check);
		}
	}

	protected static void collectForeignKeyColumns(Map<Column, Set<ForeignKey>> colsmap, ForeignKey key, Column[] cols)
	{
		Set<ForeignKey> keyset = null;
		for (Column c : cols)
		{
			keyset = colsmap.get(c);
			if (keyset == null)
			{
				keyset = new HashSet<ForeignKey>();
				colsmap.put(c, keyset);
			}
			keyset.add(key);
		}
	}

	@SuppressWarnings("unchecked")
	public static void collectForeignKeyRelations(Map<Class<? extends Table>, Set<Method>> res,
			Class<? extends Table> referrer)
	{
		Class<?>[] params = null;
		Class<? extends Table> reference = null;
		Set<Method> set = null;

		for (Method m : referrer.getMethods())
		{
			if (!Modifier.isStatic(m.getModifiers()) && m.getAnnotation(ForeignKeyMeta.class) != null
					&& Tools.isSubClass(m.getReturnType(), ForeignKey.class)
					&& (params = m.getParameterTypes()).length == 1)
			{
				try
				{
					reference = (Class<? extends Table>) params[0];
				}
				catch (Exception e)
				{
					continue;
				}

				set = res.get(reference);
				if (set == null)
				{
					set = new HashSet<Method>();
					res.put(reference, set);
				}

				set.add(m);
			}
		}
	}

	protected static void collectForeignKeyTable(Map<Table, Set<ForeignKey>> res, Table table, ForeignKey key)
	{
		Set<ForeignKey> keys = res.get(table);
		if (keys == null)
		{
			keys = new HashSet<ForeignKey>();
			res.put(table, keys);
		}
		keys.add(key);
	}

	protected static void collectReferrerAndReferenceColumns(SQL $, Set<Method> keys, //
			Map<Column, Set<ForeignKey>> colsOnRefr, Map<Column, Set<ForeignKey>> colsOnRefn)
	{
		ForeignKey key = null;
		for (Method m : keys)
		{
			key = getForeignKey($, m);
			if (key == null)
			{
				continue;
			}
			if (!(key.entity() instanceof Table) || !(key.reference().entity() instanceof Table))
			{
				continue;
			}
			collectForeignKeyColumns(colsOnRefr, key, key.columns());
			collectForeignKeyColumns(colsOnRefn, key, key.reference().columns());
		}
	}

	public static MetaContext getContext()
	{
		CONTEXT_LOCK.readLock().lock();
		try
		{
			return CONTEXT;
		}
		finally
		{
			CONTEXT_LOCK.readLock().unlock();
		}
	}

	public static ForeignKey getForeignKey(SQL $, Class<? extends Table> referrer, String name)
	{
		try
		{
			Method method = null;
			for (Method m : referrer.getMethods())
			{
				if (!Modifier.isStatic(m.getModifiers()) && m.getAnnotation(ForeignKeyMeta.class) != null
						&& Tools.isSubClass(m.getReturnType(), ForeignKey.class) && m.getParameterTypes().length == 1)
				{
					method = m;
					break;
				}
			}

			if (method == null)
			{
				return null;
			}
			else
			{
				return getForeignKey($, method);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static ForeignKey getForeignKey(SQL $, Method method)
	{
		try
		{
			ForeignKey key = (ForeignKey) method.invoke($.table((Class<? extends Table>) method.getDeclaringClass()),
					$.table((Class<? extends Table>) method.getParameterTypes()[0]));
			return initForeignKey(key, method.getAnnotation(ForeignKeyMeta.class));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected static ForeignKey initForeignKey(ForeignKey key, ForeignKeyMeta meta)
	{
		if (key == null || meta == null)
		{
			return key;
		}

		if (key instanceof AbstractForeignKey)
		{
			AbstractForeignKey fk = (AbstractForeignKey) key;
			fk.onDelete(meta.onDelete());
			fk.onUpdate(meta.onUpdate());
		}

		return key;
	}

	public static void setContext(MetaContext context)
	{
		CONTEXT_LOCK.writeLock().lock();
		try
		{
			CONTEXT = context;
		}
		finally
		{
			CONTEXT_LOCK.writeLock().unlock();
		}
	}

	protected final SQL									$;

	private Map<Class<? extends Table>, Set<Method>>	tableForeignKeyMethods		= new HashMap<Class<? extends Table>, Set<Method>>();

	private Map<Column, Set<ForeignKey>>				columnOnForeignKeyReferrer	= new WrappedHashMap<Column, Set<ForeignKey>>(
			COLUMN_EQUAL);

	private Map<Column, Set<ForeignKey>>				columnOnForeignKeyReference	= new WrappedHashMap<Column, Set<ForeignKey>>(
			COLUMN_EQUAL);

	private Map<Table, Set<ForeignKey>>					tableOnForeignKeyReferrer	= new WrappedHashMap<Table, Set<ForeignKey>>(
			TABLE_EQUAL);

	private Map<Table, Set<ForeignKey>>					tableOnForeignKeyReference	= new WrappedHashMap<Table, Set<ForeignKey>>(
			TABLE_EQUAL);

	public MetaContext(SQL sql)
	{
		this.$ = sql;
	}

	/**
	 * <pre>
	 * Suppose A(a1,a2) -> B(b1,b2)
	 * Check delete on B for every foreign key: 
	 *   select 1 from A where (a1,a2,..) in (select b1,b2,.. from B where delete rules) limit 1
	 * </pre>
	 * 
	 * @param delete
	 * @return The check rules, the last data in tuple indicates whether the
	 *         check is triggerred by referrer side. In this case, the last data
	 *         is false, so that, if the select result is not empty,
	 *         ExistingReferenceException should be thrown.
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Tuple3<ForeignKey, String, Boolean>> checkForeignKey(Delete delete)
	{
		AbstractDelete del = Tools.as(delete, AbstractDelete.class);

		if (del == null || !(del.from() instanceof Table))
		{
			return Collections.EMPTY_SET;
		}

		final Table table = (Table) del.from();
		final Condition rule = del.where();
		final Expression one = $.v(1);

		return Canal.of(getForeignKeysByReference(table))
				.map(new Mapper<ForeignKey, Tuple3<ForeignKey, String, Boolean>>()
				{
					@Override
					public Tuple3<ForeignKey, String, Boolean> map(ForeignKey fk) throws Exception
					{
						Scope scope = $.from(table).where(rule).select(fk.reference().getColumnsOf(table));
						return Tuple.of(fk, $.from(fk.entity()) //
								.where($.list(fk.columns()).in(scope)) //
								.select(one) //
								.limit(one).toString(), false);
					}
				});
	}

	/**
	 * <pre>
	 * Suppose A(a1,a2) -> B(b1,b2)
	 * Check insert on A for every foreign key: 
	 *   select 1 from (select aa1,aa2,.. from B limit 1 | select aa1,aa2,.. from ...) x where not exists (select 1 from B y where y.b1=x.aa1 and y.b2=x.aa2 ..) limit 1
	 * </pre>
	 * 
	 * @param insert
	 * @return The check rules, the last data in tuple indicates whether the
	 *         check is triggerred by referrer side. In this case, the last data
	 *         is true, so that, if the select result is not empty,
	 *         MissingReferenceException should be thrown.
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Tuple3<ForeignKey, String, Boolean>> checkForeignKey(Insert insert)
	{
		final AbstractInsert ins = Tools.as(insert, AbstractInsert.class);

		if (ins == null || !(ins.into() instanceof Table))
		{
			return Collections.EMPTY_SET;
		}

		final Table table = (Table) ins.into();
		final Expression one = $.v(1);
		final Map<Column, Expression> valMap = new WrappedHashMap<Column, Expression>(COLUMN_EQUAL);
		for (Pair<Column, Expression> r : ins.pairs())
		{
			valMap.put(r.key, r.value);
		}

		return Canal.of(getForeignKeysByReferrer(table))
				.map(new Mapper<ForeignKey, Tuple3<ForeignKey, String, Boolean>>()
				{
					@Override
					public Tuple3<ForeignKey, String, Boolean> map(ForeignKey fk) throws Exception
					{
						Collection<Tuple2<Column, Expression>> colvals = Canal.of(fk.getColumnsOf(table))
								.map(new Mapper<Column, Tuple2<Column, Expression>>()
								{
									@Override
									public Tuple2<Column, Expression> map(Column c) throws Exception
									{
										Expression e = valMap.get(c);
										if (e == null)
										{
											Expression expr = Utils.getDataValueExpressionFromField($, c.field());
											expr = expr == null ? $.Null() : expr;
											Item item = Tools.as(expr, Item.class);
											return Tuple.of(c, item != null ? item.as(c.name()) : expr);
										}
										else
										{
											Item i = Tools.as(e, Item.class);
											return Tuple.of(c, i != null ? i.as(c.name()) : e);
										}
									}
								}).collect();

						Expression[] vals = Canal.of(colvals).<Column, Expression> toPair().values().collect()
								.toArray(new Expression[0]);

						Select sel = ins.select();
						if (sel != null)
						{
							sel = sel.as("W");
							sel.select(vals);
						}
						else
						{
							sel = $.from(fk.reference().entity()).select(vals).limit(one).alias("W");
						}
						final Select subSel = sel;

						Condition subCond = Canal.of(colvals).<Column, Expression> toPair().keys()
								.map(new Mapper<Column, ComposableCondition>()
								{
									@Override
									public ComposableCondition map(Column el) throws Exception
									{
										return subSel.ref(el.name()).isNotNull();
									}
								}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
								{
									@Override
									public ComposableCondition reduce(ComposableCondition a, ComposableCondition b)
											throws Exception
									{
										return a.and(b);
									}
								}).get();

						final Select refr = $.from(subSel).where(subCond).select(subSel.all()).alias("X");

						final Table refn = fk.reference().entity().to(Table.class).as("Y");

						Condition existsCond = Canal.of(colvals).zip(Canal.of(fk.reference().columns()))
								.map(new Mapper<Tuple2<Tuple2<Column, Expression>, Column>, ComposableCondition>()
								{
									@Override
									public ComposableCondition map(Tuple2<Tuple2<Column, Expression>, Column> el)
											throws Exception
									{
										return refr.ref(el._1._1.name()).eq(refn.ref(el._2.name()));
									}
								}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
								{
									@Override
									public ComposableCondition reduce(ComposableCondition a, ComposableCondition b)
											throws Exception
									{
										return a.and(b);
									}
								}).get();

						return Tuple.of(fk, $.from(refr) //
								.where($.notExists($.from(refn).where(existsCond).select(one))) //
								.select(one) //
								.limit(one).toString(), true);
					}
				});
	}

	/**
	 * <pre>
	 * Suppose A(a1,a2) -> B(b1,b2)
	 * Check update on A for foreign keys contains updating columns:
	 *   select 1 from (select aa1,aa2 from A where (((a1 is null and aa1 is not null) or a1!=aa1) or ((a2 is null and aa2 is not null) or a2!=aa2) ..) and update rules) x where not exists (select 1 from B y where y.b1=x.aa1 and y.b2=x.aa2 ..) limit 1
	 * Check update on B for foreign keys contains updating columns:
	 *   select 1 from A where (a1,a2,..) in (select b1,b2,.. from B where (b1!=bb1 or b2!=bb2 ..) and update rules) limit 1
	 * </pre>
	 * 
	 * @param update
	 * @return The check rules, the last data in tuple indicates whether the
	 *         check is triggerred by referrer side. Which means if the last
	 *         data is true but the select result is not empty,
	 *         MissingReferenceException should be thrown; If the last data is
	 *         false but the select result is not empty,
	 *         ExistingReferenceException should be thrown.
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Tuple3<ForeignKey, String, Boolean>> checkForeignKey(Update update)
	{
		AbstractUpdate upd = Tools.as(update, AbstractUpdate.class);

		if (upd == null || !(upd.from() instanceof Table))
		{
			return Collections.EMPTY_SET;
		}

		final Table table = (Table) upd.from();
		final Condition rule = upd.where();
		final Expression one = $.v(1);
		final Map<Column, Expression> valMap = new WrappedHashMap<Column, Expression>(COLUMN_EQUAL);
		for (Relation<Column, Expression> r : upd.sets())
		{
			valMap.put(r.getKey(), r.getValue());
		}

		// Check update target as referrer in some foreign key
		return Canal.of(upd.sets()).flatMap(new Mapper<Relation<Column, Expression>, Iterable<ForeignKey>>()
		{
			@Override
			public Iterable<ForeignKey> map(Relation<Column, Expression> el) throws Exception
			{
				return getForeignKeysByReferrer(el.getKey());
			}
		}).distinct().map(new Mapper<ForeignKey, Tuple3<ForeignKey, String, Boolean>>()
		{
			@Override
			public Tuple3<ForeignKey, String, Boolean> map(ForeignKey fk) throws Exception
			{
				Collection<Tuple2<Column, Expression>> colvals = Canal.of(fk.getColumnsOf(table))
						.map(new Mapper<Column, Tuple2<Column, Expression>>()
						{
							@Override
							public Tuple2<Column, Expression> map(Column c) throws Exception
							{
								Expression e = valMap.get(c);
								if (e == null)
								{ // Check the column even if it was not being
									// updated.
									return Tuple.of(c, null);
								}
								else
								{
									Item i = Tools.as(e, Item.class);
									return Tuple.of(c, i != null ? i.as(c.name()) : e);
								}
							}
						}).collect();

				ComposableCondition diffCond = Canal.of(colvals).filter(new Filter<Tuple2<Column, Expression>>()
				{
					@Override
					public boolean filter(Tuple2<Column, Expression> el) throws Exception
					{
						return el._2 != null;
					}
				}).map(new Mapper<Tuple2<Column, Expression>, ComposableCondition>()
				{
					@Override
					public ComposableCondition map(Tuple2<Column, Expression> t) throws Exception
					{
						return $.or(t._1.ne(t._2), t._1.isNull());
					}
				}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
				{
					@Override
					public ComposableCondition reduce(ComposableCondition a, ComposableCondition b) throws Exception
					{
						return a.or(b);
					}
				}).get();

				if (rule != null)
				{
					diffCond = $.and(rule, diffCond);
				}

				Expression[] exprs = Canal.of(colvals).map(new Mapper<Tuple2<Column, Expression>, Expression>()
				{
					@Override
					public Expression map(Tuple2<Column, Expression> el) throws Exception
					{
						return el._2 == null ? el._1 : el._2;
					}
				}).collect().toArray(new Expression[0]);

				final Select diff = $.from(table).where(diffCond).select(exprs).alias("W");

				Condition notNullCond = Canal.of(colvals).<Column, Expression> toPair().keys()
						.map(new Mapper<Column, ComposableCondition>()
						{
							@Override
							public ComposableCondition map(Column el) throws Exception
							{
								return diff.ref(el.name()).isNotNull();
							}
						}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
						{
							@Override
							public ComposableCondition reduce(ComposableCondition a, ComposableCondition b)
									throws Exception
							{
								return a.and(b);
							}
						}).get();

				final Select refr = $.from(diff).where(notNullCond).select(diff.all()).alias("X");

				final Table refn = fk.reference().entity().to(Table.class).as("Y");

				Condition existsCond = Canal.of(colvals).zip(Canal.of(fk.reference().columns()))
						.map(new Mapper<Tuple2<Tuple2<Column, Expression>, Column>, ComposableCondition>()
						{
							@Override
							public ComposableCondition map(Tuple2<Tuple2<Column, Expression>, Column> el)
									throws Exception
							{
								return refr.ref(el._1._1.name()).eq(refn.ref(el._2.name()));
							}
						}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
						{
							@Override
							public ComposableCondition reduce(ComposableCondition a, ComposableCondition b)
									throws Exception
							{
								return a.and(b);
							}
						}).get();

				return Tuple.of(fk, $.from(refr) //
						.where($.notExists($.from(refn).where(existsCond).select(one))) //
						.select(one) //
						.limit(one).toString(), true);
			}
		}).union( // Check update target as reference in some foreign key
				Canal.of(upd.sets()).flatMap(new Mapper<Relation<Column, Expression>, Iterable<ForeignKey>>()
				{
					@Override
					public Iterable<ForeignKey> map(Relation<Column, Expression> el) throws Exception
					{
						return getForeignKeysByReference(el.getKey());
					}
				}).distinct().map(new Mapper<ForeignKey, Tuple3<ForeignKey, String, Boolean>>()
				{
					@Override
					public Tuple3<ForeignKey, String, Boolean> map(ForeignKey fk) throws Exception
					{
						Column[] cols = fk.reference().getColumnsOf(table);
						/*
						 * Generate the updating columns' condition which must
						 * changing values. It's no need to check if nothing
						 * change on the key columns.
						 */
						ComposableCondition cond = Canal.of(cols).map(new Mapper<Column, ComposableCondition>()
						{
							@Override
							public ComposableCondition map(Column c) throws Exception
							{
								Expression e = valMap.get(c);
								return e == null ? null : c.ne(e);
							}
						}).filter(new Filter<ComposableCondition>()
						{
							@Override
							public boolean filter(ComposableCondition el) throws Exception
							{
								return el != null;
							}
						}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
						{
							@Override
							public ComposableCondition reduce(ComposableCondition a, ComposableCondition b)
									throws Exception
							{
								return a.or(b);
							}
						}).get();

						if (rule != null)
						{
							cond = $.and(rule, cond);
						}

						Scope scope = $.from(table).where(cond).select(cols);

						return Tuple.of(fk, $.from(fk.entity()) //
								.where($.list(fk.columns()).in(scope)) //
								.select(one) //
								.limit(one).toString(), false);
					}
				}) //
		);
	}

	@SuppressWarnings("unchecked")
	public void collectForeignKeyRelations(Class<?>... classes)
	{
		for (Class<?> c : classes)
		{
			try
			{
				collectForeignKeyRelations(this.getTableForeignKeyMethods(), (Class<? extends Table>) c);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		this.collectReferrerAndReferenceColumns();
	}

	protected void collectReferrerAndReferenceColumns()
	{
		for (Set<Method> set : this.getTableForeignKeyMethods().values())
		{
			collectReferrerAndReferenceColumns($, set, this.getColumnOnForeignKeyReferrer(),
					this.getColumnOnForeignKeyReference());
		}

		Canal.of(this.getColumnOnForeignKeyReferrer().values())
				.flatMap(new Mapper<Set<ForeignKey>, Iterable<ForeignKey>>()
				{
					@Override
					public Iterable<ForeignKey> map(Set<ForeignKey> el) throws Exception
					{
						return el;
					}
				}).distinct().keyBy(new Mapper<ForeignKey, Table>()
				{
					@Override
					public Table map(ForeignKey el) throws Exception
					{
						return (Table) el.entity();
					}
				}).foreach(new Action<Canal.Tuple2<Table, ForeignKey>>()
				{
					@Override
					public void action(Tuple2<Table, ForeignKey> el) throws Exception
					{
						collectForeignKeyTable(getTableOnForeignKeyReferrer(), el._1, el._2);
					}
				});

		Canal.of(this.getColumnOnForeignKeyReference().values())
				.flatMap(new Mapper<Set<ForeignKey>, Iterable<ForeignKey>>()
				{
					@Override
					public Iterable<ForeignKey> map(Set<ForeignKey> el) throws Exception
					{
						return el;
					}
				}).distinct().keyBy(new Mapper<ForeignKey, Table>()
				{
					@Override
					public Table map(ForeignKey el) throws Exception
					{
						return (Table) el.reference().entity();
					}
				}).foreach(new Action<Canal.Tuple2<Table, ForeignKey>>()
				{
					@Override
					public void action(Tuple2<Table, ForeignKey> el) throws Exception
					{
						collectForeignKeyTable(getTableOnForeignKeyReference(), el._1, el._2);
					}
				});
	}

	protected Map<Column, Set<ForeignKey>> getColumnOnForeignKeyReference()
	{
		return columnOnForeignKeyReference;
	}

	protected Map<Column, Set<ForeignKey>> getColumnOnForeignKeyReferrer()
	{
		return columnOnForeignKeyReferrer;
	}

	@SuppressWarnings("unchecked")
	public Set<ForeignKey> getForeignKeysByReference(Column column)
	{
		Set<ForeignKey> set = this.getColumnOnForeignKeyReference().get(column);
		return set == null ? Collections.EMPTY_SET : set;
	}

	@SuppressWarnings("unchecked")
	public Set<ForeignKey> getForeignKeysByReference(Table table)
	{
		Set<ForeignKey> set = this.getTableOnForeignKeyReference().get(table);
		return set == null ? Collections.EMPTY_SET : set;
	}

	@SuppressWarnings("unchecked")
	public Set<ForeignKey> getForeignKeysByReferrer(Column column)
	{
		Set<ForeignKey> set = this.getColumnOnForeignKeyReferrer().get(column);
		return set == null ? Collections.EMPTY_SET : set;
	}

	@SuppressWarnings("unchecked")
	public Set<ForeignKey> getForeignKeysByReferrer(Table table)
	{
		Set<ForeignKey> set = this.getTableOnForeignKeyReferrer().get(table);
		return set == null ? Collections.EMPTY_SET : set;
	}

	protected Map<Class<? extends Table>, Set<Method>> getTableForeignKeyMethods()
	{
		return tableForeignKeyMethods;
	}

	protected Map<Table, Set<ForeignKey>> getTableOnForeignKeyReference()
	{
		return tableOnForeignKeyReference;
	}

	protected Map<Table, Set<ForeignKey>> getTableOnForeignKeyReferrer()
	{
		return tableOnForeignKeyReferrer;
	}

	protected void setColumnOnForeignKeyReference(Map<Column, Set<ForeignKey>> columnOnForeignKeyReference)
	{
		this.columnOnForeignKeyReference = columnOnForeignKeyReference;
	}

	protected void setColumnOnForeignKeyReferrer(Map<Column, Set<ForeignKey>> columnOnForeignKeyReferrer)
	{
		this.columnOnForeignKeyReferrer = columnOnForeignKeyReferrer;
	}

	protected void setTableForeignKeyMethods(Map<Class<? extends Table>, Set<Method>> tableForeignKeyMethods)
	{
		this.tableForeignKeyMethods = tableForeignKeyMethods;
	}

	protected void setTableOnForeignKeyReference(Map<Table, Set<ForeignKey>> tableOnForeignKeyReference)
	{
		this.tableOnForeignKeyReference = tableOnForeignKeyReference;
	}

	protected void setTableOnForeignKeyReferrer(Map<Table, Set<ForeignKey>> tableOnForeignKeyReferrer)
	{
		this.tableOnForeignKeyReferrer = tableOnForeignKeyReferrer;
	}
}
