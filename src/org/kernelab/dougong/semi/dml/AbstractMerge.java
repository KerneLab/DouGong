package org.kernelab.dougong.semi.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.basis.Relation;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Merge;
import org.kernelab.dougong.core.util.Utils;

public class AbstractMerge extends AbstractHintable implements Merge
{
	protected static final String				CLAUSE_WHEN_MATCHED		= "WHEN MATCHED";

	protected static final String				CLAUSE_MATCH_UPDATE		= CLAUSE_WHEN_MATCHED + "/UPDATE";

	protected static final String				CLAUSE_WHEN_NOT_MATCHED	= "WHEN NOT MATCHED";

	protected static final String				CLAUSE_NOT_MATCH_INSERT	= CLAUSE_WHEN_NOT_MATCHED + "/INSERT";

	private View								target;

	private Condition							on;

	private View								source;

	private List<Relation<Column, Expression>>	sets;

	private Column[]							inserts;

	private Expression[]						values;

	protected String							clausePath;

	@Override
	public AbstractMerge hint(String hint)
	{
		super.hint(hint);
		return this;
	}

	protected Column[] insert()
	{
		return inserts;
	}

	@Override
	public AbstractMerge insert(Column... columns)
	{
		this.inserts = columns;
		this.clausePath = CLAUSE_NOT_MATCH_INSERT;
		return this;
	}

	@Override
	public AbstractMerge inserts(Expression... columnValuePairs)
	{
		if (columnValuePairs == null || columnValuePairs.length == 0)
		{
			return this;
		}

		Column[] columns = new Column[columnValuePairs.length / 2];
		Expression[] values = new Expression[columns.length];
		for (int i = 0, j = 0; i < columnValuePairs.length; i += 2)
		{
			columns[j] = (Column) columnValuePairs[i];
			values[j] = columnValuePairs[i + 1];
			j++;
		}

		this.insert(columns).values(values);

		return this;
	}

	protected View into()
	{
		return target;
	}

	@Override
	public Merge into(View target)
	{
		this.target = target;
		return this;
	}

	protected Condition on()
	{
		return on;
	}

	@Override
	public Merge on(Condition conditions)
	{
		this.on = conditions;
		return this;
	}

	@Override
	public AbstractMerge set(Column column, Expression value)
	{
		if (this.sets() == null)
		{
			this.sets(new LinkedList<Relation<Column, Expression>>());
		}
		this.sets().add(new Relation<Column, Expression>(column, value));
		return this;
	}

	protected List<Relation<Column, Expression>> sets()
	{
		return sets;
	}

	@Override
	public AbstractMerge sets(Expression... columnValuePairs)
	{
		this.sets(new LinkedList<Relation<Column, Expression>>());

		if (columnValuePairs == null || columnValuePairs.length == 0)
		{
			return this;
		}

		for (int i = 0; i < columnValuePairs.length; i += 2)
		{
			this.set((Column) columnValuePairs[i], columnValuePairs[i + 1]);
		}

		return this;
	}

	protected AbstractMerge sets(List<Relation<Column, Expression>> sets)
	{
		this.sets = sets;
		return this;
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("MERGE");
	}

	protected void textOfInsert(StringBuilder buffer)
	{
		buffer.append(" INSERT (");

		boolean first = true;
		for (Column column : this.insert())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			this.provider().provideOutputColumnReference(buffer, column);
		}

		buffer.append(") VALUES (");

		first = true;
		for (Expression value : this.values())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			Utils.outputExpr(buffer, value);
		}

		buffer.append(')');
	}

	protected void textOfInto(StringBuilder buffer)
	{
		buffer.append(" INTO ");
		this.into().toStringUpdatable(buffer);
	}

	protected void textOfMatchedClause(StringBuilder buffer)
	{
		if (this.sets() != null)
		{
			buffer.append(" WHEN MATCHED THEN");
			this.textOfUpdate(buffer);
		}
	}

	protected void textOfNotMatchedClause(StringBuilder buffer)
	{
		if (this.insert() != null)
		{
			buffer.append(" WHEN NOT MATCHED THEN");
			this.textOfInsert(buffer);
		}
	}

	protected void textOfOn(StringBuilder buffer)
	{
		buffer.append(" ON (");
		this.on().toString(buffer);
		buffer.append(')');
	}

	protected void textOfUpdate(StringBuilder buffer)
	{
		buffer.append(" UPDATE");
		boolean first = true;
		for (Relation<Column, Expression> set : this.sets())
		{
			if (first)
			{
				first = false;
				buffer.append(" SET ");
			}
			else
			{
				buffer.append(',');
			}
			this.provider().provideOutputColumnReference(buffer, set.getKey());
			buffer.append('=');
			Utils.outputExpr(buffer, set.getValue());
		}
	}

	protected void textOfUsing(StringBuilder buffer)
	{
		buffer.append(" USING ");
		this.using().toStringViewed(buffer);
	}

	@Override
	public String toString()
	{
		return toString(new StringBuilder()).toString();
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.textOfHead(buffer);
		this.textOfHint(buffer);
		this.textOfInto(buffer);
		this.textOfUsing(buffer);
		this.textOfOn(buffer);
		this.textOfMatchedClause(buffer);
		this.textOfNotMatchedClause(buffer);
		return buffer;
	}

	public AbstractMerge update()
	{
		this.clausePath = CLAUSE_MATCH_UPDATE;
		return this;
	}

	protected View using()
	{
		return source;
	}

	@Override
	public Merge using(View source)
	{
		this.source = source;
		return this;
	}

	protected Expression[] values()
	{
		return values;
	}

	@Override
	public AbstractMerge values(Expression... values)
	{
		this.values = values;
		return this;
	}

	@Override
	public AbstractMerge whenMatched()
	{
		this.clausePath = CLAUSE_WHEN_MATCHED;
		return this;
	}

	@Override
	public AbstractMerge whenNotMatched()
	{
		this.clausePath = CLAUSE_WHEN_NOT_MATCHED;
		return this;
	}
}
