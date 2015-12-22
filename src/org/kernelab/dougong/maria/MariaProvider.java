package org.kernelab.dougong.maria;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Table;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.maria.dml.MariaColumn;
import org.kernelab.dougong.maria.dml.MariaDelete;
import org.kernelab.dougong.maria.dml.MariaJoin;
import org.kernelab.dougong.maria.dml.MariaListItem;
import org.kernelab.dougong.maria.dml.MariaSelect;
import org.kernelab.dougong.maria.dml.MariaStringItem;
import org.kernelab.dougong.maria.dml.MariaUpdate;
import org.kernelab.dougong.maria.dml.cond.MariaComparisonCondition;
import org.kernelab.dougong.maria.dml.cond.MariaLikeCondition;
import org.kernelab.dougong.maria.dml.cond.MariaMembershipCondition;
import org.kernelab.dougong.maria.dml.cond.MariaNullCondition;
import org.kernelab.dougong.maria.dml.cond.MariaRangeCondition;
import org.kernelab.dougong.semi.AbstractProvider;

public class MariaProvider extends AbstractProvider
{
	public String provideAliasLabel(String alias)
	{
		return Tools.notNullOrWhite(alias) ? "`" + alias + "`" : null;
	}

	public Column provideColumn(View view, String name)
	{
		return new MariaColumn(view, name);
	}

	public MariaComparisonCondition provideComparisonCondition()
	{
		return new MariaComparisonCondition();
	}

	public MariaDelete provideDelete()
	{
		return new MariaDelete().provider(this);
	}

	public String provideFunctionText(Function function)
	{
		StringBuilder buffer = new StringBuilder();

		if (Tools.notNullOrEmpty(function.schema()))
		{
			buffer.append(function.schema());
			buffer.append('.');
		}

		buffer.append(function.name());

		buffer.append('(');

		if (function.args() != null)
		{
			boolean first = true;

			for (Expression expr : function.args())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					buffer.append(',');
				}
				Utils.text(buffer, expr);
			}
		}

		buffer.append(')');

		return buffer.toString();
	}

	public Join provideJoin()
	{
		return new MariaJoin();
	}

	public MariaLikeCondition provideLikeCondition()
	{
		return new MariaLikeCondition();
	}

	public MariaListItem provideListItem()
	{
		return new MariaListItem(this);
	}

	public MariaMembershipCondition provideMembershipCondition()
	{
		return new MariaMembershipCondition();
	}

	public MariaNullCondition provideNullCondition()
	{
		return new MariaNullCondition();
	}

	public MariaRangeCondition provideRangeCondition()
	{
		return new MariaRangeCondition();
	}

	public MariaSelect provideSelect()
	{
		return new MariaSelect().provider(this);
	}

	public MariaStringItem provideStringItem(String item)
	{
		return (MariaStringItem) new MariaStringItem(this).setString(item);
	}

	public String provideTableName(Table table)
	{
		StringBuilder buffer = new StringBuilder(40);
		if (Tools.notNullOrEmpty(table.schema()))
		{
			buffer.append(table.schema());
			buffer.append('.');
		}
		buffer.append(table.name());
		return buffer.toString();
	}

	public MariaUpdate provideUpdate()
	{
		return new MariaUpdate().provider(this);
	}
}
