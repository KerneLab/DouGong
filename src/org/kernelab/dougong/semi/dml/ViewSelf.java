package org.kernelab.dougong.semi.dml;

import java.util.List;
import java.util.Map;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.semi.AbstractView;

public class ViewSelf extends AbstractView
{
	public static class NotInstantiatedException extends RuntimeException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5476557570029857363L;

		public NotInstantiatedException(String message)
		{
			super(message);
		}
	}

	private View self;

	@Override
	public AllItems all()
	{
		return self().all();
	}

	public ViewSelf as(String alias)
	{
		try
		{
			ViewSelf s = (ViewSelf) this.clone();
			s.alias(alias);
			return s;
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Item> items()
	{
		return self().items();
	}

	@Override
	public Reference ref(String name)
	{
		Reference ref = super.ref(name);
		if (ref == null)
		{
			ref = this.provider().provideReference(this, name);
		}
		return ref;
	}

	@Override
	public Map<String, Item> referItems()
	{
		return self().referItems();
	}

	public View self()
	{
		if (self == null)
		{
			throw new NotInstantiatedException(this.alias());
		}
		return self;
	}

	public ViewSelf self(View self)
	{
		this.self = self;
		return this;
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return self().toString(buffer);
	}

	@Override
	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return self().toStringDeletable(buffer);
	}

	@Override
	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		return self().toStringUpdatable(buffer);
	}

	@Override
	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		if (self() instanceof Withable)
		{
			this.provider().provideOutputWithDefinition(buffer, ((Withable) self()).with());
			this.provider().provideOutputAlias(buffer, this);
		}
		else
		{
			self().toStringViewed(buffer);
		}
		return buffer;
	}
}
