package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kernelab.basis.Canal;
import org.kernelab.basis.Canal.Option;
import org.kernelab.basis.Canal.Tuple2;
import org.kernelab.basis.Filter;
import org.kernelab.basis.JSON;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Reducer;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.Row.RowProjector;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.basis.sql.Sequel;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.AbsoluteKey;
import org.kernelab.dougong.core.ddl.EntityKey;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractEntity;
import org.kernelab.dougong.semi.AbstractTable;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public abstract class Entitys
{
	public static class GenerateValueColumns
	{
		public final short		strategy;

		public final Column[]	columns;

		public final Column[]	gencols;

		public final Column[]	abscols;

		public GenerateValueColumns(short strategy, Column[] columns, Column[] gencols, Column[] abscols)
		{
			this.strategy = strategy;
			this.columns = columns;
			this.gencols = gencols != null ? gencols : new Column[0];
			this.abscols = abscols != null ? abscols : new Column[0];
		}
	}

	public static <T> int deleteObject(SQLKit kit, SQL sql, Class<T> model, Object... pkVals) throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelClass(sql, model);

		PrimaryKey pk = entity.primaryKey();

		Map<String, Object> params = new LinkedHashMap<String, Object>();

		for (int i = 0; i < pk.columns().length; i++)
		{
			params.put(Utils.getDataLabelFromField(pk.columns()[i].field()), pkVals[i]);
		}

		Delete delete = sql.from(entity) //
				.where(pk.queryCondition()) //
				.delete();

		MetaContext.check(kit, delete, params);

		return kit.update(delete.toString(), params);
	}

	public static <T> int deleteObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return deleteObject(kit, sql, object, null);
	}

	public static <T> int deleteObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return 0;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		deleteObjectCascade(kit, sql, object, entity);

		return deleteObjectAlone(kit, sql, object, entity);
	}

	public static <T> int deleteObjectAlone(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return deleteObjectAlone(kit, sql, object, null);
	}

	public static <T> int deleteObjectAlone(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return 0;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		EntityKey key = getUpdateKey(entity, object);

		Delete delete = sql.from(entity) //
				.where(key.queryCondition()) //
				.delete();

		Map<String, Object> params = mapColumnToLabelByMeta(key.mapValues(object));

		MetaContext.check(kit, delete, params);

		return kit.update(delete.toString(), params);
	}

	protected static <T> void deleteObjectCascade(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		for (Field field : Tools.getFieldsHierarchy(object.getClass(), null).values())
		{
			if (isOneToMany(field))
			{
				deleteOneToMany(kit, sql, object, entity, field, false, null);
			}
			else if (isOneToOneNeedSave(sql, entity, field))
			{
				deleteOneToOne(kit, sql, object, field);
			}
		}
	}

	public static <T> int deleteObjects(SQLKit kit, SQL sql, T referenceObject, ForeignKey key, Entity referrer)
			throws SQLException
	{
		if (referenceObject == null || key == null)
		{
			return 0;
		}

		Delete delete = sql.from(referrer) //
				.where(key.entity() == referrer ? key.queryCondition() : key.reference().queryCondition()) //
				.delete();

		Map<String, Object> params = mapColumnToLabelByMeta(key.mapValuesTo(referenceObject, referrer));

		MetaContext.check(kit, delete, params);

		return kit.update(delete.toString(), params);
	}

	/**
	 * Delete the objects in referrer entity which match the foreign key values
	 * from parent but the absolute key are beyond the correspond values in the
	 * given children collection. If the absolute key is null then all the
	 * objects on referrer side will be deleted in its entity.
	 * 
	 * @param kit
	 * @param sql
	 * @param parent
	 * @param children
	 * @param fk
	 * @param ak
	 * @param referrerModel
	 * @param referrerEntity
	 * @throws SQLException
	 */
	protected static <T, R> void deleteObjectsBeyondAbsoluteKey(SQLKit kit, SQL sql, T parent,
			Collection<Object> children, ForeignKey fk, final AbsoluteKey ak, Class<R> referrerModel,
			Entity referrerEntity) throws SQLException
	{
		Condition cond = fk.entity() == referrerEntity ? fk.queryCondition() : fk.reference().queryCondition();

		Map<String, Object> params = mapColumnToLabelByMeta(fk.mapValuesTo(parent, referrerEntity));

		if (ak != null)
		{
			final Column absCol = ak.columns()[0];
			String absname = Utils.getDataLabelFromField(absCol.field());

			Iterable<Object> absVals = Canal.of(children).map(new Mapper<Object, Object[]>()
			{
				@Override
				public Object[] map(Object el)
				{
					return mapObjectToValues(el, absCol);
				}
			}).filter(new Filter<Object[]>()
			{
				@Override
				public boolean filter(Object[] el) throws Exception
				{
					return el != null;
				}
			}).map(new Mapper<Object[], Object>()
			{
				@Override
				public Object map(Object[] el) throws Exception
				{
					return el[0];
				}
			});

			Condition beyondCond = fragmentInConditions(sql, absCol, false, absname, true, 1000, absVals, params);
			if (beyondCond != null)
			{
				cond = sql.and(cond, beyondCond);
			}
		}

		Select select = sql.from(referrerEntity) //
				.where(cond) //
				.select(referrerEntity.all()) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();

		for (R miss : selectObjects(kit, sql, select, referrerModel, (String) null, params))
		{
			deleteObject(kit, sql, miss, referrerEntity);
		}
	}

	/**
	 * Delete the objects in referrer entity which match the foreign key values
	 * from parent but rest columns in primary key are beyond the correspond
	 * values in the given children collection.
	 * 
	 * @param kit
	 * @param sql
	 * @param parent
	 * @param children
	 * @param fk
	 * @param pk
	 * @param referrerModel
	 * @param referrerEntity
	 * @throws SQLException
	 */
	protected static <T, R> void deleteObjectsBeyondPrimaryKey(SQLKit kit, SQL sql, T parent,
			Collection<Object> children, ForeignKey fk, final PrimaryKey pk, Class<R> referrerModel,
			Entity referrerEntity) throws SQLException
	{
		Condition cond = fk.entity() == referrerEntity ? fk.queryCondition() : fk.reference().queryCondition();

		Map<String, Object> params = mapColumnToLabelByMeta(fk.mapValuesTo(parent, referrerEntity));

		final Column[] restCols = pk.excludeColumns(fk.columns());

		String name = "_";
		for (Column c : restCols)
		{
			name += "#" + c.name() + "_";
		}
		name += "#NOT_IN_#";

		Items beyondCols = sql.list(restCols);

		Iterable<Object> restVals = Canal.of(children).map(new Mapper<Object, Object>()
		{
			@Override
			public Object map(Object el)
			{
				return mapObjectToValues(el, restCols);
			}
		});

		Condition beyondCond = fragmentInConditions(sql, beyondCols, false, name, true, 1000, restVals, params);
		if (beyondCond != null)
		{
			cond = sql.and(cond, beyondCond);
		}

		Select select = sql.from(referrerEntity) //
				.where(cond) //
				.select(referrerEntity.all()) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();

		for (R miss : selectObjects(kit, sql, select, referrerModel, (String) null, params))
		{
			deleteObject(kit, sql, miss, referrerEntity);
		}
	}

	protected static <T> void deleteOneToMany(SQLKit kit, SQL sql, T parent, Entity entity, Field field,
			boolean beyondOnly, Collection<Object> many) throws SQLException
	{
		if (many == null)
		{
			try
			{
				many = Tools.access(parent, field);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);
		Class<?> manyClass = meta.model();
		final Entity manyEntity = getEntityFromModelClass(sql, manyClass);
		ForeignKey fk = getForeignKey(meta.key(), meta.referred(), entity, manyEntity);
		if (fk == null)
		{
			return;
		}

		PrimaryKey pk = null;
		AbsoluteKey ak = null;
		if (many != null && beyondOnly)
		{
			boolean hasChild = false;
			for (Object o : many)
			{
				if (o != null)
				{
					hasChild = true;
					break;
				}
			}

			if (hasChild && fk.inPrimaryKey())
			{
				Column[] pkrest = fk.entity().primaryKey().excludeColumns(fk.columns());
				if (pkrest != null && pkrest.length > 0 && hasAllColumnsInModel(manyClass, fk.entity().primaryKey()))
				{
					pk = fk.entity().primaryKey();
				}
			}

			if (hasChild && pk == null && manyEntity.absoluteKey() != null)
			{
				ak = hasAllColumnsInModel(manyClass, manyEntity.absoluteKey()) ? manyEntity.absoluteKey() : null;
			}
		}

		if (pk != null)
		{
			deleteObjectsBeyondPrimaryKey(kit, sql, parent, many, fk, pk, manyClass, manyEntity);
		}
		else
		{
			deleteObjectsBeyondAbsoluteKey(kit, sql, parent, many, fk, ak, manyClass, manyEntity);
		}
	}

	protected static <T> void deleteOneToOne(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		Object val = null;
		try
		{
			val = Tools.access(object, field);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		if (val != null)
		{
			deleteObjectAlone(kit, sql, val, null);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> boolean existsObject(SQLKit kit, SQL sql, Class<T> model, Object... pkVals) throws SQLException
	{
		Entity entity = (Entity) (Tools.isSubClass(model, Entity.class) ? sql.view((Class<View>) model)
				: Entitys.getEntityFromModelClass(sql, model));

		PrimaryKey pk = entity.primaryKey();

		Map<String, Object> params = new LinkedHashMap<String, Object>();

		for (int i = 0; i < pk.columns().length; i++)
		{
			params.put(Utils.getDataLabelFromField(pk.columns()[i].field()), pkVals[i]);
		}

		Expression one = sql.val(1);

		Select select = sql.from(entity) //
				.where(pk.queryCondition()) //
				.select(one) //
				.limit(one);

		return kit.exists(select.toString(), params);
	}

	public static <T> boolean existsObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return existsObject(kit, sql, object, null);
	}

	public static <T> boolean existsObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return false;
		}

		if (entity == null)
		{
			entity = getEntityFromModelObject(sql, object);
		}

		EntityKey key = getUpdateKey(entity, object);

		if (key == null)
		{
			return false;
		}

		Expression one = sql.val(1);

		Select select = sql.from(entity) //
				.where(key.queryCondition()) //
				.select(one) //
				.limit(one);

		Map<String, Object> params = mapColumnToLabelByMeta(key.mapValues(object));

		return kit.exists(select.toString(), params);
	}

	/**
	 * Fragment a IN/NOT IN list into several segments and combine each
	 * condition using AND/OR logic.
	 * 
	 * @param sql
	 * @param cols
	 *            The expression on the left side of IN/NOT IN condition.
	 * @param in
	 *            true means compose IN condition otherwise use NOT IN.
	 * @param paramPrefix
	 *            The prefix of parameter(s) name.
	 * @param and
	 *            true means combine each condition with AND logic, otherwise
	 *            use OR.
	 * @param segmentLength
	 *            The max length of each segment.
	 * @param vals
	 *            The value(s) list.
	 * @param params
	 *            The parameters' map.
	 * @return
	 */
	protected static Condition fragmentInConditions(final SQL sql, final Expression cols, final boolean in,
			final String paramPrefix, final boolean and, int segmentLength, Iterable<Object> vals,
			final Map<String, Object> params)
	{
		return Canal.of(vals).filter(new Filter<Object>()
		{
			@Override
			public boolean filter(Object el) throws Exception
			{
				return el != null;
			}
		}).sliding(segmentLength).zipWithIndex()
				.map(new Mapper<Tuple2<Iterable<Object>, Integer>, ComposableCondition>()
				{
					@Override
					public ComposableCondition map(Tuple2<Iterable<Object>, Integer> el) throws Exception
					{
						String param = paramPrefix + el._2;
						params.put(param, el._1);
						return in ? cols.in(sql.param(param)) : cols.not().in(sql.param(param));
					}
				}).reduce(new Reducer<ComposableCondition, ComposableCondition>()
				{
					@Override
					public ComposableCondition reduce(ComposableCondition a, ComposableCondition b) throws Exception
					{
						return and ? a.and(b) : a.or(b);
					}
				}).orNull();
	}

	protected static Set<Column> getColumns(Entity entity)
	{
		Set<Column> columns = new LinkedHashSet<Column>();

		for (Field field : Tools.getFieldsHierarchy(entity.getClass(), null).values())
		{
			if (Tools.isSubClass(field.getType(), Column.class))
			{
				try
				{
					columns.add((Column) Tools.access(entity, field));
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}

		return columns;
	}

	public static String getColumnSelectExpression(Provider p, Column column)
	{
		DataMeta meta = column.field() == null ? null : column.field().getAnnotation(DataMeta.class);
		if (meta == null || meta.select() == null || meta.select().length() == 0)
		{
			return null;
		}

		String expr = meta.select();
		if (column.view().alias() == null)
		{
			expr = expr.replace("?.", "");
		}
		else
		{
			expr = expr.replace("?.", p.provideNameText(column.view().alias()) + ".");
		}
		return expr.replace("?", p.provideNameText(column.name()));
	}

	public static Collection<Column> getColumnsFromEntityByModel(SQL sql, Class<?> model, Entity entity,
			Map<String, Column> colsMap, Collection<Column> columns)
	{
		if (model == null || model == Object.class)
		{
			return columns;
		}

		if (entity == null)
		{
			entity = getEntityFromModelClass(sql, model);
		}

		if (colsMap == null)
		{
			colsMap = new HashMap<String, Column>();
			Column column = null;
			Field field = null;
			for (Item item : entity.items())
			{
				if ((column = Tools.as(item, Column.class)) != null)
				{
					if ((field = column.field()) != null)
					{
						colsMap.put(Utils.getDataLabelFromField(field), column);
					}
				}
			}
		}

		if (columns == null)
		{
			columns = new LinkedHashSet<Column>();
		}

		columns = getColumnsFromEntityByModel(sql, model.getSuperclass(), entity, colsMap, columns);

		DataMeta meta = null;
		Column column = null;
		for (Field field : model.getDeclaredFields())
		{
			if ((meta = field.getAnnotation(DataMeta.class)) != null //
					&& meta.raw())
			{
				if ((column = colsMap.get(Utils.getDataLabelFromField(field))) != null)
				{
					columns.add(column);
				}
			}
		}

		return columns;
	}

	protected static Map<Column, String> getColumnsLabelMap(Collection<Column> columns)
	{
		Map<Column, String> map = new LinkedHashMap<Column, String>();

		for (Column column : columns)
		{
			map.put(column, Utils.getDataLabelFromField(column.field()));
		}

		return map;
	}

	protected static Map<Column, String> getColumnsLabelMap(Column... columns)
	{
		Map<Column, String> map = new LinkedHashMap<Column, String>();

		for (Column column : columns)
		{
			map.put(column, Utils.getDataLabelFromField(column.field()));
		}

		return map;
	}

	public static Class<? extends Entity> getEntityClassFromModel(Class<?> modelClass)
	{
		EntityMeta meta = modelClass.getAnnotation(EntityMeta.class);
		return meta != null ? meta.entity() : null;
	}

	public static <T> Entity getEntityFromModelClass(SQL sql, Class<T> model)
	{
		return sql.view(getEntityClassFromModel(model));
	}

	public static <T> Entity getEntityFromModelObject(SQL sql, T object)
	{
		return object == null ? null : getEntityFromModelClass(sql, object.getClass());
	}

	/**
	 * Get a foreign key with given name. And the fist entity parameter would
	 * refers to the second entity if secondAsReference is true. Otherwise the
	 * first entity would be the reference.
	 * 
	 * @param name
	 * @param secondAsReference
	 * @param first
	 * @param second
	 * @return
	 */
	protected static ForeignKey getForeignKey(String name, boolean secondAsReference, Entity first, Entity second)
	{
		if (secondAsReference)
		{
			return first.foreignKey(name, second);
		}
		else
		{
			return second.foreignKey(name, first);
		}
	}

	/**
	 * Get columns that generating values defined in Entity.
	 * 
	 * @param entity
	 * @return
	 */
	protected static GenerateValueColumns getGenerateValueColumns(Entity entity)
	{
		GenerateValueMeta gen = null;
		AbsoluteKeyMeta abs = null;

		List<Column> columns = new LinkedList<Column>();
		List<Column> gencols = new LinkedList<Column>();
		List<Column> abscols = new LinkedList<Column>();

		Column column = null;

		for (Field field : Tools.getFieldsHierarchy(entity.getClass(), null).values())
		{
			if (AbstractEntity.isColumn(field))
			{
				gen = field.getAnnotation(GenerateValueMeta.class);
				abs = field.getAnnotation(AbsoluteKeyMeta.class);

				if (gen != null || abs != null)
				{
					try
					{
						column = (Column) Tools.access(entity, field);

						if (gen != null)
						{
							switch (gen.strategy())
							{
								case GenerateValueMeta.AUTO:
									columns.add(column);
									gencols.add(column);
									break;

								case GenerateValueMeta.IDENTITY:
									return new GenerateValueColumns(GenerateValueMeta.IDENTITY, new Column[] { column },
											null, new Column[] { column });
							}
						}
						else if (abs != null)
						{
							if (abs.generate())
							{
								columns.add(column);
								abscols.add(column);
							}
						}
					}
					catch (Exception ex)
					{
						throw new RuntimeException(ex);
					}
				}
			}
		}

		if (columns.isEmpty())
		{
			return null;
		}

		Column[] cols = columns.toArray(new Column[columns.size()]);
		Column[] gens = gencols.toArray(new Column[gencols.size()]);
		Column[] abss = abscols.toArray(new Column[abscols.size()]);

		return new GenerateValueColumns(GenerateValueMeta.AUTO, cols, gens, abss);
	}

	public static String getLabelFromColumnByMeta(Column column)
	{
		return Utils.getDataLabelFromField(column.field());
	}

	public static Set<String> getLabelsOfSelect(Select select)
	{
		Set<String> labels = new LinkedHashSet<String>();
		for (Item item : select.items())
		{
			labels.add(item.label());
		}
		return labels;
	}

	protected static Field getManyToOneField(Class<?> manyClass, Class<?> oneClass)
	{
		ManyToOneMeta meta = null;
		for (Field field : Tools.getFieldsHierarchy(manyClass, null).values())
		{
			meta = field.getAnnotation(ManyToOneMeta.class);
			if (meta != null && Tools.equals(oneClass, meta.model()))
			{
				return field;
			}
		}
		return null;
	}

	/**
	 * Get an array of model fields according to the given columns' list.
	 * 
	 * @param model
	 * @param columns
	 * @return
	 */
	protected static Field[] getModelFieldsOfColumns(Class<?> model, Column... columns)
	{
		if (model == null || columns == null)
		{
			return null;
		}

		Map<String, Field> map = Utils.getLabelFieldMapByMeta(model, null);

		Field[] fields = new Field[columns.length];

		for (int i = 0; i < columns.length; i++)
		{
			fields[i] = map.get(Utils.getDataLabelFromField(columns[i].field()));
		}

		return fields;
	}

	protected static Field getOneToOneField(Class<?> oneClass, Class<?> anotherClass)
	{
		for (Field field : Tools.getFieldsHierarchy(oneClass, null).values())
		{
			if (field.getAnnotation(OneToOneMeta.class) != null //
					&& Tools.isSubClass(anotherClass, field.getType()))
			{
				return field;
			}
		}
		return null;
	}

	protected static <T> Queryable getRedefinedQueryableObject(SQL sql, T object, Field field)
	{
		if (object == null || field == null)
		{
			return null;
		}

		Method method = Tools.accessor(object.getClass(), null, field, Queryable.class);
		if (method == null)
		{
			return null;
		}

		Queryable sap = new Queryable(sql);
		try
		{
			method.invoke(object, sap);
			return sap;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static <T> RowProjector<T> getRowProjectorByModelMeta(Class<T> model)
	{
		return new RowProjector<T>(model, Utils.getFieldNameMapByMeta(model, null, null));
	}

	public static <T> EntityKey getUpdateKey(Entity entity, T object)
	{
		EntityKey ak = null, pk = null;

		if ((ak = entity.absoluteKey()) != null && (object == null || !hasNullValue(ak.mapValues(object))))
		{
			return ak;
		}
		else if ((pk = entity.primaryKey()) != null && (object == null || !hasNullValue(pk.mapValues(object))))
		{
			return pk;
		}
		else if (ak != null)
		{
			return ak;
		}
		else if (pk != null)
		{
			return pk;
		}
		else
		{
			return null;
		}
	}

	protected static boolean hasAllColumnsInModel(Class<?> model, Column... columns)
	{
		return Tools.noNulls(getModelFieldsOfColumns(model, columns)) != null;
	}

	protected static boolean hasAllColumnsInModel(Class<?> model, Key key)
	{
		return hasAllColumnsInModel(model, key.columns());
	}

	protected static boolean hasNullValue(Map<?, Object> param)
	{
		for (Object value : param.values())
		{
			if (value == null)
			{
				return true;
			}
		}
		return false;
	}

	protected static <T> boolean hasNullValue(T object, Entity entity, Column... columns)
	{
		Map<Column, Object> values = mapObjectToEntity(object, entity);

		for (Column column : columns)
		{
			if (values.get(column) == null)
			{
				return true;
			}
		}

		return false;
	}

	protected static ResultSet insertAndReturn(SQLKit kit, SQL sql, Insert insert, Map<String, Object> params,
			GenerateValueColumns generates, Column[] returns) throws SQLException
	{
		if (generates == null || generates.strategy == GenerateValueMeta.NONE)
		{
			MetaContext.check(kit, insert, params);
			kit.update(insert.toString(), params);
			return null;
		}
		else if (generates.strategy == GenerateValueMeta.IDENTITY //
				|| (generates.strategy == GenerateValueMeta.AUTO && generates.gencols.length == 0
						&& generates.abscols.length > 0))
		{
			MetaContext.check(kit, insert, params);
			PreparedStatement ps = kit.prepareStatement(insert.toString(), params, true);
			kit.update(ps, params);
			return ps.getGeneratedKeys();
		}
		else if (generates.strategy == GenerateValueMeta.AUTO)
		{
			Set<Column> retSet = Tools.setOfArray(new HashSet<Column>(), returns);

			boolean hasAbs = false;
			for (Column column : generates.abscols)
			{
				if (retSet.contains(column))
				{
					hasAbs = true;
					break;
				}
			}

			MetaContext.check(kit, insert, params);

			if (!hasAbs)
			{
				return sql.provider().provideDoInsertAndReturnGenerates(kit, sql, insert, params, returns);
			}
			else
			{
				return sql.provider().provideDoInsertAndReturnGenerates(kit, sql, insert, params, generates, returns);
			}
		}
		else
		{
			return null;
		}
	}

	public static <T> int insertObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return insertObject(kit, sql, object, null);
	}

	public static <T> int insertObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return 0;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		int res = insertObjectAlone(kit, sql, object, entity);

		insertObjectCascade(kit, sql, object, entity);

		return res;
	}

	public static <T> int insertObjectAlone(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return insertObjectAlone(kit, sql, object, null);
	}

	public static <T> int insertObjectAlone(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return 0;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		GenerateValueColumns generates = Entitys.getGenerateValueColumns(entity);
		Column[] gencols = generates != null ? generates.columns : null;

		AbstractTable table = entity.to(AbstractTable.class);
		Map<Column, Expression> insertMeta = table.getColumnDefaultExpressions(null);
		Map<Column, Expression> valueMeta = table.getColumnValueExpressions(null);
		insertMeta = overwriteColumnDefaults(sql, object, insertMeta, valueMeta);

		Map<String, Object> params = Entitys.mapObjectByMeta(object);
		Insert insert = null;
		Column[] returns = null;

		if (generates == null)
		{
			insert = table.insertByMetaMap(insertMeta);
		}
		else if (generates.strategy == GenerateValueMeta.IDENTITY //
				|| (generates.strategy == GenerateValueMeta.AUTO && generates.gencols.length == 0
						&& generates.abscols.length > 0))
		{
			for (Column column : gencols)
			{
				insertMeta.remove(column);
				returns = new Column[] { column };
				break;
			}
			insert = table.insertByMetaMap(insertMeta);
		}
		else if (generates.strategy == GenerateValueMeta.AUTO)
		{
			Set<Column> genset = Tools.setOfArray(new LinkedHashSet<Column>(), gencols);
			Map<Column, Object> genvals = mapObjectToEntity(object, gencols);
			for (Entry<Column, Object> entry : genvals.entrySet())
			{
				if (entry.getValue() != null)
				{
					genset.remove(entry.getKey());
				}
			}

			if (genset.isEmpty())
			{
				insert = table.insertByMetaMap(insertMeta);
				generates = null;
			}
			else
			{
				returns = genset.toArray(new Column[genset.size()]);
				insert = table.insertByMetaMap(insertMeta);
			}
		}

		ResultSet returnSet = insertAndReturn(kit, sql, insert, params, generates, returns);

		// Set generate values
		if (returnSet != null)
		{
			try
			{
				Set<Column> restGens = new LinkedHashSet<Column>(valueMeta.keySet());
				if (returnSet.next())
				{
					Field field = null;
					Object value = null;
					Map<String, Object> genvals = new HashMap<String, Object>();
					Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass(), null);
					for (int i = 0; i < returns.length; i++)
					{
						field = fields.get(Utils.getDataLabelFromField(returns[i].field()));
						value = returnSet.getObject(i + 1);
						Entitys.mapColumnToLabelByMeta(genvals, returns[i], value);
						if (field != null)
						{
							try
							{
								Tools.access(object, field, Tools.castTo(value, field.getType()));
							}
							catch (Exception e)
							{
								throw new RuntimeException(e);
							}
							restGens.remove(returns[i]);
						}
					}
					if (!restGens.isEmpty())
					{
						refreshObject(kit, sql, object, entity, generates, restGens, genvals);
					}
				}
			}
			finally
			{
				try
				{
					returnSet.close();
				}
				catch (SQLException e)
				{
				}
			}
		}

		return 1;
	}

	protected static <T> void insertObjectCascade(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		for (Field field : Tools.getFieldsHierarchy(object.getClass(), null).values())
		{
			if (isOneToMany(field))
			{
				insertOneToMany(kit, sql, object, field);
			}
			else if (isOneToOneNeedSave(sql, entity, field))
			{
				insertOneToOne(kit, sql, object, field);
			}
		}
	}

	protected static <T> void insertOneToMany(SQLKit kit, SQL sql, T parent, Entity entity, Field field,
			Collection<Object> many) throws SQLException
	{
		if (many == null)
		{
			try
			{
				many = Tools.access(parent, field);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		if (many == null)
		{
			return;
		}

		OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);
		Class<?> manyClass = meta.model();
		Entity manyEntity = getEntityFromModelClass(sql, manyClass);
		GenerateValueColumns generates = getGenerateValueColumns(manyEntity);

		for (Object child : many)
		{
			if (generates == null || hasNullValue(child, manyEntity, generates.columns))
			{
				saveObject(kit, sql, child, manyEntity);
			}
		}
	}

	protected static <T> void insertOneToMany(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		insertOneToMany(kit, sql, object, field, null);
	}

	protected static <T> void insertOneToMany(SQLKit kit, SQL sql, T object, Field field, Collection<?> many)
			throws SQLException
	{
		if (many == null)
		{
			try
			{
				many = Tools.access(object, field);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		if (many == null)
		{
			return;
		}

		Entity entity = null;

		for (Object o : many)
		{
			if (entity == null)
			{
				entity = getEntityFromModelObject(sql, o);
			}
			insertObjectAlone(kit, sql, o, entity);
			insertObjectCascade(kit, sql, o, entity);
		}
	}

	protected static <T> void insertOneToOne(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		Object val = null;

		try
		{
			val = Tools.access(object, field);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		if (val != null)
		{
			Entity entity = getEntityFromModelObject(sql, val);
			insertObjectAlone(kit, sql, val, entity);
			insertObjectCascade(kit, sql, val, entity);
		}
	}

	protected static boolean isManyToOne(Field field)
	{
		return field.getAnnotation(ManyToOneMeta.class) != null;
	}

	protected static boolean isNeedSetup(ManyToOneMeta meta, String scene)
	{
		return meta != null && isUnderScene(scene, meta.scenes());
	}

	protected static boolean isNeedSetup(OneToManyMeta meta, String scene)
	{
		return meta != null && isUnderScene(scene, meta.scenes());
	}

	protected static boolean isNeedSetup(OneToOneMeta meta, String scene)
	{
		return meta != null && isUnderScene(scene, meta.scenes());
	}

	protected static boolean isOneToMany(Field field)
	{
		return field.getAnnotation(OneToManyMeta.class) != null;
	}

	protected static boolean isOneToOneNeedSave(SQL sql, Entity entity, Field field)
	{
		OneToOneMeta meta = field.getAnnotation(OneToOneMeta.class);
		if (meta != null)
		{
			ForeignKey key = getForeignKey(meta.key(), meta.referred(), entity,
					getEntityFromModelClass(sql, meta.model()));
			return key != null && key.inPrimaryKey();
		}
		else
		{
			return false;
		}
	}

	protected static boolean isUnderScene(String target, String[] scenes)
	{
		if (target == null || scenes == null || scenes.length == 0)
		{
			return true;
		}
		for (String s : scenes)
		{
			if (target.equals(s))
			{
				return true;
			}
		}
		return false;
	}

	public static <T> Map<String, Object> makeParams(SQL sql, T object)
	{
		return makeParams(object, getEntityFromModelObject(sql, object));
	}

	public static <T> Map<String, Object> makeParams(T object, Entity entity)
	{
		return mapColumnToLabelByMeta(mapObjectToEntity(object, entity));
	}

	public static Select makeSelectByPrimaryKey(SQL sql, Class<?> model, Entity entity)
	{
		if (entity == null)
		{
			entity = Entitys.getEntityFromModelClass(sql, model);
		}

		return sql.from(entity) //
				.where(entity.primaryKey().queryCondition()) //
				.select(getColumnsFromEntityByModel(sql, model, entity, null, null).toArray(new Column[0])) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();
	}

	public static <T> Update makeUpdate(SQL sql, Entity entity, T object, Key key)
	{
		if (entity == null && object == null)
		{
			return null;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		AbstractTable table = entity.to(AbstractTable.class);

		Map<Column, Expression> meta = table.getColumnDefaultExpressions(key);

		if (meta.isEmpty())
		{
			return null;
		}

		if (object != null)
		{
			meta = overwriteColumnDefaults(sql, object, meta, null);
		}

		return table.updateByMetaMap(meta);
	}

	protected static Map<String, Object> mapColumnToLabelByMeta(Column column, Object value)
	{
		return mapColumnToLabelByMeta(new HashMap<String, Object>(), column, value);
	}

	/**
	 * Map key to label defined by DataMeta.
	 * 
	 * @param data
	 * @return
	 */
	protected static Map<String, Object> mapColumnToLabelByMeta(Map<Column, Object> data)
	{
		return mapColumnToLabelByMeta(data, null);
	}

	/**
	 * Map key to label defined by DataMeta into a given Map.
	 * 
	 * @param data
	 * @param result
	 * @return
	 */
	protected static Map<String, Object> mapColumnToLabelByMeta(Map<Column, Object> data, Map<String, Object> result)
	{
		if (result == null)
		{
			result = new HashMap<String, Object>();
		}

		for (Entry<Column, Object> entry : data.entrySet())
		{
			mapColumnToLabelByMeta(result, entry.getKey(), entry.getValue());
		}

		return result;
	}

	protected static Map<String, Object> mapColumnToLabelByMeta(Map<String, Object> result, Column column, Object value)
	{
		String key = getLabelFromColumnByMeta(column);
		result.put(key, Option.or(value, result.get(key)));
		return result;
	}

	protected static Map<Column, String> mapLabelsFromColumnsByMeta(Column... columns)
	{
		Map<Column, String> map = new LinkedHashMap<Column, String>();

		for (Column column : columns)
		{
			map.put(column, getLabelFromColumnByMeta(column));
		}

		return map;
	}

	protected static <T> Map<String, Object> mapObjectByMeta(T object)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		Object value = null;

		for (Field field : Tools.getFieldsHierarchy(object.getClass(), null).values())
		{
			if (field.getAnnotation(DataMeta.class) != null)
			{
				try
				{
					value = Tools.access(object, field);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
				map.put(Utils.getDataLabelFromField(field), value);
			}
		}

		return map;
	}

	/**
	 * Map model object to column/value pairs.
	 * 
	 * @param object
	 * @param columns
	 * @return
	 */
	protected static <T> Map<Column, Object> mapObjectToEntity(T object, Collection<Column> columns)
	{
		Map<Column, String> labels = getColumnsLabelMap(columns);

		Map<String, Field> modelFields = Utils.getLabelFieldMapByMeta(object.getClass(),
				new HashSet<String>(labels.values()), null);

		Map<Column, Object> map = new LinkedHashMap<Column, Object>();

		Field field = null;
		for (Column column : columns)
		{
			field = modelFields.get(labels.get(column));
			if (field != null)
			{
				try
				{
					map.put(column, Tools.access(object, field));
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}

		return map;
	}

	/**
	 * Map model object to entity column/value pairs.
	 * 
	 * @param object
	 * @param entity
	 * @param columns
	 * @return
	 */
	protected static <T> Map<Column, Object> mapObjectToEntity(T object, Column... columns)
	{
		return mapObjectToEntity(object, Tools.setOfArray(new LinkedHashSet<Column>(), columns));
	}

	/**
	 * Map model object to entity column/value pairs.
	 * 
	 * @param object
	 * @return
	 */
	protected static <T> Map<Column, Object> mapObjectToEntity(T object, Entity entity)
	{
		return mapObjectToEntity(object, getColumns(entity));
	}

	/**
	 * Map model object to values array according to the given entity columns.
	 * 
	 * @param object
	 * @param columns
	 * @return
	 */
	protected static <T> Object[] mapObjectToValues(T object, Column... columns)
	{
		if (object == null || columns == null)
		{
			return null;
		}

		Map<Column, String> labels = getColumnsLabelMap(columns);

		Map<String, Field> modelFields = Utils.getLabelFieldMapByMeta(object.getClass(),
				new HashSet<String>(labels.values()), null);

		Object[] values = new Object[columns.length];

		Column column = null;
		Field field = null;
		for (int i = 0; i < columns.length; i++)
		{
			column = columns[i];
			field = modelFields.get(labels.get(column));
			if (field != null)
			{
				try
				{
					values[i] = Tools.access(object, field);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		}

		return values;
	}

	/**
	 * Map column/values in reference by foreign key.
	 * 
	 * @param reference
	 * @param foreignKey
	 * @return
	 */
	protected static Map<Column, Object> mapValuesFromReferenceByForeignKey(Map<Column, Object> reference,
			ForeignKey foreignKey)
	{
		Column[] columns = foreignKey.columns();
		Column[] refers = foreignKey.reference().columns();
		Map<Column, Object> map = new HashMap<Column, Object>();
		for (int i = 0; i < columns.length; i++)
		{
			map.put(columns[i], reference.get(refers[i]));
		}
		return map;
	}

	/**
	 * Override the default meta map with parameters' placeholder if the value
	 * in object is not null.<br />
	 * Remove the expression map (which defined by DataMeta(value)) on
	 * overriding, so that the these column will not be retrieved.
	 * 
	 * @param sql
	 * @param object
	 * @param meta
	 * @param exprs
	 * @return
	 */
	protected static <T> Map<Column, Expression> overwriteColumnDefaults(SQL sql, T object,
			Map<Column, Expression> meta, Map<Column, Expression> exprs)
	{
		Map<Column, Object> data = mapObjectToEntity(object, meta.keySet());

		for (Entry<Column, Object> entry : data.entrySet())
		{
			if (entry.getValue() != null)
			{
				if (meta.containsKey(entry.getKey()))
				{
					meta.put(entry.getKey(), Utils.getDataParameterFromField(sql, entry.getKey().field()));
				}
				if (exprs != null)
				{
					exprs.remove(entry.getKey());
				}
			}
		}

		return meta;
	}

	protected static <T> T refreshObject(SQLKit kit, SQL sql, T object, Entity entity, EntityKey key,
			Set<Column> columns, Map<String, Object> genvals) throws SQLException
	{
		if (object == null || key == null)
		{
			return object;
		}

		if (entity == null)
		{
			entity = getEntityFromModelObject(sql, object);
		}

		if (columns == null)
		{
			columns = new LinkedHashSet<Column>();
			for (Item i : entity.items())
			{
				columns.add((Column) i);
			}
		}

		if (columns.isEmpty())
		{
			return object;
		}

		for (Column c : key.columns())
		{
			columns.remove(c);
		}

		Select sel = sql.from(entity) //
				.select(columns.toArray(new Column[0])) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();

		sel = sel.where(key.queryCondition());

		Map<String, Object> params = mapColumnToLabelByMeta(key.mapValues(object), genvals);

		Sequel sq = kit.execute(sel.toString(), params);
		try
		{
			Set<String> labels = new LinkedHashSet<String>();
			for (Column c : columns)
			{
				labels.add(getLabelFromColumnByMeta(c));
			}
			sq.mapRow(Utils.getFieldNameMapByMeta(object.getClass(), null, labels), object);
		}
		finally
		{
			if (sq != null)
			{
				sq.close();
			}
		}
		return object;
	}

	protected static <T> T refreshObject(SQLKit kit, SQL sql, T object, Entity entity, GenerateValueColumns generates,
			Set<Column> columns, Map<String, Object> genvals) throws SQLException
	{
		EntityKey key = null;

		if (generates != null && generates.abscols != null && generates.abscols.length > 0)
		{
			key = sql.provider().provideAbsoluteKey(entity, generates.abscols);
		}
		else
		{
			key = getUpdateKey(entity, object);
		}

		return refreshObject(kit, sql, object, entity, key, columns, genvals);
	}

	public static <T> T reloadObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return reloadObject(kit, sql, object, null);
	}

	public static <T> T reloadObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		return reloadObject(kit, sql, object, entity, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T reloadObject(SQLKit kit, SQL sql, T object, Entity entity, String scene) throws SQLException
	{
		if (object == null)
		{
			return null;
		}

		if (entity == null)
		{
			entity = getEntityFromModelObject(sql, object);
		}

		Select select = sql.from(entity) //
				.select(getColumnsFromEntityByModel(sql, object.getClass(), entity, null, null).toArray(new Column[0])) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();

		EntityKey key = getUpdateKey(entity, object);

		if (key != null)
		{
			select.where(key.queryCondition());
			return selectObject(kit, sql, select, (Class<T>) object.getClass(), scene,
					mapColumnToLabelByMeta(key.mapValues(object)));
		}
		else
		{
			return object;
		}
	}

	public static <T> T saveObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return saveObject(kit, sql, object, null);
	}

	public static <T> T saveObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return null;
		}

		if (entity == null)
		{
			entity = getEntityFromModelObject(sql, object);
		}

		if (entity.absoluteKey() != null && hasAllColumnsInModel(object.getClass(), entity.absoluteKey()))
		{
			if (!existsObject(kit, sql, object, entity))
			{
				insertObjectAlone(kit, sql, object, entity);
				insertObjectCascade(kit, sql, object, entity);
			}
			else
			{
				updateObject(kit, sql, object, entity);
				saveObjectCascade(kit, sql, object, entity);
			}
		}
		else
		{
			if (!existsObject(kit, sql, object, entity))
			{ // New record
				insertObjectAlone(kit, sql, object, entity);
			}
			else
			{
				updateObject(kit, sql, object, entity);
			}
			saveObjectCascade(kit, sql, object, entity);
		}

		return object;
	}

	public static <T> T saveObjectAlone(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return saveObjectAlone(kit, sql, object, null);
	}

	public static <T> T saveObjectAlone(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return null;
		}

		if (entity == null)
		{
			entity = getEntityFromModelObject(sql, object);
		}

		if (!existsObject(kit, sql, object, entity))
		{ // New record
			insertObjectAlone(kit, sql, object, entity);
		}
		else
		{
			updateObject(kit, sql, object, entity);
		}

		return object;
	}

	protected static <T> void saveObjectCascade(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		for (Field field : Tools.getFieldsHierarchy(object.getClass(), null).values())
		{
			if (isOneToMany(field))
			{
				saveOneToMany(kit, sql, object, entity, field);
			}
			else if (isOneToOneNeedSave(sql, entity, field))
			{
				saveOneToOne(kit, sql, object, entity, field);
			}
		}
	}

	/**
	 * Save the objects in referrer entity but whose absolute key are within the
	 * correspond values in the given children collection.
	 * 
	 * @param kit
	 * @param sql
	 * @param parent
	 * @param children
	 * @param abskey
	 * @param referrerEntity
	 * @throws SQLException
	 */
	protected static <T, R> void saveObjectsWithinAbsoluteKey(SQLKit kit, SQL sql, T parent,
			Collection<Object> children, final AbsoluteKey abskey, Entity referrerEntity) throws SQLException
	{
		Iterable<Object> withins = Canal.of(children).filter(new Filter<Object>()
		{
			@Override
			public boolean filter(Object el)
			{
				return Canal.of(abskey.mapValues(el).values()).first().orNull() != null;
			}
		});

		for (Object child : withins)
		{
			saveObject(kit, sql, child, referrerEntity);
		}
	}

	protected static <T> void saveOneToMany(SQLKit kit, SQL sql, T parent, Entity entity, Field field)
			throws SQLException
	{
		Collection<Object> many = null;
		try
		{
			many = Tools.access(parent, field);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		deleteOneToMany(kit, sql, parent, entity, field, true, many);

		saveOneToMany(kit, sql, parent, entity, field, many);

		insertOneToMany(kit, sql, parent, entity, field, many);
	}

	protected static <T> void saveOneToMany(SQLKit kit, SQL sql, T parent, Entity entity, Field field,
			Collection<Object> many) throws SQLException
	{
		if (many == null)
		{
			try
			{
				many = Tools.access(parent, field);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		if (many == null)
		{
			return;
		}

		OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);
		Class<?> manyClass = meta.model();
		Entity manyEntity = getEntityFromModelClass(sql, manyClass);
		AbsoluteKey abskey = manyEntity.absoluteKey();

		if (abskey != null)
		{
			saveObjectsWithinAbsoluteKey(kit, sql, parent, many, abskey, manyEntity);
		}
	}

	protected static <T> void saveOneToOne(SQLKit kit, SQL sql, T object, Entity entity, Field field)
			throws SQLException
	{
		Object val = null;

		try
		{
			val = Tools.access(object, field);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		if (val != null)
		{
			Entity other = getEntityFromModelObject(sql, val);
			saveObjectAlone(kit, sql, val, other);
			saveObjectCascade(kit, sql, val, other);
		}
	}

	protected static <T> Queryable selectAndParams(SQL sql, T object, RelationDefine rels, JoinMeta joins)
	{
		Entity origin = getEntityFromModelObject(sql, object);
		Entity target = getEntityFromModelClass(sql, rels.model());
		Entity first = null;

		ForeignKey key = null;
		Map<Column, Object> params = null;
		if (joins != null)
		{
			JoinDefine join = joins.value()[0];
			first = sql.view(join.entity());
			key = getForeignKey(join.key(), join.referred(), origin, first);
			params = key.mapValuesTo(object, key.entity() == first ? key.entity() : key.reference().entity());
		}
		else if (rels != null)
		{
			key = getForeignKey(rels.key(), rels.referred(), origin, target);
			params = key.mapValuesTo(object, rels.referred() ? key.reference().entity() : key.entity());
		}

		if (params != null && !hasNullValue(params))
		{
			Select sel = null;
			Entity last = null, curr = null;

			if (joins != null)
			{
				int i = 0;
				for (JoinDefine join : joins.value())
				{
					if (i == 0 && first != null)
					{
						curr = first;
					}
					else
					{
						curr = sql.view(join.entity());
					}
					curr.alias("T" + i);
					if (last == null)
					{
						sel = sql.from(curr).select();
					}
					else
					{
						sel = sel.innerJoin(curr, getForeignKey(join.key(), join.referred(), last, curr));
					}
					last = curr;
					i++;
				}
			}

			Column[] cols = getColumnsFromEntityByModel(sql, rels.model(), target, null, null).toArray(new Column[0]);

			if (sel != null)
			{ // Join with target
				sel = sel.innerJoin(target.alias("T"), getForeignKey(rels.key(), rels.referred(), last, target)) //
						.where(key.entity() == first ? key.queryCondition() : key.reference().queryCondition()) //
						.select(cols);
			}
			else
			{ // Query from target
				sel = sql.from(target) //
						.where(key.entity() == target ? key.queryCondition() : key.reference().queryCondition()) //
						.select(cols);
			}

			sel = sel.to(AbstractSelect.class).fillAliasByMeta();

			return new Queryable(sel, mapColumnToLabelByMeta(params));
		}
		else
		{
			return null;
		}
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Class<T> model, JSON params) throws SQLException
	{
		return selectObject(kit, sql, model, (String) null, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Class<T> model, Map<String, Object> params)
			throws SQLException
	{
		return selectObject(kit, sql, model, (String) null, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Class<T> model, Object... pkVals) throws SQLException
	{
		return selectObject(kit, sql, (String) null, model, pkVals);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Class<T> model, String scene, JSON params) throws SQLException
	{
		return selectObject(kit, sql, makeSelectByPrimaryKey(sql, model, null), model, scene, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Class<T> model, String scene, Map<String, Object> params)
			throws SQLException
	{
		return selectObject(kit, sql, makeSelectByPrimaryKey(sql, model, null), model, scene, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Select select, Class<T> model, JSON params)
			throws SQLException
	{
		return selectObject(kit, sql, select, model, (String) null, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Select select, Class<T> model, Map<String, Object> params)
			throws SQLException
	{
		return selectObject(kit, sql, select, model, (String) null, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Select select, Class<T> model, String scene, JSON params)
			throws SQLException
	{
		return setupObject(kit, sql, selectObjectAlone(kit, select, model, params), scene, true);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Select select, Class<T> model, String scene,
			Map<String, Object> params) throws SQLException
	{
		return setupObject(kit, sql, selectObjectAlone(kit, select, model, params), scene, true);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, String scene, Class<T> model, Object... pkVals)
			throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelClass(sql, model);

		PrimaryKey pk = entity.primaryKey();

		Map<String, Object> params = new LinkedHashMap<String, Object>();

		for (int i = 0; i < pk.columns().length; i++)
		{
			params.put(Utils.getDataLabelFromField(pk.columns()[i].field()), pkVals[i]);
		}

		return selectObject(kit, sql, model, scene, params);
	}

	public static <T> T selectObjectAlone(SQLKit kit, Select select, Class<T> model, JSON params) throws SQLException
	{
		Sequel s = kit.execute(select.toString(), params);
		try
		{
			return s.getRow(model, Utils.getFieldNameMapByMeta(model, null, getLabelsOfSelect(select)));
		}
		finally
		{
			s.close();
		}
	}

	public static <T> T selectObjectAlone(SQLKit kit, Select select, Class<T> model, Map<String, Object> params)
			throws SQLException
	{
		Sequel s = kit.execute(select.toString(), params);
		try
		{
			return s.getRow(model, Utils.getFieldNameMapByMeta(model, null, getLabelsOfSelect(select)));
		}
		finally
		{
			s.close();
		}
	}

	public static <T> T selectObjectAlone(SQLKit kit, SQL sql, Class<T> model, JSON params) throws SQLException
	{
		return selectObjectAlone(kit, makeSelectByPrimaryKey(sql, model, null), model, params);
	}

	public static <T> T selectObjectAlone(SQLKit kit, SQL sql, Class<T> model, Map<String, Object> params)
			throws SQLException
	{
		return selectObjectAlone(kit, makeSelectByPrimaryKey(sql, model, null), model, params);
	}

	public static <T> Canal<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model, JSON params)
			throws SQLException
	{
		return selectObjects(kit, sql, select, model, (String) null, params);
	}

	public static <T> Canal<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model,
			Map<String, Object> params) throws SQLException
	{
		return selectObjects(kit, sql, select, model, (String) null, params);
	}

	public static <T> Canal<T> selectObjects(final SQLKit kit, final SQL sql, Select select, Class<T> model,
			final String scene, JSON params) throws SQLException
	{
		return kit.execute(select.toString(), params) //
				.getRows(model, Utils.getFieldNameMapByMeta(model, null, getLabelsOfSelect(select))) //
				.map(new Mapper<T, T>()
				{
					@Override
					public T map(T el)
					{
						try
						{
							return setupObject(kit, sql, el, scene, true);
						}
						catch (Exception e)
						{
							throw new RuntimeException(e);
						}
					}
				});
	}

	public static <T> Collection<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model, String scene,
			JSON params, Collection<T> coll, int limit) throws SQLException
	{
		return selectObjects(kit, sql, select, model, scene, params).limit(limit)
				.collect(coll != null ? coll : new LinkedList<T>());
	}

	public static <T> Canal<T> selectObjects(final SQLKit kit, final SQL sql, Select select, Class<T> model,
			final String scene, Map<String, Object> params) throws SQLException
	{
		return kit.execute(select.toString(), params) //
				.getRows(model, Utils.getFieldNameMapByMeta(model, null, getLabelsOfSelect(select))) //
				.map(new Mapper<T, T>()
				{
					@Override
					public T map(T el)
					{
						try
						{
							return setupObject(kit, sql, el, scene, true);
						}
						catch (Exception e)
						{
							throw new RuntimeException(e);
						}
					}
				});
	}

	public static <T> Collection<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model, String scene,
			Map<String, Object> params, Collection<T> coll, int limit) throws SQLException
	{
		return selectObjects(kit, sql, select, model, scene, params).limit(limit)
				.collect(coll != null ? coll : new LinkedList<T>());
	}

	protected static Collection<Object> setCollection(Object object, Field field, Collection<Object> coll)
	{
		Collection<Object> c = null;
		try
		{
			c = Tools.access(object, field);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		if (c != null && coll != null)
		{
			if (c != coll)
			{
				c.clear();
				c.addAll(coll);
			}
			coll = c;
		}

		try
		{
			Tools.access(object, field, coll);
			return Tools.access(object, field);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	protected static <T> void setManyToOneMembers(SQLKit kit, SQL sql, T object, Field field, String scene,
			boolean fully) throws SQLException
	{
		boolean setable = false;
		try
		{
			setable = object != null && Tools.access(object, field) == null;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		if (setable)
		{
			ManyToOneMeta meta = field.getAnnotation(ManyToOneMeta.class);

			if (meta != null)
			{
				Class<?> model = meta.model();

				Queryable pair = null;
				if (field.getAnnotation(RedefineMeta.class) != null)
				{
					pair = getRedefinedQueryableObject(sql, object, field);
				}
				else
				{
					pair = selectAndParams(sql, object, new RelationDefine(meta), field.getAnnotation(JoinMeta.class));
				}

				if (pair != null && pair.getSelect() != null)
				{
					Select sel = pair.getSelect();
					Map<String, Object> param = pair.getParams();

					Sequel s = (param instanceof JSON ? kit.execute(sel.toString(), (JSON) param)
							: kit.execute(sel.toString(), param));
					Object another = s.getRow(model, Utils.getFieldNameMapByMeta(model, null));
					s.close();
					try
					{
						Tools.access(object, field, another);
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}

					setupObject(kit, sql, another, scene, fully && meta.fully());
				}
			}
		}
	}

	protected static <T> void setOneToManyMembers(SQLKit kit, SQL sql, T object, Field field, String scene,
			boolean fully) throws SQLException
	{
		OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);

		if (meta == null)
		{
			return;
		}

		Class<?> manyModel = meta.model();

		Queryable pair = null;
		if (field.getAnnotation(RedefineMeta.class) != null)
		{
			pair = getRedefinedQueryableObject(sql, object, field);
		}
		else
		{
			pair = selectAndParams(sql, object, new RelationDefine(meta), field.getAnnotation(JoinMeta.class));
		}

		if (pair != null && pair.getSelect() != null)
		{
			Select sel = pair.getSelect();
			Map<String, Object> param = pair.getParams();

			@SuppressWarnings({ "unchecked", "rawtypes" })
			Collection<Object> coll = (param instanceof JSON ? kit.execute(sel.toString(), (JSON) param)
					: kit.execute(sel.toString(), param)) //
							.getRows(new LinkedList(), manyModel, Utils.getFieldNameMapByMeta(manyModel, null));
			coll = setCollection(object, field, coll);

			Field manyToOne = getManyToOneField(manyModel, object.getClass());

			if (coll != null)
			{
				for (Object obj : coll)
				{
					if (manyToOne != null)
					{
						try
						{
							Tools.access(obj, manyToOne, object);
						}
						catch (Exception e)
						{
							throw new RuntimeException(e);
						}
					}
					setupObject(kit, sql, obj, scene, fully);
				}
			}
		}
	}

	protected static <T> void setOneToOneMembers(SQLKit kit, SQL sql, T object, Field field, String scene,
			boolean fully) throws SQLException
	{
		OneToOneMeta meta = field.getAnnotation(OneToOneMeta.class);

		if (meta == null)
		{
			return;
		}

		Class<?> oneModel = meta.model();

		Queryable pair = null;
		if (field.getAnnotation(RedefineMeta.class) != null)
		{
			pair = getRedefinedQueryableObject(sql, object, field);
		}
		else
		{
			pair = selectAndParams(sql, object, new RelationDefine(meta), field.getAnnotation(JoinMeta.class));
		}

		if (pair != null && pair.getSelect() != null)
		{
			Select sel = pair.getSelect();
			Map<String, Object> param = pair.getParams();

			Sequel s = (param instanceof JSON ? kit.execute(sel.toString(), (JSON) param)
					: kit.execute(sel.toString(), param));
			Object another = s.getRow(oneModel, Utils.getFieldNameMapByMeta(oneModel, null));
			s.close();
			try
			{
				Tools.access(object, field, another);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}

			Field oneToOne = getOneToOneField(oneModel, object.getClass());

			if (oneToOne != null)
			{
				try
				{
					Tools.access(another, oneToOne, object);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}

			setupObject(kit, sql, another, scene, fully);
		}
	}

	public static <T> T setupObject(SQLKit kit, SQL sql, T object, Field field, String scene, boolean fully)
			throws SQLException
	{
		if (object == null)
		{
			return null;
		}

		if (isNeedSetup(field.getAnnotation(ManyToOneMeta.class), scene))
		{
			setManyToOneMembers(kit, sql, object, field, scene, fully);
		}

		if (isNeedSetup(field.getAnnotation(OneToOneMeta.class), scene))
		{
			setOneToOneMembers(kit, sql, object, field, scene, fully);
		}

		if (fully && isNeedSetup(field.getAnnotation(OneToManyMeta.class), scene))
		{
			setOneToManyMembers(kit, sql, object, field, scene, fully);
		}

		return object;
	}

	public static <T> T setupObject(SQLKit kit, SQL sql, T object, String scene, boolean fully) throws SQLException
	{
		if (object == null)
		{
			return null;
		}

		for (Field field : Tools.getFieldsHierarchy(object.getClass(), null).values())
		{
			setupObject(kit, sql, object, field, scene, fully);
		}

		return object;
	}

	public static <T> int updateObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return updateObject(kit, sql, object, null);
	}

	public static <T> int updateObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return 0;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		Key key = getUpdateKey(entity, object);

		Update update = makeUpdate(sql, entity, object, key);

		if (update == null)
		{
			return 0;
		}

		update = update.where(key.queryCondition());

		Map<String, Object> params = makeParams(object, entity);

		MetaContext.check(kit, update, params);

		return kit.update(update.toString(), params);
	}
}
