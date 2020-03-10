package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import org.kernelab.dougong.core.ddl.Key;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.demo.Company;
import org.kernelab.dougong.demo.Config;
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

	public static ForeignKey getForeignKeyBetweenEntitys(String name, Entity a, Entity b)
	{
		try
		{
			Method method = a.getClass().getDeclaredMethod(name, b.getClass());
			return (ForeignKey) method.invoke(a, b);
		}
		catch (Exception e)
		{
			try
			{
				Method method = b.getClass().getDeclaredMethod(name, a.getClass());
				return (ForeignKey) method.invoke(b, a);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return null;
			}
		}
	}

	public static ForeignKey getForeignKeyFromEntity(Entity entity, String name, Entity refer)
	{
		try
		{
			Method method = entity.getClass().getDeclaredMethod(name, refer.getClass());
			return (ForeignKey) method.invoke(entity, refer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
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
			if (AbstractEntity.isColumnField(field))
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

	/**
	 * Get parameters by mapping values in reference according to the given
	 * foreign key. The key of result is the label name of main entity which is
	 * useful to query the main entity.
	 * 
	 * @param reference
	 *            Column/Value map of reference entity.
	 * @param foreignKey
	 *            The foreign key point to the reference.
	 * @return
	 */
	public static Map<String, Object> getParametersFromReferenceByForeignKey(Map<Column, Object> reference,
			ForeignKey foreignKey)
	{
		return mapColumnToLabelByMeta(mapValuesFromReferenceByForeignKey(reference, foreignKey));
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

	public static void main(String[] args) throws SQLException
	{
		Company c = Entitys.selectObjectByPrimaryKey(Config.getSQLKit(), Config.SQL, Company.class,
				new JSON().attr("compId", "1"));

		Tools.debug(c);

		// Company company = new Company();
		// company.setName("Bbb");
		//
		// Entitys.insertObject(Config.getSQLKit(), Config.SQL, company);
		//
		// Tools.debug(company.getId());

		Entity entity = getEntityFromModelClass(Config.SQL, Company.class);
		for (Field field : Company.class.getDeclaredFields())
		{
			select(Config.SQL, entity, field.getAnnotation(JoinMeta.class));
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

	public static void select(SQL sql, Entity origin, JoinMeta joinMeta)
	{
		Primitive prm = null;
		Select sel = null;

		if (joinMeta != null)
		{
			Entity first = null, last = null, curr = null;
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
					prm = sql.from(curr);
				}
				else if (sel == null)
				{
					sel = prm.innerJoin(curr, getForeignKeyBetweenEntitys(join.foreignKey(), last, curr));
				}
				else
				{
					sel = sel.innerJoin(curr, getForeignKeyBetweenEntitys(join.foreignKey(), last, curr));
				}
				last = curr;
				i++;
			}

			sel.select(curr.all());

			ForeignKey fk = getForeignKeyBetweenEntitys(joinMeta.joins()[0].foreignKey(), origin, first);

			Key key = first == fk.entity() ? fk : fk.reference();

			sel.where(key.queryCondition());

			Tools.debug(sel.toString());
		}

		// TODO select from entity and joins
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

		setOneToManyMembers(kit, sql, object, entity);

		return object;
	}

	public static <T> void setOneToManyMembers(SQLKit kit, SQL sql, T object, Entity entity) throws SQLException
	{
		Map<Column, Object> entityFields = Entitys.mapObjectToEntity(object, entity);

		for (Field field : object.getClass().getDeclaredFields())
		{
			OneToManyMeta manyMeta = field.getAnnotation(OneToManyMeta.class);

			if (manyMeta != null)
			{
				Class<?> manyModel = manyMeta.model();

				Entity manyEntity = Entitys.getEntityFromModelClass(sql, manyModel);

				ForeignKey fk = Entitys.getForeignKeyFromEntity(manyEntity, manyMeta.foreignKey(), entity);

				Map<String, Object> param = Entitys.getParametersFromReferenceByForeignKey(entityFields, fk);

				Select sel = sql.from(manyEntity) //
						.where(fk.queryCondition()) //
						.select(manyEntity.all()) //
						.as(AbstractSelect.class) //
						.fillAliasByMeta();

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

				Field manyToOne = Utils.getManyToOneField(manyModel);

				if (manyToOne != null)
				{
					for (Object obj : coll)
					{
						try
						{
							Tools.access(obj, manyToOne, object);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						setOneToManyMembers(kit, sql, obj, manyEntity);
					}
				}
			}
		}
	}
}
