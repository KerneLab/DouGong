package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.core.util.Utils;

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

			DataMeta meta = null;
			for (Field field : cls.getDeclaredFields())
			{
				meta = field.getAnnotation(DataMeta.class);
				if (meta == null || meta.serialize())
				{
					if (!Entitys.isManyToOne(field))
					{
						map.put(Utils.getDataLabelFromField(field), field.getName());
					}
				}
			}

			cache.put(cls, map);
		}

		return map;
	}

	public JSON reflect(JSON json, Object obj)
	{
		return obj == null ? null : JSON.Reflect(json, obj, mapFields(obj.getClass()));
	}
}
