package org.kernelab.dougong.semi;

import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.dml.AbstractSortable;

public class AbstractFunction extends AbstractSortable implements Function
{
	private Provider		provider;

	private String			name;

	private Expression[]	arguments;

	private String			catalog	= null;

	private String			schema	= null;

	private String[]		aliases	= null;

	@Override
	public AbstractFunction alias(String alias)
	{
		return (AbstractFunction) super.alias(alias);
	}

	@Override
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

	@Override
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
	public AbstractFunction as(String... aliases)
	{
		return this.replicate().aliases(aliases);
	}

	@Override
	public AbstractFunction call(Expression... arguments)
	{
		return this.replicate().args(arguments);
	}

	@Override
	public String catalog()
	{
		return catalog;
	}

	protected AbstractFunction catalog(String catalog)
	{
		this.catalog = catalog;
		return this;
	}

	protected void initFunction()
	{
		this.catalog(Utils.getCatalogFromMember(this));
		this.schema(Utils.getSchemaFromMember(this));
		this.name(Utils.getNameFromNamed(this));
	}

	@Override
	public boolean isPseudo()
	{
		return false;
	}

	@Override
	public String name()
	{
		return name;
	}

	protected AbstractFunction name(String name)
	{
		this.name = name;
		return this;
	}

	@Override
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

	@Override
	public Provider provider()
	{
		return provider;
	}

	@Override
	public AbstractFunction provider(Provider provider)
	{
		this.provider = provider;
		this.initFunction();
		return this;
	}

	@Override
	protected AbstractFunction replicate()
	{
		return (AbstractFunction) this.newInstance() //
				.catalog(this.catalog()) //
				.schema(this.schema()) //
				.name(this.name()) //
				.args(this.args()) //
				.provider(this.provider()) //
				.ascend(this.ascending()) //
				.nullsPosition(this.nullsPosition()) //
		;
	}

	@Override
	public String schema()
	{
		return schema;
	}

	protected AbstractFunction schema(String schema)
	{
		this.schema = schema;
		return this;
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	@Deprecated
	@Override
	public String toString(int level)
	{
		return toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		return provider().provideOutputFunction(buffer, this);
	}

	@Deprecated
	@Override
	public StringBuilder toString(StringBuilder buffer, int level)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringExpress(StringBuilder buffer)
	{
		return toString(buffer);
	}

	@Override
	public StringBuilder toStringSelected(StringBuilder buffer)
	{
		return Utils.outputAlias(this.provider(), toString(buffer), this);
	}
}
