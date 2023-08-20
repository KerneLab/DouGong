package org.kernelab.dougong.orcl.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class OracleSelect extends AbstractSelect
{
	protected static final String	ROWNUM_ALIAS	= "+DouGong-Limit|Offset*RowNum/";

	private boolean					forceOrder		= false;

	protected boolean forceOrder()
	{
		return forceOrder;
	}

	protected OracleSelect forceOrder(boolean forceOrder)
	{
		this.forceOrder = forceOrder;
		return this;
	}

	@Override
	protected OracleSelect prepare()
	{
		Expression rows = this.rows();
		Expression skip = this.skip();

		if (rows != null || skip != null)
		{
			String rn = ROWNUM_ALIAS;
			Item rownum = provider().provideStringItem("ROWNUM");

			if (rows != null && skip != null)
			{
				rows = rows.plus(skip);
			}

			OracleSelect inner = (OracleSelect) this.as("I");
			inner.withs((List<WithDefinition>) null);
			inner.limit(null, null);
			inner.setopr().clear();
			inner.forceOrder(true);

			Select semi = provider().provideSelect().from(inner);
			semi.alias("S");

			if (rows != null)
			{
				semi = semi.where(rownum.le(rows));
			}

			if (skip == null)
			{
				OracleSelect res = (OracleSelect) semi.select(inner.all());
				res.withs(this.withs());
				res.setopr().addAll(this.setopr());
				res.alias(this.alias());
				return res;
			}
			else
			{
				semi.select(inner.all(), //
						rownum.as(rn));

				LinkedList<Expression> list = new LinkedList<Expression>();

				list.addAll(semi.referItems().values());
				list.removeLast();

				OracleSelect res = (OracleSelect) provider().provideSelect() //
						.from(semi) //
						.select(list.toArray(new Expression[0])) //
						.where(semi.ref(rn).gt(skip));

				res.withs(this.withs());
				res.setopr().addAll(this.setopr());
				res.alias(this.alias());

				return res;
			}
		}
		else
		{
			return this;
		}
	}

	@Override
	public OracleSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	@Override
	protected void textOfSetopr(StringBuilder buffer)
	{
		for (Setopr opr : setopr())
		{
			if (opr != null)
			{
				opr.toStringScoped(buffer);
			}
		}
	}

	@Override
	protected void toString(AbstractSelect select, StringBuilder buffer)
	{
		if (select.isSetopr())
		{
			super.toStringScoped(select, buffer);
		}
		else
		{
			super.toString(select, buffer);
		}
	}

	@Override
	protected void toStringScoped(AbstractSelect select, StringBuilder buffer)
	{
		super.toStringScoped(select, buffer);
		OracleSelect s = select.to(OracleSelect.class);
		if (s != null && s.forceOrder())
		{
			this.textOfOrder(buffer);
		}
	}

	@Override
	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		if (this.with() != null)
		{
			return this.provider().provideOutputWithableAliased(buffer, this);
		}
		else
		{
			buffer.append('(');
			this.toStringScoped(buffer);
			buffer.append(')');
			return this.provider().provideOutputAlias(buffer, this);
		}
	}
}
