package org.kernelab.dougong.core.meta;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.kernelab.basis.JSON;
import org.kernelab.basis.Tools;

public class DataProjector implements JSON.Projector<Object>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6441655272323399094L;

	public static Map<Class<?>, Object> register(Class<?>... classes)
	{
		DataProjector projector = new DataProjector();

		Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

		for (Class<?> cls : classes)
		{
			map.put(cls, projector);
		}

		return map;
	}

	private Map<Class<?>, Map<String, Object>>	cache		= new HashMap<Class<?>, Map<String, Object>>();

	private Map<Class<?>, Set<Field>>			oneToMany	= new HashMap<Class<?>, Set<Field>>();

	private Map<Class<?>, Set<Field>>			manyToOne	= new HashMap<Class<?>, Set<Field>>();

	protected Map<String, Object> mapFields(Class<?> cls)
	{
		Map<String, Object> map = cache.get(cls);

		if (map == null)
		{
			map = new LinkedHashMap<String, Object>();
			Set<Field> otm = new HashSet<Field>();
			Set<Field> mto = new HashSet<Field>();

			for (Field field : Tools.getFieldsHierarchy(cls, null).values())
			{
				if (!Modifier.isStatic(field.getModifiers()))
				{
					if (!Entitys.isManyToOne(field))
					{
						map.put(field.getName(), field.getName());
					}
					if (Entitys.isOneToMany(field))
					{
						otm.add(field);
					}
					if (Entitys.isManyToOne(field))
					{
						mto.add(field);
					}
				}
			}

			cache.put(cls, map);
			oneToMany.put(cls, otm);
			manyToOne.put(cls, mto);
		}

		return map;
	}

	public Object project(Object obj, JSON json)
	{
		if (json == null)
		{
			return null;
		}
		try
		{
			obj = JSON.Project(obj, json, mapFields(obj.getClass()));
			setManyToOnes(obj);
			return obj;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected void setManyToOnes(Object obj)
	{
		if (obj != null)
		{
			Set<Field> mtoFields = null;

			for (Field field : oneToMany.get(obj.getClass()))
			{
				try
				{
					Field target = null;
					Iterable<?> iter = Tools.access(obj, field);
					for (Object el : iter)
					{
						if (el != null)
						{
							if (target != null)
							{
								Tools.access(el, target, obj);
							}
							else
							{
								if ((mtoFields = manyToOne.get(el.getClass())) != null)
								{
									for (Field f : mtoFields)
									{
										try
										{
											if (f.getType().isInstance(obj))
											{
												Tools.access(el, f, obj);
												target = f;
												break;
											}
										}
										catch (Exception ex)
										{
											ex.printStackTrace();
										}
									}
								}
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
