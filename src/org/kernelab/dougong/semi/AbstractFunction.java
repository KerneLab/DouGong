package org.kernelab.dougong.semi;

import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.dml.AbstractItem;

public class AbstractFunction extends AbstractItem implements Function
{
	private Provider		provider;

	private String			name;

	private Expression[]	arguments;

	private boolean			order	= true;

	private String			schema	= null;

	private String[]		aliases	= null;

	@Override
	public AbstractFunction alias(String alias)
	{
		return (AbstractFunction) super.alias(alias);
	}

	public String[] aliases()
	{
		return this.aliases;
	}

	@Override
	public AbstractFunction aliases(String... aliases)
	{
		if (aliases == null || aliases.length == 0)
		{
			this.aliases = null;
		}
		else
		{
			if (aliases.length == 1)
			{
				this.aliases = null;
				this.alias(aliases[0]);
			}
			else
			{
				this.aliases = aliases;
			}
		}
		return this;
	}

	public Expression[] args()
	{
		return arguments;
	}

	protected AbstractFunction args(Expression... arguments)
	{
		this.arguments = arguments;
		return this;
	}

	@Override
	public AbstractFunction as(String alias)
	{
		return this.replicate().alias(alias);
	}

	@Override
	public AbstractFunction as(String... alias)
	{
		return this.replicate().aliases(alias);
	}

	public AbstractFunction ascend()
	{
		return ascend(true);
	}

	public AbstractFunction ascend(boolean ascend)
	{
		this.order = ascend;
		return this;
	}

	public boolean ascending()
	{
		return order;
	}

	public AbstractFunction call(Expression... arguments)
	{
		return this.replicate().args(arguments);
	}

	public AbstractFunction descend()
	{
		return ascend(false);
	}

	protected void initFunction()
	{
		this.schema(Utils.getSchemaFromMember(this));
		this.name(Utils.getNameFromNamed(this));
	}

	public boolean isPseudoColumn()
	{
		return false;
	}

	public String name()
	{
		return name;
	}

	protected AbstractFunction name(String name)
	{
		this.name = name;
		return this;
	}

	protected AbstractFunction newInstance()
	{
		try
		{
			return this.getClass().newInstance();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Provider provider()
	{
		return provider;
	}

	public AbstractFunction provider(Provider provider)
	{
		this.provider = provider;
		this.initFunction();
		return this;
	}

	@Override
	protected AbstractFunction replicate()
	{
		return this.newInstance() //
				.schema(this.schema()) //
				.name(this.name()) //
				.args(this.args()) //
				.ascend(this.ascending()) //
				.provider(this.provider());
	}

	public String schema()
	{
		return schema;
	}

	protected AbstractFunction schema(String schema)
	{
		this.schema = schema;
		return this;
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		return provider().provideOutputFunction(buffer, this);
	}

	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	public StringBuilder toStringOrdered(StringBuilder buffer)
	{
		return this.provider().provideOutputOrder(this.toString(buffer), this);
	}

	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), toString(buffer), this);
	}
}
