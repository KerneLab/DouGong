package org.kernelab.dougong.core.dml;

import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Providable;
import org.kernelab.dougong.core.Text;
import org.kernelab.dougong.core.View;

public interface Merge extends DML, Providable, Hintable
{
	public static interface UpdateClause extends Text, Providable
	{
		public UpdateClause set(Column column, Expression value);

		public UpdateClause sets(Expression... columnValuePairs);

		public NotMatchedClause whenNotMatched();

		public Merge merge();
	}

	public static interface MatchedClause extends Text, Providable
	{
		public UpdateClause update();
	}

	public static interface InsertClause extends Text, Providable
	{
		public InsertClause insert(Column... columns);

		public InsertClause values(Expression... values);

		public InsertClause into(Expression... columnValuePairs);

		public MatchedClause whenMatched();

		public Merge merge();
	}

	public static interface NotMatchedClause extends Text, Providable
	{
		public InsertClause insert(Column... columns);
	}

	public Merge into(View target);

	public Merge using(View source);

	public Merge on(Condition conditions);

	public MatchedClause whenMatched();

	public NotMatchedClause whenNotMatched();

	public Merge hint(String hint);
}
