package org.kernelab.dougong.core.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.basis.JSON.JSAN;
import org.kernelab.basis.Mapper;
import org.kernelab.dougong.core.dml.param.ByteParam;
import org.kernelab.dougong.core.dml.param.CharParam;
import org.kernelab.dougong.core.dml.param.DateParam;
import org.kernelab.dougong.core.dml.param.DecimalParam;
import org.kernelab.dougong.core.dml.param.DoubleParam;
import org.kernelab.dougong.core.dml.param.FloatParam;
import org.kernelab.dougong.core.dml.param.IntParam;
import org.kernelab.dougong.core.dml.param.IterableParam;
import org.kernelab.dougong.core.dml.param.JSANParam;
import org.kernelab.dougong.core.dml.param.JSONParam;
import org.kernelab.dougong.core.dml.param.LongParam;
import org.kernelab.dougong.core.dml.param.MapParam;
import org.kernelab.dougong.core.dml.param.ObjectParam;
import org.kernelab.dougong.core.dml.param.Param;
import org.kernelab.dougong.core.dml.param.ShortParam;
import org.kernelab.dougong.core.dml.param.StringParam;
import org.kernelab.dougong.core.dml.param.TimestampParam;
import org.kernelab.dougong.semi.AbstractProvidable;

public class ParamsContext extends AbstractProvidable
{
	protected static final Mapper<Integer, String> DEFAULT_PARAM_NAMER = new Mapper<Integer, String>()
	{
		@Override
		public String map(Integer i) throws Exception
		{
			return "_p_" + i;
		}
	};

	public static Map<String, Object> params(Iterable<Param<?>> params)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (Param<?> param : params)
		{
			map.put(param.name(), param.value());
		}

		return map;
	}

	public static Map<String, Object> params(Map<String, Object>... params)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (Map<String, Object> param : params)
		{
			map.putAll(param);
		}

		return map;
	}

	public static Map<String, Object> params(Param<?>... params)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		for (Param<?> param : params)
		{
			map.put(param.name(), param.value());
		}

		return map;
	}

	private Map<String, Param<?>>	params	= new LinkedHashMap<String, Param<?>>();

	private int						index	= 0;

	private Mapper<Integer, String>	namer	= DEFAULT_PARAM_NAMER;

	protected void check(String name)
	{
		if (this.getParams().containsKey(name))
		{
			throw new IllegalArgumentException("Parameter " + name + " has already been set");
		}
	}

	public Param<?> get(String name)
	{
		return this.getParams().get(name);
	}

	public Param<?> get(String name, Object value)
	{
		Param<?> p = this.getParams().get(name);
		if (p == null)
		{
			p = this.setBy(name, value);
		}
		return p;
	}

	protected int getIndex()
	{
		return index;
	}

	public Mapper<Integer, String> getNamer()
	{
		return namer;
	}

	protected Map<String, Param<?>> getParams()
	{
		return params;
	}

	public Map<String, Object> map()
	{
		return params(this.getParams().values());
	}

	public DecimalParam next(BigDecimal value)
	{
		return set(nextName(), value);
	}

	public ByteParam next(Byte value)
	{
		return set(nextName(), value);
	}

	public CharParam next(Character value)
	{
		return set(nextName(), value);
	}

	public DateParam next(Date value)
	{
		return set(nextName(), value);
	}

	public DoubleParam next(Double value)
	{
		return set(nextName(), value);
	}

	public FloatParam next(Float value)
	{
		return set(nextName(), value);
	}

	public IntParam next(Integer value)
	{
		return set(nextName(), value);
	}

	public IterableParam next(Iterable<?> value)
	{
		return set(nextName(), value);
	}

	public JSANParam next(JSAN value)
	{
		return set(nextName(), value);
	}

	public JSONParam next(JSON value)
	{
		return set(nextName(), value);
	}

	public LongParam next(Long value)
	{
		return set(nextName(), value);
	}

	public <K, V> MapParam<K, V> next(Map<K, V> value)
	{
		return set(nextName(), value);
	}

	public ShortParam next(Short value)
	{
		return set(nextName(), value);
	}

	public StringParam next(String value)
	{
		return set(nextName(), value);
	}

	public <T> ObjectParam<T> next(T value)
	{
		return set(nextName(), value);
	}

	public TimestampParam next(Timestamp value)
	{
		return set(nextName(), value);
	}

	public Param<?> nextBy(Object value)
	{
		return setBy(nextName(), value);
	}

	protected String nextName()
	{
		try
		{
			return this.getNamer().map(this.getIndex());
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			this.setIndex(this.getIndex() + 1);
		}
	}

	protected <T extends Param<?>> T put(String name, T param)
	{
		this.getParams().put(name, param);
		return param;
	}

	public DecimalParam set(String name, BigDecimal value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public ByteParam set(String name, Byte value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public CharParam set(String name, Character value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public DateParam set(String name, Date value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public DoubleParam set(String name, Double value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public FloatParam set(String name, Float value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public IntParam set(String name, Integer value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public IterableParam set(String name, Iterable<?> value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public JSANParam set(String name, JSAN value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public JSONParam set(String name, JSON value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public LongParam set(String name, Long value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public <K, V> MapParam<K, V> set(String name, Map<K, V> value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public ShortParam set(String name, Short value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public StringParam set(String name, String value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public <T> ObjectParam<T> set(String name, T value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public TimestampParam set(String name, Timestamp value)
	{
		check(name);
		return put(name, this.provider().provideParameter(name, value));
	}

	public Param<?> setBy(String name, Object value)
	{
		check(name);
		return put(name, this.provider().provideParameterByValue(name, value));
	}

	protected void setIndex(int index)
	{
		this.index = index;
	}

	public ParamsContext setNamer(Mapper<Integer, String> namer)
	{
		this.namer = namer;
		return this;
	}

	protected void setParams(Map<String, Param<?>> params)
	{
		this.params = params;
	}
}
