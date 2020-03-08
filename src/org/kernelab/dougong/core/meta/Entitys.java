package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.demo.Company;
import org.kernelab.dougong.demo.Config;
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

	public static void main(String[] args) throws SQLException
	{
		Company c = Entitys.queryObjectByPrimaryKey(Config.getSQLKit(), Config.SQL, Company.class,
				new JSON().attr("compId", "1"));

		Tools.debug(c);
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

	public static <T> T queryObjectByPrimaryKey(SQLKit kit, SQL sql, Class<T> model, JSON params) throws SQLException
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
