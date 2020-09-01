package org.kernelab.dougong.maria;

import java.lang.reflect.Field;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Entity;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.opr.ArithmeticOperable;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;
import org.kernelab.dougong.core.util.KeysFetcher;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.maria.ddl.MariaForeignKey;
import org.kernelab.dougong.maria.ddl.MariaPrimaryKey;
import org.kernelab.dougong.maria.dml.MariaAllItems;
import org.kernelab.dougong.maria.dml.MariaDelete;
import org.kernelab.dougong.maria.dml.MariaInsert;
import org.kernelab.dougong.maria.dml.MariaItems;
import org.kernelab.dougong.maria.dml.MariaJoin;
import org.kernelab.dougong.maria.dml.MariaReference;
import org.kernelab.dougong.maria.dml.MariaSelect;
import org.kernelab.dougong.maria.dml.MariaStringItem;
import org.kernelab.dougong.maria.dml.MariaUpdate;
import org.kernelab.dougong.maria.dml.cond.MariaComparisonCondition;
import org.kernelab.dougong.maria.dml.cond.MariaLikeCondition;
import org.kernelab.dougong.maria.dml.cond.MariaLogicalCondition;
import org.kernelab.dougong.maria.dml.cond.MariaMembershipCondition;
import org.kernelab.dougong.maria.dml.cond.MariaNullCondition;
import org.kernelab.dougong.maria.dml.cond.MariaRangeCondition;
import org.kernelab.dougong.maria.dml.opr.MariaArithmeticOperator;
import org.kernelab.dougong.maria.dml.opr.MariaCaseDecideExpression;
import org.kernelab.dougong.maria.dml.opr.MariaCaseSwitchExpression;
import org.kernelab.dougong.maria.dml.opr.MariaJointOperator;
import org.kernelab.dougong.semi.AbstractProvider;
import org.kernelab.dougong.semi.dml.AbstractMerge;

public class MariaProvider extends AbstractProvider
{
	public static final char TEXT_BOUNDARY_CHAR = '`';

	public MariaAllItems provideAllItems(View view)
	{
		return new MariaAllItems(view);
	}

	public MariaCaseDecideExpression provideCaseExpression()
	{
		return new MariaCaseDecideExpression(this);
	}

	public MariaCaseSwitchExpression provideCaseExpression(Expression value)
	{
		return (MariaCaseSwitchExpression) new MariaCaseSwitchExpression(this).caseValue(value);
	}

	public MariaColumn provideColumn(View view, String name, Field field)
	{
		return new MariaColumn(view, name, field);
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

	public ForeignKey provideForeignKey(PrimaryKey reference, Entity entity, Column... columns)
	{
		return provideProvider(new MariaForeignKey(reference, entity, columns));
	}

	public MariaInsert provideInsert()
	{
		return new MariaInsert().provider(this);
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

	public KeysFetcher provideKeysFetcher()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public MariaLikeCondition provideLikeCondition()
	{
		return new MariaLikeCondition();
	}

	public MariaLogicalCondition provideLogicalCondition()
	{
		return new MariaLogicalCondition();
	}

	public MariaMembershipCondition provideMembershipCondition()
	{
		return new MariaMembershipCondition();
	}

	@Override
	public AbstractMerge provideMerge()
	{
		throw new UnsupportedOperationException("Merge statement has not been supported");
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
		if (Tools.notNullOrEmpty(function.schema()))
		{
			this.provideOutputNameText(buffer, function.schema());
			buffer.append(OBJECT_SEPARATOR_CHAR);
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

	public PrimaryKey providePrimaryKey(Entity entity, Column... columns)
	{
		return provideProvider(new MariaPrimaryKey(entity, columns));
	}

	public MariaRangeCondition provideRangeCondition()
	{
		return new MariaRangeCondition();
	}

	public MariaReference provideReference(View view, Expression expr)
	{
		return provideProvider(new MariaReference(view, expr));
	}

	public MariaSelect provideSelect()
	{
		return new MariaSelect().provider(this);
	}

	public MariaStringItem provideStringItem(String item)
	{
		return (MariaStringItem) provideProvider(new MariaStringItem(this).setString(item));
	}

	public MariaUpdate provideUpdate()
	{
		return new MariaUpdate().provider(this);
	}
}
