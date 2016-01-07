package org.kernelab.dougong.maria;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Utils;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.maria.dml.MariaAllColumns;
import org.kernelab.dougong.maria.dml.MariaColumn;
import org.kernelab.dougong.maria.dml.MariaDelete;
import org.kernelab.dougong.maria.dml.MariaItems;
import org.kernelab.dougong.maria.dml.MariaJoin;
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
	public static final char	TEXT_BOUNDARY_CHAR	= '`';

	public MariaAllColumns provideAllColumns(View view)
	{
		return new MariaAllColumns(view);
	}

	public MariaColumn provideColumn(View view, String name)
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

	public MariaItems provideItems()
	{
		return (MariaItems) new MariaItems().provider(this);
	}

	public MariaJoin provideJoin()
	{
		return new MariaJoin();
	}

	public MariaLikeCondition provideLikeCondition()
	{
		return new MariaLikeCondition();
	}

	public MariaMembershipCondition provideMembershipCondition()
	{
		return new MariaMembershipCondition();
	}

	public String provideNameText(String name)
	{
		if (name != null)
		{
			return TEXT_BOUNDARY_CHAR + name + TEXT_BOUNDARY_CHAR;
		}
		else
		{
			return SQL.NULL;
		}
	}

	public MariaNullCondition provideNullCondition()
	{
		return new MariaNullCondition();
	}

	public StringBuilder provideOutputFunction(StringBuilder buffer, Function function)
	{
		this.provideOutputMember(buffer, function);

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
				Utils.outputExpr(buffer, expr);
			}
		}

		buffer.append(')');

		return buffer;
	}

	public StringBuilder provideOutputOrder(StringBuilder buffer, Sortable sort)
	{
		if (buffer != null && sort != null)
		{
			buffer.append(' ');

			if (sort.ascending())
			{
				buffer.append("ASC");
			}
			else
			{
				buffer.append("DESC");
			}
		}
		return buffer;
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

	public MariaUpdate provideUpdate()
	{
		return new MariaUpdate().provider(this);
	}
}
