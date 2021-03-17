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
import org.kernelab.basis.JSON;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Pair;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.basis.sql.Sequel;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.AbsoluteKey;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
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

		PrimaryKey key = entity.primaryKey();

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

	public static <T> int deleteObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object != null)
		{
			if (entity == null)
			{
				entity = Entitys.getEntityFromModelObject(sql, object);
			}

			deleteObjectCascade(kit, sql, object, entity);

			return deleteObjectAlone(kit, sql, object, entity);
		}
		else
		{
			return -1;
		}
	}

	public static <T> int deleteObjectAlone(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object != null)
		{
			if (entity == null)
			{
				entity = Entitys.getEntityFromModelObject(sql, object);
			}

			Pair<AbsoluteKey, Object> absVal = getAbsoluteKeyValue(kit, sql, object, entity);

			if (absVal != null)
			{
				Delete delete = sql.from(entity).where(absVal.key.queryCondition()).delete();

				Map<String, Object> params = mapColumnToLabelByMeta(absVal.key.columns()[0], absVal.value);

				return kit.update(delete.toString(), params);
			}
			else
			{
				PrimaryKey key = entity.primaryKey();

				Delete delete = sql.from(entity).where(key.queryCondition()).delete();

				Map<String, Object> params = mapColumnToLabelByMeta(key.mapValues(object));

				return kit.update(delete.toString(), params);
			}
		}
		else
		{
			return -1;
		}
	}

	public static <T, R> void deleteReferrerBeyondAbsoluteKeyValues(SQLKit kit, SQL sql, T parent, AbsoluteKey abskey,
			Iterable<Object> absKeyVals, ForeignKey key, Entity referrer, Class<R> referrerModel) throws SQLException
	{
		Column abscol = abskey.columns()[0];
		String absname = Utils.getDataLabelFromField(abscol.field());

		Condition cond = key.entity() == referrer ? key.queryCondition() : key.reference().queryCondition();
		if (absKeyVals.iterator().hasNext())
		{
			Condition beyond = abscol.notIn(sql.provider().provideParameter(absname));
			cond = sql.and(cond, beyond);
		}

		Select select = sql.from(referrer) //
				.where(cond) //
				.select(referrer.all()) //
				.as(AbstractSelect.class) //
				.fillAliasByMeta();

		Map<String, Object> params = mapColumnToLabelByMeta(key.mapValuesTo(parent, referrer));
		params.put(absname, absKeyVals);

		Collection<R> refs = selectObjects(kit, sql, select, referrerModel, new JSON().attrAll(params),
				new LinkedList<R>());

		for (R ref : refs)
		{
			deleteObject(kit, sql, ref, referrer);
		}
	}

	public static <T> void deleteObjectCascade(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
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
				deleteOneToMany(kit, sql, object, entity, field);
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

	public static <T> void deleteOneToMany(SQLKit kit, SQL sql, T object, Entity entity, Field field)
			throws SQLException
	{
		Collection<?> coll = null;
		try
		{
			coll = Tools.access(object, field);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (coll != null)
		{
			OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);
			Class<?> manyClass = meta.model();
			Entity manyEntity = getEntityFromModelClass(sql, manyClass);

			ForeignKey key = getForeignKey(meta.key(), meta.referred(), entity, manyEntity);
			deleteObjects(kit, sql, object, key, manyEntity);

			for (Object o : coll)
			{
				deleteObjectCascade(kit, sql, o, manyEntity);
				deleteObjectAlone(kit, sql, o, manyEntity);
			}
		}
	}

	public static <T> void deleteOneToOne(SQLKit kit, SQL sql, T object, Field field) throws SQLException
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

	public static <T> Pair<AbsoluteKey, Object> getAbsoluteKeyValue(SQLKit kit, SQL sql, T object, Entity entity)
	{
		if (object == null)
		{
			return null;
		}

		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		AbsoluteKey abskey = null;
		if ((abskey = entity.absoluteKey()) == null)
		{
			return null;
		}

		for (Entry<Column, Object> entry : abskey.mapValues(object).entrySet())
		{
			if (entry.getValue() != null)
			{
				return new Pair<AbsoluteKey, Object>(abskey, entry.getValue());
			}
			break;
		}

		return null;
	}

	public static Set<Column> getColumns(Entity entity)
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

	public static Column[] getColumnsArray(Entity entity)
	{
		Set<Column> columns = getColumns(entity);
		return columns.toArray(new Column[columns.size()]);
	}

	public static Map<Column, String> getColumnsLabelMap(Collection<Column> columns)
	{
		Map<Column, String> map = new LinkedHashMap<Column, String>();

		for (Column column : columns)
		{
			map.put(column, Utils.getDataLabelFromField(column.field()));
		}

		return map;
	}

	public static Map<Column, String> getColumnsLabelMap(Column... columns)
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
	public static ForeignKey getForeignKey(String name, boolean secondAsReference, Entity first, Entity second)
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
	public static Pair<Short, Column[]> getGenerateValueColumns(Entity entity)
	{
		GenerateValueMeta meta = null;

		List<Column> columns = new LinkedList<Column>();

		Column column = null;

		for (Field field : Tools.getFieldsHierarchy(entity.getClass(), null).values())
		{
			if (AbstractEntity.isColumn(field))
			{
				meta = field.getAnnotation(GenerateValueMeta.class);

				if (meta != null)
				{
					try
					{
						column = (Column) Tools.access(entity, field);

						switch (meta.strategy())
						{
							case GenerateValueMeta.AUTO:
								try
								{
									columns.add(column);
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								break;

							case GenerateValueMeta.IDENTITY:
								return new Pair<Short, Column[]>(GenerateValueMeta.IDENTITY, new Column[] { column });
						}
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}

				}
			}
		}

		if (columns.isEmpty())
		{
			return null;
		}
		else
		{
			return new Pair<Short, Column[]>(GenerateValueMeta.AUTO, columns.toArray(new Column[columns.size()]));
		}
	}

	public static String getLabelFromColumnByMeta(Column column)
	{
		return Utils.getDataLabelFromField(column.field());
	}

	public static Field getManyToOneField(Class<?> manyClass, Class<?> oneClass)
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

	public static Field getOneToOneField(Class<?> oneClass, Class<?> anotherClass)
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

	public static <T> Queryable getRedefinedQueryableObject(SQL sql, T object, Field field)
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

	public static boolean hasNullValue(Map<?, Object> param)
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

	public static <T> boolean hasNullValue(T object, Entity entity, Column... columns)
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

	public static <T> int insertObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object != null)
		{
			if (entity == null)
			{
				entity = Entitys.getEntityFromModelObject(sql, object);
			}

			int res = insertObjectAlone(kit, sql, object, entity);

			insertObjectCascade(kit, sql, object, entity);

			return res;
		}
		else
		{
			return -1;
		}
	}

	public static <T> int insertObjectAlone(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (entity == null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		Pair<Short, Column[]> generates = Entitys.getGenerateValueColumns(entity);
		Column[] gencols = generates != null ? generates.value : null;

		AbstractTable table = entity.as(AbstractTable.class);
		Map<Column, Expression> insertMeta = table.getColumnDefaultExpressions(null);
		insertMeta = overwriteColumnDefaults(sql, object, insertMeta);

		Map<String, Object> params = Entitys.mapObjectByMeta(object);

		PreparedStatement stmt = null;

		Insert insert = null;

		if (generates == null)
		{
			insert = table.insertByMetaMap(insertMeta);
			stmt = kit.prepareStatement(insert.toString(), params);
		}
		else if (generates.key == GenerateValueMeta.IDENTITY)
		{
			for (Column column : gencols)
			{
				insertMeta.remove(column);
			}
			insert = table.insertByMetaMap(insertMeta);
			stmt = kit.prepareStatement(insert.toString(), params, true);
		}
		else if (generates.key == GenerateValueMeta.AUTO)
		{
			Set<Column> genset = Tools.setOfArray(new LinkedHashSet<Column>(), gencols);
			Map<Column, Object> genvals = mapObjectToEntity(object, gencols);
			for (Entry<Column, Object> entry : genvals.entrySet())
			{
				if (entry.getValue() != null)
				{
					Column column = entry.getKey();
					genset.remove(column);
				}
			}

			if (genset.isEmpty())
			{
				gencols = null;
				insert = table.insertByMetaMap(insertMeta);
				stmt = kit.prepareStatement(insert.toString(), params);
			}
			else
			{
				gencols = genset.toArray(new Column[genset.size()]);
				String[] genames = new String[genset.size()];
				int i = 0;
				for (Column column : genset)
				{
					genames[i] = column.name();
					i++;
				}
				insert = table.insertByMetaMap(insertMeta);
				stmt = kit.prepareStatement(insert.toString(), params, genames);
			}
		}

		Sequel seq = kit.execute(stmt, params);

		// Set generate values
		if (gencols != null)
		{
			Sequel gen = seq.getGeneratedKeys();
			Object val = null;
			Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass(), null);
			for (int i = 0; i < gencols.length; i++)
			{
				val = gen.getValueObject(i + 1);
				try
				{
					Tools.access(object, fields.get(Utils.getDataLabelFromField(gencols[i].field())), val);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return seq.getUpdateCount();
	}

	public static <T> void insertObjectCascade(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
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

	public static <T> void insertOneToMany(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		Collection<?> coll = null;

		try
		{
			coll = Tools.access(object, field);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (coll != null)
		{
			Entity entity = null;

			for (Object o : coll)
			{
				if (entity == null)
				{
					entity = getEntityFromModelObject(sql, o);
				}
				insertObjectAlone(kit, sql, o, entity);
				insertObjectCascade(kit, sql, o, entity);
			}
		}
	}

	public static <T> void insertOneToOne(SQLKit kit, SQL sql, T object, Field field) throws SQLException
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

	public static boolean isManyToOne(Field field)
	{
		return field.getAnnotation(ManyToOneMeta.class) != null;
	}

	public static boolean isOneToMany(Field field)
	{
		return field.getAnnotation(OneToManyMeta.class) != null;
	}

	public static boolean isOneToOneNeedSave(SQL sql, Entity entity, Field field)
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

	public static <T> Update makeUpdate(SQL sql, Entity entity, T object)
	{
		if (entity == null && object == null)
		{
			return null;
		}

		if (entity == null && object != null)
		{
			entity = Entitys.getEntityFromModelObject(sql, object);
		}

		AbstractTable table = entity.as(AbstractTable.class);

		Map<Column, Expression> meta = table.getColumnDefaultExpressions(entity.primaryKey());
		if (object != null)
		{
			meta = overwriteColumnDefaults(sql, object, meta);
		}

		return table.updateByMetaMap(meta);
	}

	public static Map<String, Object> mapColumnToLabelByMeta(Column column, Object value)
	{
		return mapColumnToLabelByMeta(new HashMap<String, Object>(), column, value);
	}

	/**
	 * Map key to label defined by DataMeta.
	 * 
	 * @param data
	 * @return
	 */
	public static Map<String, Object> mapColumnToLabelByMeta(Map<Column, Object> data)
	{
		Map<String, Object> map = new HashMap<String, Object>();

		for (Entry<Column, Object> entry : data.entrySet())
		{
			mapColumnToLabelByMeta(map, entry.getKey(), entry.getValue());
		}

		return map;
	}

	public static Map<String, Object> mapColumnToLabelByMeta(Map<String, Object> result, Column column, Object value)
	{
		result.put(getLabelFromColumnByMeta(column), value);
		return result;
	}

	public static Map<Column, String> mapLabelsFromColumnsByMeta(Column... columns)
	{
		Map<Column, String> map = new LinkedHashMap<Column, String>();

		for (Column column : columns)
		{
			map.put(column, getLabelFromColumnByMeta(column));
		}

		return map;
	}

	public static <T> Map<String, Object> mapObjectByMeta(T object)
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
	public static <T> Map<Column, Object> mapObjectToEntity(T object, Collection<Column> columns)
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
	public static <T> Map<Column, Object> mapObjectToEntity(T object, Column... columns)
	{
		return mapObjectToEntity(object, Tools.setOfArray(new LinkedHashSet<Column>(), columns));
	}

	/**
	 * Map model object to entity column/value pairs.
	 * 
	 * @param object
	 * @return
	 */
	public static <T> Map<Column, Object> mapObjectToEntity(T object, Entity entity)
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
	public static Map<Column, Object> mapValuesFromReferenceByForeignKey(Map<Column, Object> reference,
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

	public static <T> Map<Column, Expression> overwriteColumnDefaults(SQL sql, T object, Map<Column, Expression> meta)
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

	public static <T> T saveObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		if (object != null)
		{
			Entity entity = getEntityFromModelObject(sql, object);

			Pair<Short, Column[]> generates = Entitys.getGenerateValueColumns(entity);

			AbsoluteKey abskey = null;

			if (generates != null && hasNullValue(object, entity, generates.value))
			{ // Missing values in generated columns
				insertObjectAlone(kit, sql, object, entity);
				insertObjectCascade(kit, sql, object, entity);
			}
			else if ((abskey = entity.absoluteKey()) != null)
			{
				// TODO update or insert by abskey
				// TODO saveCascade
			}
			else
			{
				if (countObject(kit, sql, object, entity) == 0)
				{ // New record
					insertObjectAlone(kit, sql, object, entity);
				}
				else
				{
					deleteObjectCascade(kit, sql, object, entity);
					updateObject(kit, sql, object);
				}
				insertObjectCascade(kit, sql, object, entity);
			}
		}
		return object;
	}

	public static <T> T saveObjectAlone(SQLKit kit, SQL sql, T object) throws SQLException
	{
		if (object != null)
		{
			Entity entity = getEntityFromModelObject(sql, object);

			Pair<Short, Column[]> generates = Entitys.getGenerateValueColumns(entity);

			if (generates != null && hasNullValue(object, entity, generates.value))
			{ // Missing values in generated columns
				insertObjectAlone(kit, sql, object, entity);
			}
			// TODO save by abskey
			else
			{
				if (countObject(kit, sql, object, entity) == 0)
				{ // New record
					insertObjectAlone(kit, sql, object, entity);
				}
				else
				{
					updateObject(kit, sql, object);
				}
			}
		}
		return object;
	}

	public static <T> void saveObjectCascade(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
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
				// TODO deleteOneToOne(kit, sql, object, field);
			}
		}
	}

	public static <T> void saveOneToMany(SQLKit kit, SQL sql, T parent, Entity entity, Field field) throws SQLException
	{
		Collection<Object> coll = null;
		try
		{
			coll = Tools.access(parent, field);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (coll == null)
		{
			return;
		}

		OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);
		Class<?> manyClass = meta.model();
		Entity manyEntity = getEntityFromModelClass(sql, manyClass);
		ForeignKey key = getForeignKey(meta.key(), meta.referred(), entity, manyEntity);
		final AbsoluteKey abskey = manyEntity.absoluteKey();

		if (abskey != null)
		{
			Iterable<Object> absKeyVals = Canal.of(coll).map(new Mapper<Object, Object>()
			{
				@Override
				public Object map(Object el)
				{
					return Canal.of(abskey.mapValues(el).values()).first().get();
				}
			});
			deleteReferrerBeyondAbsoluteKeyValues(kit, sql, parent, abskey, absKeyVals, key, manyEntity, manyClass);
		}
		else
		{
			// TODO
			deleteObjects(kit, sql, parent, key, manyEntity);

			for (Object o : coll)
			{
				deleteObjectCascade(kit, sql, o, manyEntity);
				deleteObjectAlone(kit, sql, o, manyEntity);
			}
		}
	}

	public static <T> Queryable selectAndParams(SQL sql, T object, RelationDefine rels, JoinMeta joins)
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
			params = key.mapValuesTo(object, key.entity() == target ? key.entity() : key.reference().entity());
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

			sel = sel.as(AbstractSelect.class).fillAliasByMeta();

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
				.as(AbstractSelect.class) //
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

	public static Collection<Object> setCollection(Object object, Field field, Collection<Object> coll)
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

	public static <T> void setManyToOneMembers(SQLKit kit, SQL sql, T object, Field field, boolean fully)
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

	public static <T> void setOneToManyMembers(SQLKit kit, SQL sql, T object, Field field, boolean fully)
			throws SQLException
	{
		OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);

		if (meta != null)
		{
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
	}

	public static <T> void setOneToOneMembers(SQLKit kit, SQL sql, T object, Field field, boolean fully)
			throws SQLException
	{
		OneToOneMeta meta = field.getAnnotation(OneToOneMeta.class);

		if (meta != null)
		{
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
	}

	public static <T> void setupObject(SQLKit kit, SQL sql, T object, boolean fully) throws SQLException
	{
		if (object != null)
		{
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
	}

	public static <T> int updateObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelObject(sql, object);
		PrimaryKey key = entity.primaryKey();

		Update update = makeUpdate(sql, entity, object) //
				.where(key.queryCondition());

		Map<String, Object> params = makeParams(object, entity);

		return kit.update(update.toString(), params);
	}
}
