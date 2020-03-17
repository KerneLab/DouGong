package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractEntity;
import org.kernelab.dougong.semi.AbstractTable;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public abstract class Entitys
{
	public static Set<Column> getColumns(Entity entity)
	{
		Set<Column> columns = new HashSet<Column>();

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
		for (Field field : manyClass.getDeclaredFields())
		{
			if (field.getAnnotation(ManyToOneMeta.class) != null //
					&& Tools.isSubClass(oneClass, field.getType()))
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

	public static <T> int insertObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelClass(sql, object.getClass());

		Insert insert = entity.as(AbstractTable.class).insertByMetaMap();

		Tools.debug(insert.toString(new StringBuilder()));

		Pair<Short, Column[]> generateColumns = getGenerateValueColumns(entity);

		Map<String, Object> params = Entitys.mapObjectByMeta(object);

		Tools.debug(params);

		PreparedStatement stmt = null;

		if (generateColumns == null)
		{
			stmt = kit.prepareStatement(insert.toString(), params);
		}
		else if (generateColumns.key == GenerateValueMeta.IDENTITY)
		{
			stmt = kit.prepareStatement(insert.toString(), params, true);
		}
		else if (generateColumns.key == GenerateValueMeta.AUTO)
		{
			Column[] columns = generateColumns.value;
			String[] generateColumnNames = new String[columns.length];
			for (int i = 0; i < columns.length; i++)
			{
				generateColumnNames[i] = columns[i].name();
			}
			stmt = kit.prepareStatement(insert.toString(), params, generateColumnNames);
		}

		Sequel seq = kit.execute(stmt, params);

		// Set generate values
		if (generateColumns != null)
		{
			Column[] columns = generateColumns.value;
			Sequel gen = seq.getGeneratedKeys();
			Object val = null;
			Map<String, Field> fields = Utils.getLabelFieldMapByMeta(object.getClass());
			for (int i = 0; i < columns.length; i++)
			{
				val = gen.getValueObject(i + 1);
				try
				{
					Tools.access(object, fields.get(Utils.getDataLabelFromField(columns[i].field())), val);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		// TODO insert one to many

		return 0;
	}

	public static boolean isManyToOne(Field field)
	{
		return field.getAnnotation(ManyToOneMeta.class) != null;
	}

	public static boolean isOneToMany(Field field)
	{
		return field.getAnnotation(OneToManyMeta.class) != null;
	}

	public static boolean isOneToOne(Field field)
	{
		return field.getAnnotation(OneToOneMeta.class) != null;
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
	 * Map model object to entity column/value pair.
	 * 
	 * @param model
	 * @return
	 */
	public static <T> Map<Column, Object> mapObjectToEntity(T model, Entity entity)
	{
		Map<String, Field> modelFields = Utils.getLabelFieldMapByMeta(model.getClass());

		Map<Column, Object> map = new HashMap<Column, Object>();

		for (Column column : getColumns(entity))
		{
			try
			{
				map.put(column, Tools.access(model, modelFields.get(Utils.getDataLabelFromField(column.field()))));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return map;
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

	public static <T> Pair<Select, Map<Column, Object>> selectAndParams(SQL sql, T object, OneToManyMeta oneToMany,
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

		Entity target = getEntityFromModelClass(sql, oneToMany.model());

		Map<Column, Object> params = null;

		if (sel != null)
		{ // Join with target
			JoinDefine firstJoin = joinMeta.joins()[0];
			ForeignKey key = getForeignKey(firstJoin.key(), firstJoin.referred(), origin, first);
			sel = sel.innerJoin(target.alias("t"), getForeignKey(oneToMany.key(), oneToMany.referred(), last, target)) //
					.where(key.entity() == first ? key.queryCondition() : key.reference().queryCondition()) //
					.select(target.all());
			params = key.mapValuesTo(object, key.entity() == first ? key.entity() : key.reference().entity());
		}
		else
		{ // Query from target
			ForeignKey key = getForeignKey(oneToMany.key(), oneToMany.referred(), origin, target);
			sel = sql.from(target) //
					.where(key.entity() == target ? key.queryCondition() : key.reference().queryCondition()) //
					.select(target.all());
			params = key.mapValuesTo(object, key.entity() == target ? key.entity() : key.reference().entity());
		}

		sel = sel.as(AbstractSelect.class) //
				.fillAliasByMeta();

		// Tools.debug(sel);
		// Tools.debug(params);

		return new Pair<Select, Map<Column, Object>>(sel, params);
	}

	public static <T> Pair<Select, Map<Column, Object>> selectAndParams(SQL sql, T object, OneToOneMeta oneToMany,
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

		Entity target = getEntityFromModelClass(sql, oneToMany.model());

		Map<Column, Object> params = null;

		if (sel != null)
		{ // Join with target
			JoinDefine firstJoin = joinMeta.joins()[0];
			ForeignKey key = getForeignKey(firstJoin.key(), firstJoin.referred(), origin, first);
			sel = sel.innerJoin(target.alias("t"), getForeignKey(oneToMany.key(), oneToMany.referred(), last, target)) //
					.where(key.entity() == first ? key.queryCondition() : key.reference().queryCondition()) //
					.select(target.all());
			params = key.mapValuesTo(object, key.entity() == first ? key.entity() : key.reference().entity());
		}
		else
		{ // Query from target
			ForeignKey key = getForeignKey(oneToMany.key(), oneToMany.referred(), origin, target);
			sel = sql.from(target) //
					.where(key.entity() == target ? key.queryCondition() : key.reference().queryCondition()) //
					.select(target.all());
			params = key.mapValuesTo(object, key.entity() == target ? key.entity() : key.reference().entity());
		}

		sel = sel.as(AbstractSelect.class) //
				.fillAliasByMeta();

		// Tools.debug(sel);
		// Tools.debug(params);

		return new Pair<Select, Map<Column, Object>>(sel, params);
	}

	public static <T> T selectObjectByPrimaryKey(SQLKit kit, SQL sql, Class<T> model, JSON params) throws SQLException
	{
		Entity entity = Entitys.getEntityFromModelClass(sql, model);
		Select select = sql.from(entity) //
				.where(entity.primaryKey().queryCondition()) //
				.select(entity.all()) //
				.as(AbstractSelect.class) //
				.fillAliasByMeta();
		T object = kit.execute(select.toString(), params) //
				.getRow(model, Utils.getFieldNameMapByMeta(model));

		setupObject(kit, sql, object);

		return object;
	}

	public static <T> void setOneToManyMembers(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		OneToManyMeta manyMeta = field.getAnnotation(OneToManyMeta.class);

		if (manyMeta != null)
		{
			Class<?> manyModel = manyMeta.model();

			Pair<Select, Map<Column, Object>> pair = selectAndParams(sql, object, manyMeta,
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
				setupObject(kit, sql, obj);
			}
		}
	}

	public static <T> void setOneToOneMembers(SQLKit kit, SQL sql, T object, Field field) throws SQLException
	{
		OneToOneMeta oneMeta = field.getAnnotation(OneToOneMeta.class);

		if (oneMeta != null)
		{
			Class<?> oneModel = oneMeta.model();

			Pair<Select, Map<Column, Object>> pair = selectAndParams(sql, object, oneMeta,
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

			setupObject(kit, sql, another);
		}
	}

	public static <T> void setupObject(SQLKit kit, SQL sql, T object) throws SQLException
	{
		if (object != null)
		{
			for (Field field : object.getClass().getDeclaredFields())
			{
				if (field.getAnnotation(OneToManyMeta.class) != null)
				{
					setOneToManyMembers(kit, sql, object, field);
				}
				else if (field.getAnnotation(OneToOneMeta.class) != null)
				{
					setOneToOneMembers(kit, sql, object, field);
				}
			}
		}
	}
}
