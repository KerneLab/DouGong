package org.kernelab.dougong.semi.dml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.kernelab.basis.Agent;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.param.Name;
import org.kernelab.dougong.core.dml.param.Param;

public class DaoAgent extends Agent implements Providable
{
	private Provider provider;

	public DaoAgent(Object real)
	{
		super(real);
	}

	@Deprecated
	@Override
	protected Method find(Class<?> real, Method method) throws Throwable
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T extends Annotation> T getAnnotation(Annotation[] ans, Class<T> cls)
	{
		if (ans == null || cls == null || ans.length == 0)
		{
			return null;
		}
		for (int i = 0; i < ans.length; i++)
		{
			if (cls.equals(ans[i].annotationType()))
			{
				return (T) ans[i];
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		Provider p = provider();
		Class<?>[] raw = method.getParameterTypes();
		Annotation[][] pas = method.getParameterAnnotations();
		Class<?>[] types = new Class<?>[raw.length];
		Object[] params = new Object[raw.length];
		Name name = null;
		for (int i = 0; i < raw.length; i++)
		{
			name = getAnnotation(pas[i], Name.class);
			types[i] = p.provideParameterType(raw[i]);
			params[i] = p.provideParameter((Class<? extends Param<?>>) types[i],
					name != null && Tools.notNullOrEmpty(name.value()) ? name.value() : ("p" + i), args[i]);
		}
		return this.real.getClass().getMethod(method.getName(), types).invoke(this.real, params);
	}

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public Providable provider(Provider provider)
	{
		this.provider = provider;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T to(Class<T> cls)
	{
		return (T) this;
	}
}
