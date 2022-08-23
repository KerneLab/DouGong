package org.kernelab.dougong.orcl.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class OracleSelect extends AbstractSelect
{
	protected static final String ROWNUM_ALIAS = "+Dou" + ((char) 31) + "Gong-Limit*Orcl/Row" + ((char) 30) + "Num|";

	@Override
	protected OracleSelect prepare()
	{
		Expression rows = this.rows();

		if (rows != null)
		{
			String rn = ROWNUM_ALIAS;
			String col = '"' + rn + '"';
			Item rownum = provider().provideStringItem("ROWNUM");

			Expression skip = this.skip();

			if (skip != null)
			{
				rows = rows.plus(skip);
			}

			Select inner = this.as("t");
			inner.limit(null, null);

			Select semi = provider().provideSelect() //
					.from(inner) //
					.where(rownum.le(rows));

			if (skip == null)
			{
				return (OracleSelect) semi.select(inner.all());
			}
			else
			{
				semi.select(inner.all(), //
						rownum.as(rn));

				List<Expression> list = new LinkedList<Expression>();

				list.addAll(semi.referItems().values());
				list.remove(list.size() - 1);

				return (OracleSelect) provider().provideSelect() //
						.from(semi) //
						.select(list.toArray(new Expression[list.size()])) //
						.where(provider().provideStringItem(col).gt(skip));
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
