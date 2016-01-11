package org.kernelab.dougong.maria;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.opr.ArithmeticOperable;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;
import org.kernelab.dougong.core.util.Utils;
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
import org.kernelab.dougong.maria.dml.opr.MariaArithmeticOperator;
import org.kernelab.dougong.maria.dml.opr.MariaJointOperator;
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

	public DivideOperator provideDivideOperator()
	{
		return this.provideProvider(new MariaArithmeticOperator(ArithmeticOperable.DIVIDE));
	}

	public MariaItems provideItems()
	{
		return (MariaItems) new MariaItems().provider(this);
	}

	public MariaJoin provideJoin()
	{
		return new MariaJoin();
	}

	public MariaJointOperator provideJointOperator()
	{
		return this.provideProvider(new MariaJointOperator());
	}

	public MariaLikeCondition provideLikeCondition()
	{
		return new MariaLikeCondition();
	}

	public MariaMembershipCondition provideMembershipCondition()
	{
		return new MariaMembershipCondition();
	}

	public MinusOperator provideMinusOperator()
	{
		return this.provideProvider(new MariaArithmeticOperator(ArithmeticOperable.MINUS));
	}

	public MultiplyOperator provideMultiplyOperator()
	{
		return this.provideProvider(new MariaArithmeticOperator(ArithmeticOperable.MULTIPLY));
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

	public PlusOperator providePlusOperator()
	{
		return this.provideProvider(new MariaArithmeticOperator(ArithmeticOperable.PLUS));
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
