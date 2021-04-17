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
import org.kernelab.basis.Filter;
import org.kernelab.basis.JSON;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.AbsoluteKey;
import org.kernelab.dougong.core.ddl.EntityKey;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;
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

	public static <T> int countObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object == null)
		{
			return 0;
		}

		if (entity == null)
		{
			entity = getEntityFromModelObject(sql, object);
		}

		EntityKey key = getUpdateKey(entity);

		if (key == null)
		{
			return 0;
		}

		Select sel = sql.from(entity) //
				.where(key.queryCondition()) //
				.select(sql.expr("COUNT(1)"));

		Map<String, Object> param = mapColumnToLabelByMeta(key.mapValues(object));

		ResultSet rs = kit.query(sel.toString(), param);

		if (rs.next())
		{
			return rs.getInt(1);
		}
		else
		{
			return 0;
		}
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

		EntityKey key = getUpdateKey(entity);

		if (key == null)
		{
			return 0;
		}

		Delete delete = sql.from(entity) //
				.where(key.queryCondition()) //
				.delete();

		Map<String, Object> params = mapColumnToLabelByMeta(key.mapValues(object));

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
				deleteOneToMany(kit, sql, object, entity, field, null);
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

		return kit.update(delete.toString(), params);
	}

	/**
	 * Delete the objects in referrer entity but whose absolute key are beyond
	 * the correspond values in the given children collection.
	 * 
	 * @param kit
	 * @param sql
	 * @param parent
	 * @param children
	 * @param abskey
	 * @param key
	 * @param referrerModel
	 * @param referrerEntity
	 * @throws SQLException
	 */
	protected static <T, R> void deleteObjectsBeyondAbsoluteKey(SQLKit kit, SQL sql, T parent,
			Collection<Object> children, final AbsoluteKey abskey, ForeignKey key, Class<R> referrerModel,
			Entity referrerEntity) throws SQLException
	{
		Condition cond = key.entity() == referrerEntity ? key.queryCondition() : key.reference().queryCondition();

		Column abscol = abskey.columns()[0];
		String absname = Utils.getDataLabelFromField(abscol.field());

		Iterable<Object> absKeyVals = Canal.of(children).map(new Mapper<Object, Object>()
		{
			@Override
			public Object map(Object el)
			{
				return Canal.of(abskey.mapValues(el).values()).first().orNull();
			}
		}).filter(new Filter<Object>()
		{
			@Override
			public boolean filter(Object el)
			{
				return el != null;
			}
		});

		boolean gotKey = absKeyVals.iterator().hasNext();

		if (gotKey)
		{
			Condition beyond = abscol.notIn(sql.provider().provideParameter(absname));
			cond = sql.and(cond, beyond);
		}

		Select select = sql.from(referrerEntity) //
				.where(cond) //
				.select(referrerEntity.all()) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();

		Map<String, Object> params = mapColumnToLabelByMeta(key.mapValuesTo(parent, referrerEntity));
		if (gotKey)
		{
			params.put(absname, absKeyVals);
		}

		for (R miss : selectObjects(kit, sql, select, referrerModel, new JSON().attrAll(params), new LinkedList<R>()))
		{
			deleteObject(kit, sql, miss, referrerEntity);
		}
	}

	protected static <T> void deleteOneToMany(SQLKit kit, SQL sql, T parent, Entity entity, Field field,
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
				e.printStackTrace();
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
		ForeignKey key = getForeignKey(meta.key(), meta.referred(), entity, manyEntity);

		if (abskey != null)
		{
			deleteObjectsBeyondAbsoluteKey(kit, sql, parent, many, abskey, key, manyClass, manyEntity);
		}
		else
		{
			deleteObjects(kit, sql, parent, key, manyEntity);

			for (Object child : many)
			{
				deleteObjectCascade(kit, sql, child, manyEntity);
				deleteObjectAlone(kit, sql, child, manyEntity);
			}
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
			e.printStackTrace();
		}
		if (val != null)
		{
			deleteObjectAlone(kit, sql, val, null);
		}
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
					e.printStackTrace();
				}
			}
		}

		return columns;
	}

	public static String getColumnSelectExpression(Column column)
	{
		DataMeta meta = column.field() == null ? null : column.field().getAnnotation(DataMeta.class);
		if (meta == null || meta.select() == null || meta.select().length() == 0)
		{
			return null;
		}
		else
		{
			return meta.select();
		}
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

	protected static Class<? extends Entity> getEntityClassFromModel(Class<?> modelClass)
	{
		EntityMeta meta = modelClass.getAnnotation(EntityMeta.class);
		return meta != null ? meta.entity() : null;
	}

	protected static <T> Entity getEntityFromModelClass(SQL sql, Class<T> model)
	{
		return sql.view(getEntityClassFromModel(model));
	}

	protected static <T> Entity getEntityFromModelObject(SQL sql, T object)
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
						ex.printStackTrace();
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
			e.printStackTrace();
		}

		return null;
	}

	public static EntityKey getUpdateKey(Entity entity)
	{
		EntityKey key = null;

		if ((key = entity.absoluteKey()) != null)
		{
			return key;
		}
		else
		{
			return entity.primaryKey();
		}
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
			kit.update(insert.toString(), params);
			return null;
		}
		else if (generates.strategy == GenerateValueMeta.IDENTITY)
		{
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
		insertMeta = overwriteColumnDefaults(sql, object, insertMeta);

		Map<String, Object> params = Entitys.mapObjectByMeta(object);
		Insert insert = null;
		Column[] returns = null;

		if (generates == null)
		{
			insert = table.insertByMetaMap(insertMeta);
		}
		else if (generates.strategy == GenerateValueMeta.IDENTITY)
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
			if (returnSet.next())
			{
				Object val = null;
				Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass(), null);
				for (int i = 0; i < returns.length; i++)
				{
					val = returnSet.getObject(i + 1);
					try
					{
						Tools.access(object, fields.get(Utils.getDataLabelFromField(returns[i].field())), val);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
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
				e.printStackTrace();
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
				e.printStackTrace();
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
			e.printStackTrace();
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

	public static <T> Map<String, Object> makeParams(SQL sql, T object)
	{
		return makeParams(object, getEntityFromModelObject(sql, object));
	}

	public static <T> Map<String, Object> makeParams(T object, Entity entity)
	{
		return mapColumnToLabelByMeta(mapObjectToEntity(object, entity));
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

		if (object != null)
		{
			meta = overwriteColumnDefaults(sql, object, meta);
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
		Map<String, Object> map = new HashMap<String, Object>();

		for (Entry<Column, Object> entry : data.entrySet())
		{
			mapColumnToLabelByMeta(map, entry.getKey(), entry.getValue());
		}

		return map;
	}

	protected static Map<String, Object> mapColumnToLabelByMeta(Map<String, Object> result, Column column, Object value)
	{
		result.put(getLabelFromColumnByMeta(column), value);
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
					e.printStackTrace();
					value = null;
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
					e.printStackTrace();
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

	protected static <T> Map<Column, Expression> overwriteColumnDefaults(SQL sql, T object,
			Map<Column, Expression> meta)
	{
		Map<Column, Object> data = mapObjectToEntity(object, meta.keySet());

		for (Entry<Column, Object> entry : data.entrySet())
		{
			if (entry.getValue() != null && meta.containsKey(entry.getKey()))
			{
				meta.put(entry.getKey(), Utils.getDataParameterFromField(sql, entry.getKey().field()));
			}
		}

		return meta;
	}

	public static <T> T refreshObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		return refreshObject(kit, sql, object, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T refreshObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
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
				.select(entity.all()) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();

		EntityKey key = getUpdateKey(entity);

		if (key != null)
		{
			select.where(key.queryCondition());
			return selectObject(kit, sql, select, (Class<T>) object.getClass(),
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

		GenerateValueColumns generates = Entitys.getGenerateValueColumns(entity);

		if (generates != null && hasNullValue(object, entity, generates.columns))
		{ // Missing values in generated columns
			insertObjectAlone(kit, sql, object, entity);
			insertObjectCascade(kit, sql, object, entity);
		}
		else if (entity.absoluteKey() != null)
		{
			if (countObject(kit, sql, object, entity) == 0)
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
			if (countObject(kit, sql, object, entity) == 0)
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

		GenerateValueColumns generates = Entitys.getGenerateValueColumns(entity);

		if (generates != null && hasNullValue(object, entity, generates.columns))
		{ // Missing values in generated columns
			insertObjectAlone(kit, sql, object, entity);
		}
		else
		{
			if (countObject(kit, sql, object, entity) == 0)
			{ // New record
				insertObjectAlone(kit, sql, object, entity);
			}
			else
			{
				updateObject(kit, sql, object, entity);
			}
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
			e.printStackTrace();
		}

		if (many == null)
		{
			return;
		}

		deleteOneToMany(kit, sql, parent, entity, field, many);

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
				e.printStackTrace();
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
			e.printStackTrace();
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
					curr.alias("t" + i);
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

			if (sel != null)
			{ // Join with target
				sel = sel.innerJoin(target.alias("t"), getForeignKey(rels.key(), rels.referred(), last, target)) //
						.where(key.entity() == first ? key.queryCondition() : key.reference().queryCondition()) //
						.select(target.all());
			}
			else
			{ // Query from target
				sel = sql.from(target) //
						.where(key.entity() == target ? key.queryCondition() : key.reference().queryCondition()) //
						.select(target.all());
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
		Entity entity = Entitys.getEntityFromModelClass(sql, model);
		Select select = sql.from(entity) //
				.where(entity.primaryKey().queryCondition()) //
				.select(entity.all()) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();
		return selectObject(kit, sql, select, model, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Class<T> model, Map<String, Object> params)
			throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelClass(sql, model);
		Select select = sql.from(entity) //
				.where(entity.primaryKey().queryCondition()) //
				.select(entity.all()) //
				.to(AbstractSelect.class) //
				.fillAliasByMeta();
		return selectObject(kit, sql, select, model, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Select select, Class<T> model, JSON params)
			throws SQLException
	{
		T object = kit.execute(select.toString(), params) //
				.getRow(model, Utils.getFieldNameMapByMetaFully(model, null));
		setupObject(kit, sql, object, true);
		return object;
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Select select, Class<T> model, Map<String, Object> params)
			throws SQLException
	{
		T object = kit.execute(select.toString(), params) //
				.getRow(model, Utils.getFieldNameMapByMetaFully(model, null));
		setupObject(kit, sql, object, true);
		return object;
	}

	public static <T> Collection<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model, JSON params,
			Collection<T> coll) throws SQLException
	{
		return selectObjects(kit, sql, select, model, params, coll, -1);
	}

	public static <T> Collection<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model, JSON params,
			Collection<T> coll, int limit) throws SQLException
	{
		coll = kit.execute(select.toString(), params).getRows(coll, model,
				Utils.getFieldNameMapByMetaFully(model, null), limit);
		if (coll != null)
		{
			for (T t : coll)
			{
				setupObject(kit, sql, t, true);
			}
		}
		return coll;
	}

	public static <T> Collection<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model,
			Map<String, Object> params, Collection<T> coll) throws SQLException
	{
		return selectObjects(kit, sql, select, model, params, coll, -1);
	}

	public static <T> Collection<T> selectObjects(SQLKit kit, SQL sql, Select select, Class<T> model,
			Map<String, Object> params, Collection<T> coll, int limit) throws SQLException
	{
		coll = kit.execute(select.toString(), params).getRows(coll, model,
				Utils.getFieldNameMapByMetaFully(model, null), limit);
		if (coll != null)
		{
			for (T t : coll)
			{
				setupObject(kit, sql, t, true);
			}
		}
		return coll;
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
			e.printStackTrace();
		}
		return null;
	}

	protected static <T> void setManyToOneMembers(SQLKit kit, SQL sql, T object, Field field, boolean fully)
			throws SQLException
	{
		boolean setable = false;
		try
		{
			setable = object != null && Tools.access(object, field) == null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
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

					Object another = (param instanceof JSON ? kit.execute(sel.toString(), (JSON) param)
							: kit.execute(sel.toString(), param)) //
									.getRow(model, Utils.getFieldNameMapByMeta(model, null));
					try
					{
						Tools.access(object, field, another);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					setupObject(kit, sql, another, fully && meta.fully());
				}
			}
		}
	}

	protected static <T> void setOneToManyMembers(SQLKit kit, SQL sql, T object, Field field, boolean fully)
			throws SQLException
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
						}
					}
					setupObject(kit, sql, obj, fully);
				}
			}
		}
	}

	protected static <T> void setOneToOneMembers(SQLKit kit, SQL sql, T object, Field field, boolean fully)
			throws SQLException
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

			Object another = (param instanceof JSON ? kit.execute(sel.toString(), (JSON) param)
					: kit.execute(sel.toString(), param)) //
							.getRow(oneModel, Utils.getFieldNameMapByMeta(oneModel, null));
			try
			{
				Tools.access(object, field, another);
			}
			catch (Exception e)
			{
				e.printStackTrace();
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
				}
			}

			setupObject(kit, sql, another, fully);
		}
	}

	protected static <T> void setupObject(SQLKit kit, SQL sql, T object, boolean fully) throws SQLException
	{
		if (object == null)
		{
			return;
		}

		Collection<Field> fields = Tools.getFieldsHierarchy(object.getClass(), null).values();

		for (Field field : fields)
		{
			if (field.getAnnotation(ManyToOneMeta.class) != null)
			{
				setManyToOneMembers(kit, sql, object, field, fully);
			}
		}

		for (Field field : fields)
		{
			if (field.getAnnotation(OneToOneMeta.class) != null)
			{
				setOneToOneMembers(kit, sql, object, field, fully);
			}
		}

		if (fully)
		{
			for (Field field : fields)
			{
				if (field.getAnnotation(OneToManyMeta.class) != null)
				{
					setOneToManyMembers(kit, sql, object, field, fully);
				}
			}
		}
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

		Key key = getUpdateKey(entity);

		Update update = makeUpdate(sql, entity, object, key) //
				.where(key.queryCondition());

		Map<String, Object> params = makeParams(object, entity);

		return kit.update(update.toString(), params);
	}
}
