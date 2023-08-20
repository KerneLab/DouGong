package org.kernelab.dougong.orcl.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class OracleSelect extends AbstractSelect
{
	protected static final String ROWNUM_ALIAS = "+Dou" + ((char) 31) + "Gong-Limit*Orcl/Row" + ((char) 30) + "Num|";

	@Override
	protected OracleSelect prepare()
	{
		Expression rows = this.rows();
		Expression skip = this.skip();

		if (rows != null || skip != null)
		{
			String rn = ROWNUM_ALIAS;
			String col = '"' + rn + '"';
			Item rownum = provider().provideStringItem("ROWNUM");

			if (rows != null && skip != null)
			{
				rows = rows.plus(skip);
			}

			AbstractSelect inner = this.as("t");
			inner.withs((List<WithDefinition>) null);
			inner.limit(null, null);
			inner.setopr().clear();

			Select semi = provider().provideSelect().from(inner);

			if (rows != null)
			{
				semi = semi.where(rownum.le(rows));
			}

			if (skip == null)
			{
				OracleSelect res = (OracleSelect) semi.select(inner.all());
				res.withs(this.withs());
				res.setopr().addAll(this.setopr());
				return res;
			}
			else
			{
				semi.select(inner.all(), //
						rownum.as(rn));

				List<Expression> list = new LinkedList<Expression>();

				list.addAll(semi.referItems().values());
				list.remove(list.size() - 1);

				OracleSelect res = (OracleSelect) provider().provideSelect() //
						.from(semi) //
						.select(list.toArray(new Expression[list.size()])) //
						.where(provider().provideStringItem(col).gt(skip));

				res.withs(this.withs());
				res.setopr().addAll(this.setopr());

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
}
