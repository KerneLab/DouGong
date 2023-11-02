package org.kernelab.dougong.orcl.dml.cond;

import java.util.ArrayList;
import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.semi.dml.cond.AbstractComparisonCondition;

public class OracleComparisonCondition extends AbstractComparisonCondition
{
	public static class IllegalNullSafeEqualsException extends RuntimeException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 777357513118683364L;

		public IllegalNullSafeEqualsException(String msg)
		{
			super(msg);
		}
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		if (!EQUALS_NULL_SAFE.equals(this.compType))
		{
			if (this.leftExpr instanceof Items)
			{
				buffer.append('(');
			}
			this.leftExpr.toStringExpress(buffer);
			if (this.leftExpr instanceof Items)
			{
				buffer.append(')');
			}
			buffer.append(this.compType);
			if (this.groupQual != null)
			{
				buffer.append(' ');
				buffer.append(this.groupQual);
				buffer.append(' ');
			}
			if (this.rightExpr instanceof Items)
			{
				buffer.append('(');
			}
			this.rightExpr.toStringExpress(buffer);
			if (this.rightExpr instanceof Items)
			{
				buffer.append(')');
			}
		}
		else
		{
			List<Expression> lefts = new ArrayList<Expression>();
			if (this.leftExpr instanceof Items)
			{
				Tools.listOfArray(lefts, ((Items) this.leftExpr).list());
			}
			else
			{
				lefts.add(this.leftExpr);
			}
			List<Expression> rights = new ArrayList<Expression>();
			if (this.rightExpr instanceof Items)
			{
				Tools.listOfArray(rights, ((Items) this.rightExpr).list());
			}
			else
			{
				rights.add(this.rightExpr);
			}
			if (lefts.size() != rights.size())
			{
				throw new IllegalNullSafeEqualsException(this.leftExpr.toString(new StringBuilder()) + " vs "
						+ this.rightExpr.toString(new StringBuilder()));
			}
			int num = lefts.size();
			if (num > 1)
			{
				buffer.append('(');
			}
			Expression l, r;
			for (int i = 0; i < num; i++)
			{
				l = lefts.get(i);
				r = rights.get(i);
				if (i > 0)
				{
					buffer.append(" AND ");
				}
				buffer.append('(');
				l.toStringExpress(buffer);
				buffer.append(EQUALS);
				r.toStringExpress(buffer);
				buffer.append(" OR (");
				l.toStringExpress(buffer);
				buffer.append(" IS NULL AND ");
				r.toStringExpress(buffer);
				buffer.append(" IS NULL)");
				buffer.append(')');
			}
			if (num > 1)
			{
				buffer.append(')');
			}
		}
		return buffer;
	}
}
