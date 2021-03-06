package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;

public class DataReflector implements JSON.Reflector<Object>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5661668902746480856L;

	public static Map<Class<?>, Object> register(Class<?>... classes)
	{
		DataReflector reflector = new DataReflector();

		Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

		for (Class<?> cls : classes)
		{
			map.put(cls, reflector);
		}

		return map;
	}

	private Map<Class<?>, Map<String, Object>> cache = new HashMap<Class<?>, Map<String, Object>>();

	protected Map<String, Object> mapFields(Class<?> cls)
	{
		Map<String, Object> map = cache.get(cls);

		if (map == null)
		{
			map = new LinkedHashMap<String, Object>();

			for (Field field : Tools.getFieldsHierarchy(cls, null).values())
			{
				if (!Modifier.isStatic(field.getModifiers()))
				{
					if (needSerialize(field))
					{
						map.put(field.getName(), field.getName());
					}
				}
			}

			cache.put(cls, map);
		}

		return map;
	}

	protected boolean needSerialize(Field field)
	{
		DataMeta meta = field.getAnnotation(DataMeta.class);
		if (meta != null && !meta.serialize())
		{
			return false;
		}

		OneToOneMeta oto = field.getAnnotation(OneToOneMeta.class);
		if (oto != null && !oto.serialize())
		{
			return false;
		}

		OneToManyMeta otm = field.getAnnotation(OneToManyMeta.class);
		if (otm != null && !otm.serialize())
		{
			return false;
		}

		ManyToOneMeta mto = field.getAnnotation(ManyToOneMeta.class);
		if (mto != null && !mto.serialize())
		{
			return false;
		}

		return true;
	}

	public JSON reflect(JSON json, Object obj)
	{
		return obj == null ? null : JSON.Reflect(json, obj, mapFields(obj.getClass()));
	}
}
