package org.kernelab.dougong.semi.dml;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.kernelab.basis.Relation;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.util.Utils;

public class AdvancedInsert extends AbstractInsert
{
	private List<Relation<Column, Expression>>	updates	= new LinkedList<Relation<Column, Expression>>();

	private Expression[]						returning;

	@Override
	public AdvancedInsert provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	public Expression[] returning()
	{
		return returning;
	}

	public AdvancedInsert returning(Expression... exprs)
	{
		this.returning = exprs != null && exprs.length > 0 ? exprs : null;
		return this;
	}

	protected StringBuilder textOfDuplicateKeyUpdate(StringBuilder buffer)
	{
		boolean first = true;
		for (Relation<Column, Expression> set : updates())
		{
			if (first)
			{
				buffer.append(" ON DUPLICATE KEY UPDATE ");
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			provider().provideOutputColumnInsert(buffer, set.getKey());
			buffer.append('=');
			Utils.outputExpr(buffer, set.getValue());
		}
		return buffer;
	}

	protected StringBuilder textOfReturningClause(StringBuilder buffer)
	{
		if (returning() != null && returning().length > 0)
		{
			buffer.append(" RETURNING ");
			int index = 1;
			for (Expression expr : returning())
			{
				if (index > 1)
				{
					buffer.append(',');
				}
				expr.toStringExpress(buffer);
				index++;
			}
		}
		return buffer;
	}

	public AdvancedInsert update(Column column, Expression value)
	{
		updates().add(new Relation<Column, Expression>(column, value));
		return this;
	}

	public List<Relation<Column, Expression>> updates()
	{
		return updates;
	}

	public AdvancedInsert updates(Expression... columnValuePairs)
	{
		this.updates().clear();

		if (columnValuePairs == null || columnValuePairs.length == 0)
		{
			return this;
		}

		for (int i = 0; i < columnValuePairs.length; i += 2)
		{
			this.update((Column) columnValuePairs[i], columnValuePairs[i + 1]);
		}

		return this;
	}

	public AdvancedInsert updatesByValues()
	{
		Table table = Tools.as(this.into(), Table.class);

		if (table == null)
		{
			throw new RuntimeException("Only table target can be updated");
		}

		this.updates().clear();

		Set<Column> key = new HashSet<Column>();

		PrimaryKey pk = table.primaryKey();

		if (pk != null)
		{
			for (Column col : pk.columns())
			{
				key.add(col);
			}
		}

		Iterable<? extends Item> cols = columns() != null && columns().length > 0 ? Tools.iterable(columns())
				: table.items();

		for (Item col : cols)
		{
			if (!key.contains(col))
			{
				this.update((Column) col, provider()
						.provideStringItem("VALUES(" + provider().provideNameText(((Column) col).name()) + ")"));
			}
		}

		return this;
	}

	public AdvancedInsert updatesOfColumns(Column... columns)
	{
		if (columns == null || columns.length == 0)
		{
			return this.updatesByValues();
		}

		this.updates().clear();

		for (Item col : columns)
		{
			this.update((Column) col,
					provider().provideStringItem("VALUES(" + provider().provideNameText(((Column) col).name()) + ")"));
		}

		return this;
	}
}
