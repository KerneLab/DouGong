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
import org.kernelab.dougong.semi.AbstractProvidable;

public class AbstractMerge extends AbstractProvidable implements Merge
{
	public static class AbstractInsertClause extends AbstractProvidable implements InsertClause
	{
		private AbstractNotMatchedClause	notMatchedClause;

		private Column[]					columns;

		private Expression[]				values;

		public AbstractInsertClause(AbstractNotMatchedClause notMatchedClause)
		{
			this.notMatchedClause = notMatchedClause;
		}

		protected Column[] insert()
		{
			return columns;
		}

		@Override
		public AbstractInsertClause insert(Column... columns)
		{
			this.columns = columns;
			return this;
		}

		@Override
		public AbstractInsertClause into(Expression... columnValuePairs)
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

		@Override
		public AbstractMerge merge()
		{
			return this.notMatchedClause().merge();
		}

		protected AbstractNotMatchedClause notMatchedClause()
		{
			return notMatchedClause;
		}

		@Override
		public StringBuilder toString(StringBuilder buffer)
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

			return buffer;
		}

		protected Expression[] values()
		{
			return values;
		}

		@Override
		public AbstractInsertClause values(Expression... values)
		{
			this.values = values;
			return this;
		}

		@Override
		public AbstractMatchedClause whenMatched()
		{
			return this.merge().whenMatched();
		}
	}

	public static class AbstractMatchedClause extends AbstractProvidable implements MatchedClause
	{
		private AbstractMerge			merge;

		private AbstractUpdateClause	updateClause;

		public AbstractMatchedClause(AbstractMerge merge)
		{
			this.merge = merge;
		}

		protected AbstractMerge merge()
		{
			return merge;
		}

		protected AbstractUpdateClause provideUpdateClause()
		{
			return this.provider().provideProvider(new AbstractUpdateClause(this));
		}

		@Override
		public StringBuilder toString(StringBuilder buffer)
		{
			buffer.append(" WHEN MATCHED THEN");
			this.updateClause().toString(buffer);
			return buffer;
		}

		@Override
		public AbstractUpdateClause update()
		{
			this.updateClause(this.provideUpdateClause());
			return this.updateClause();
		}

		protected AbstractUpdateClause updateClause()
		{
			return updateClause;
		}

		protected AbstractMatchedClause updateClause(AbstractUpdateClause updateClause)
		{
			this.updateClause = updateClause;
			return this;
		}
	}

	public static class AbstractNotMatchedClause extends AbstractProvidable implements NotMatchedClause
	{
		private AbstractMerge			merge;

		private AbstractInsertClause	insertClause;

		public AbstractNotMatchedClause(AbstractMerge merge)
		{
			this.merge = merge;
		}

		@Override
		public AbstractInsertClause insert(Column... columns)
		{
			this.insertClause(this.provideInsertClause());
			return this.insertClause().insert(columns);
		}

		protected AbstractInsertClause insertClause()
		{
			return insertClause;
		}

		protected AbstractNotMatchedClause insertClause(AbstractInsertClause insertClause)
		{
			this.insertClause = insertClause;
			return this;
		}

		protected AbstractMerge merge()
		{
			return merge;
		}

		protected AbstractInsertClause provideInsertClause()
		{
			return this.provider().provideProvider(new AbstractInsertClause(this));
		}

		@Override
		public StringBuilder toString(StringBuilder buffer)
		{
			buffer.append(" WHEN NOT MATCHED THEN");
			this.insertClause().toString(buffer);
			return buffer;
		}
	}

	public static class AbstractUpdateClause extends AbstractProvidable implements UpdateClause
	{
		private AbstractMatchedClause				matchedClause;

		private List<Relation<Column, Expression>>	sets	= new LinkedList<Relation<Column, Expression>>();

		public AbstractUpdateClause(AbstractMatchedClause matchedClause)
		{
			this.matchedClause = matchedClause;
		}

		protected AbstractMatchedClause matchedClause()
		{
			return matchedClause;
		}

		@Override
		public AbstractMerge merge()
		{
			return this.matchedClause().merge();
		}

		@Override
		public AbstractUpdateClause set(Column column, Expression value)
		{
			this.sets().add(new Relation<Column, Expression>(column, value));
			return this;
		}

		protected List<Relation<Column, Expression>> sets()
		{
			return sets;
		}

		@Override
		public AbstractUpdateClause sets(Expression... columnValuePairs)
		{
			this.sets().clear();

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

		@Override
		public StringBuilder toString(StringBuilder buffer)
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
			return buffer;
		}

		@Override
		public AbstractNotMatchedClause whenNotMatched()
		{
			return this.merge().whenNotMatched();
		}
	}

	private View						target;

	private Condition					on;

	private View						source;

	private AbstractMatchedClause		matchedClause;

	private AbstractNotMatchedClause	notMatchedClause;

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

	protected AbstractMatchedClause matchedClause()
	{
		return matchedClause;
	}

	protected AbstractMerge matchedClause(AbstractMatchedClause matchedClause)
	{
		this.matchedClause = matchedClause;
		return this;
	}

	protected AbstractNotMatchedClause notMatchedClause()
	{
		return notMatchedClause;
	}

	protected AbstractMerge notMatchedClause(AbstractNotMatchedClause notMatchedClause)
	{
		this.notMatchedClause = notMatchedClause;
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

	protected AbstractMatchedClause provideMatchedClause()
	{
		return this.provider().provideProvider(new AbstractMatchedClause(this));
	}

	protected AbstractNotMatchedClause provideNotMatchedClause()
	{
		return this.provider().provideProvider(new AbstractNotMatchedClause(this));
	}

	protected void textOfHead(StringBuilder buffer)
	{
		buffer.append("MERGE");
	}

	protected void textOfInto(StringBuilder buffer)
	{
		buffer.append(" INTO ");
		this.into().toStringUpdatable(buffer);
	}

	protected void textOfMatchedClause(StringBuilder buffer)
	{
		if (this.matchedClause() != null)
		{
			this.matchedClause().toString(buffer);
		}
	}

	protected void textOfNotMatchedClause(StringBuilder buffer)
	{
		if (this.notMatchedClause() != null)
		{
			this.notMatchedClause().toString(buffer);
		}
	}

	protected void textOfOn(StringBuilder buffer)
	{
		buffer.append(" ON (");
		this.on().toString(buffer);
		buffer.append(')');
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
		this.textOfInto(buffer);
		this.textOfUsing(buffer);
		this.textOfOn(buffer);
		this.textOfMatchedClause(buffer);
		this.textOfNotMatchedClause(buffer);
		return buffer;
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

	@Override
	public AbstractMatchedClause whenMatched()
	{
		if (this.matchedClause() == null)
		{
			this.matchedClause(this.provideMatchedClause());
		}
		return this.matchedClause();
	}

	@Override
	public AbstractNotMatchedClause whenNotMatched()
	{
		if (this.notMatchedClause() == null)
		{
			this.notMatchedClause(this.provideNotMatchedClause());
		}
		return this.notMatchedClause();
	}
}
