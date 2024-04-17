package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.semi.dml.AdvancedInsert;

public class MariaInsert extends AdvancedInsert
{
	private boolean ignore = false;

	public boolean ignore()
	{
		return ignore;
	}

	public MariaInsert ignore(boolean ignore)
	{
		this.ignore = ignore;
		return this;
	}

	@Override
	public MariaInsert provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	protected void textOfHead(StringBuilder buffer)
	{
		super.textOfHead(buffer);

		if (this.ignore())
		{
			buffer.append(" IGNORE");
		}
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		super.toString(buffer);
		return textOfDuplicateKeyUpdate(buffer);
	}

	public String toStringWithFirst()
	{
		return this.toStringWithFirst(new StringBuilder()).toString();
	}

	public StringBuilder toStringWithFirst(StringBuilder buffer)
	{
		if (this.select() == null)
		{
			return this.toString(buffer);
		}

		Select sel = null;
		if (this.select() instanceof Select)
		{
			sel = (Select) this.select();
		}
		else if (this.select() instanceof Subquery)
		{
			sel = ((Subquery) this.select()).select();
		}

		if (sel != null && sel.withs() != null && !sel.withs().isEmpty())
		{
			this.textOfSourceWith(buffer);
		}

		this.textOfHead(buffer);
		this.textOfHint(buffer);
		this.textOfTarget(buffer);
		this.textOfColumns(buffer);
		if (values() != null)
		{
			this.textOfValues(buffer);
		}
		else if (this.select() != null)
		{
			buffer.append(' ');
			this.textOfSourceBody(buffer);
		}

		return textOfDuplicateKeyUpdate(buffer);
	}
}
