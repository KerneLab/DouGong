package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Relation;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Update;

public class AbstractUpdate extends AbstractFilterable implements Update
{
	private List<Relation<Column, Expression>>	sets	= new LinkedList<Relation<Column, Expression>>();

	@Override
	public AbstractUpdate from(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractUpdate provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	public AbstractUpdate set(Column column, Expression expr)
	{
		sets.add(new Relation<Column, Expression>(column, expr));
		return this;
	}

	protected List<Relation<Column, Expression>> sets()
	{
		return sets;
	}

	@Override
	protected void textOfFrom(StringBuilder buffer)
	{
		buffer.append(' ');
		from().toStringViewed(buffer);
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("UPDATE");
	}

	protected void textOfSets(StringBuilder buffer)
	{
		buffer.append(" SET");

		boolean first = true;

		for (Relation<Column, Expression> set : this.sets())
		{
			if (first)
			{
				first = false;
				buffer.append(' ');
			}
			else
			{
				buffer.append(',');
			}
			Utils.text(buffer, set.getKey());
			buffer.append('=');
			Utils.text(buffer, set.getValue());
		}
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfFrom(buffer);
		this.textOfSets(buffer);
		this.textOfWhere(buffer);
		return buffer;
	}

	public AbstractUpdate update(View view)
	{
		super.from(view);
		return this;
	}

	@Override
	public AbstractUpdate where(Condition cond)
	{
		super.where(cond);
		return this;
	}
}