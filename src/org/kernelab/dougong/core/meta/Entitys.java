package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
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

import org.kernelab.basis.JSON;
import org.kernelab.basis.Pair;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.basis.sql.Sequel;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
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
	public static <T> int deleteObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object != null)
		{
			if (entity == null)
			{
				entity = Entitys.getEntityFromModelClass(sql, object.getClass());
			}

			PrimaryKey key = entity.primaryKey();

			Delete delete = sql.from(entity).where(key.queryCondition()).delete();

			Map<String, Object> params = mapColumnToLabelByMeta(key.mapValues(object));

			return kit.update(delete.toString(), params);
		}
		else
		{
			return -1;
		}
	}

	public static <T> void deleteObjectCascade(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (object != null)
		{
			if (entity == null)
			{
				entity = Entitys.getEntityFromModelClass(sql, object.getClass());
			}

			for (Field field : object.getClass().getDeclaredFields())
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
	}

	public static <T> int deleteObjects(SQLKit kit, SQL sql, T referenceObject, ForeignKey key, Entity referrer)
			throws SQLException
	{
		if (referenceObject != null)
		{
			Delete delete = sql.from(referrer) //
					.where(key.entity() == referrer ? key.queryCondition() : key.reference().queryCondition()) //
					.delete();

			Map<String, Object> params = mapColumnToLabelByMeta(key.mapValuesTo(referenceObject, referrer));

			return kit.update(delete.toString(), params);
		}
		else
		{
			return 0;
		}
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
				deleteObject(kit, sql, o, manyEntity);
			}
		}
	}

	public static <T> void deleteOneToOne(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		try
		{
			deleteObject(kit, sql, Tools.access(object, field), null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Set<Column> getColumns(Entity entity)
	{
		Set<Column> columns = new LinkedHashSet<Column>();

		for (Field field : entity.getClass().getDeclaredFields())
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

		for (Field field : entity.getClass().getDeclaredFields())
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
		for (Field field : manyClass.getDeclaredFields())
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
		for (Field field : oneClass.getDeclaredFields())
		{
			if (field.getAnnotation(OneToOneMeta.class) != null //
					&& Tools.isSubClass(anotherClass, field.getType()))
			{
				return field;
			}
		}
		return null;
	}

	public static <T> int insertObject(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		if (entity == null)
		{
			entity = Entitys.getEntityFromModelClass(sql, object.getClass());
		}

		Pair<Short, Column[]> generates = Entitys.getGenerateValueColumns(entity);
		Column[] gencols = generates != null ? generates.value : null;

		AbstractTable table = entity.as(AbstractTable.class);
		Map<Column, Expression> insertMeta = table.getColumnDefaultExpressions();
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
			Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass());
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
			entity = Entitys.getEntityFromModelClass(sql, object.getClass());
		}

		for (Field field : object.getClass().getDeclaredFields())
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

	public static <T> void insertOneToMany(SQLKit kit, SQL sql, T object, Field field)
	{
		try
		{
			Collection<?> col = Tools.access(object, field);

			Entity entity = null;

			for (Object o : col)
			{
				if (entity == null)
				{
					entity = getEntityFromModelClass(sql, o.getClass());
				}
				insertObject(kit, sql, o, entity);
				insertObjectCascade(kit, sql, o, entity);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static <T> void insertOneToOne(SQLKit kit, SQL sql, T object, Field field)
	{
		try
		{
			Object o = Tools.access(object, field);
			if (o != null)
			{
				Entity entity = getEntityFromModelClass(sql, o.getClass());
				insertObject(kit, sql, o, entity);
				insertObjectCascade(kit, sql, o, entity);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isManyToOne(Field field)
	{
		return field.getAnnotation(ManyToOneMeta.class) != null;
	}

	public static <T> boolean isMissingValue(T object, Entity entity, Column... columns)
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
			map.put(getLabelFromColumnByMeta(entry.getKey()), entry.getValue());
		}

		return map;
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

		for (Field field : object.getClass().getDeclaredFields())
		{
			DataMeta meta = field.getAnnotation(DataMeta.class);

			if (meta != null)
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
				new HashSet<String>(labels.values()));

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

	public static <T> void saveObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		if (object != null)
		{
			Entity entity = getEntityFromModelClass(sql, object.getClass());

			Pair<Short, Column[]> generates = Entitys.getGenerateValueColumns(entity);

			if (generates != null && isMissingValue(object, entity, generates.value))
			{ // Missing values in generated columns
				insertObject(kit, sql, object, entity);
				insertObjectCascade(kit, sql, object, entity);
			}
			else
			{
				deleteObjectCascade(kit, sql, object, entity);
				updateObject(kit, sql, object);
				insertObjectCascade(kit, sql, object, entity);
			}
		}
	}

	public static <T> Pair<Select, Map<Column, Object>> selectAndParams(SQL sql, T object, RelationDefine meta,
			JoinMeta joinMeta)
	{
		Select sel = null;
		Entity first = null, last = null, curr = null;

		if (joinMeta != null)
		{
			int i = 0;
			for (JoinDefine join : joinMeta.joins())
			{
				curr = sql.view(join.entity());
				curr.alias("t" + i);
				if (first == null)
				{
					first = curr;
				}
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

		Entity origin = getEntityFromModelClass(sql, object.getClass());

		Entity target = getEntityFromModelClass(sql, meta.model());

		Map<Column, Object> params = null;

		if (sel != null)
		{ // Join with target
			JoinDefine firstJoin = joinMeta.joins()[0];
			ForeignKey key = getForeignKey(firstJoin.key(), firstJoin.referred(), origin, first);
			sel = sel.innerJoin(target.alias("t"), getForeignKey(meta.key(), meta.referred(), last, target)) //
					.where(key.entity() == first ? key.queryCondition() : key.reference().queryCondition()) //
					.select(target.all());
			params = key.mapValuesTo(object, key.entity() == first ? key.entity() : key.reference().entity());
		}
		else
		{ // Query from target
			ForeignKey key = getForeignKey(meta.key(), meta.referred(), origin, target);
			sel = sql.from(target) //
					.where(key.entity() == target ? key.queryCondition() : key.reference().queryCondition()) //
					.select(target.all());
			params = key.mapValuesTo(object, key.entity() == target ? key.entity() : key.reference().entity());
		}

		sel = sel.as(AbstractSelect.class) //
				.fillAliasByMeta();

		return new Pair<Select, Map<Column, Object>>(sel, params);
	}

	public static <T> T selectObject(SQLKit kit, SQL sql, Class<T> model, JSON params) throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelClass(sql, model);
		Select select = sql.from(entity) //
				.where(entity.primaryKey().queryCondition()) //
				.select(entity.all()) //
				.as(AbstractSelect.class) //
				.fillAliasByMeta();
		T object = kit.execute(select.toString(), params) //
				.getRow(model, Utils.getFieldNameMapByMeta(model));

		setupObject(kit, sql, object, true);

		return object;
	}

	public static <T> void setManyToOneMembers(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		try
		{
			if (object != null && Tools.access(object, field) == null)
			{
				ManyToOneMeta meta = field.getAnnotation(ManyToOneMeta.class);

				if (meta != null)
				{
					Class<?> model = meta.model();

					Pair<Select, Map<Column, Object>> pair = selectAndParams(sql, object, new RelationDefine(meta),
							field.getAnnotation(JoinMeta.class));
					Select sel = pair.key;
					Map<String, Object> param = mapColumnToLabelByMeta(pair.value);

					Object another = kit.execute(sel.toString(), param) //
							.getRow(model, Utils.getFieldNameMapByMeta(model));
					try
					{
						Tools.access(object, field, another);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					setupObject(kit, sql, another, meta.fully());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static <T> void setOneToManyMembers(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		OneToManyMeta meta = field.getAnnotation(OneToManyMeta.class);

		if (meta != null)
		{
			Class<?> manyModel = meta.model();

			Pair<Select, Map<Column, Object>> pair = selectAndParams(sql, object, new RelationDefine(meta),
					field.getAnnotation(JoinMeta.class));
			Select sel = pair.key;
			Map<String, Object> param = mapColumnToLabelByMeta(pair.value);

			@SuppressWarnings({ "unchecked", "rawtypes" })
			Collection<?> coll = kit.execute(sel.toString(), param) //
					.getRows(new LinkedList(), manyModel, Utils.getFieldNameMapByMeta(manyModel));
			try
			{
				Tools.access(object, field, coll);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			Field manyToOne = getManyToOneField(manyModel, object.getClass());

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
				setupObject(kit, sql, obj, true);
			}
		}
	}

	public static <T> void setOneToOneMembers(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		OneToOneMeta meta = field.getAnnotation(OneToOneMeta.class);

		if (meta != null)
		{
			Class<?> oneModel = meta.model();

			Pair<Select, Map<Column, Object>> pair = selectAndParams(sql, object, new RelationDefine(meta),
					field.getAnnotation(JoinMeta.class));
			Select sel = pair.key;
			Map<String, Object> param = mapColumnToLabelByMeta(pair.value);

			Object another = kit.execute(sel.toString(), param) //
					.getRow(oneModel, Utils.getFieldNameMapByMeta(oneModel));
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

			setupObject(kit, sql, another, true);
		}
	}

	public static <T> void setupObject(SQLKit kit, SQL sql, T object, boolean setOneToMany) throws SQLException
	{
		if (object != null)
		{
			for (Field field : object.getClass().getDeclaredFields())
			{
				if (setOneToMany && field.getAnnotation(OneToManyMeta.class) != null)
				{
					setOneToManyMembers(kit, sql, object, field);
				}
				else if (field.getAnnotation(OneToOneMeta.class) != null)
				{
					setOneToOneMembers(kit, sql, object, field);
				}
				else if (field.getAnnotation(ManyToOneMeta.class) != null)
				{
					setManyToOneMembers(kit, sql, object, field);
				}
			}
		}
	}

	public static <T> int updateObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelClass(sql, object.getClass());
		PrimaryKey key = entity.primaryKey();

		AbstractTable table = entity.as(AbstractTable.class);
		Map<Column, Expression> updateMeta = table.getColumnDefaultExpressions();
		updateMeta = overwriteColumnDefaults(sql, object, updateMeta);

		Update update = table.updateByMetaMap(updateMeta) //
				.where(key.queryCondition());

		Map<String, Object> params = mapColumnToLabelByMeta(mapObjectToEntity(object, entity));

		return kit.update(update.toString(), params);
	}
}
