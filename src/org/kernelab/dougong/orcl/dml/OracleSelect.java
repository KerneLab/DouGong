package org.kernelab.dougong.orcl.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class OracleSelect extends AbstractSelect
{
	@Override
	protected OracleSelect prepare()
	{
		Expression rows = this.rows();

		if (rows != null)
		{
			String rn = "+DouGong-Limit*Orcl/RowNum|";
			String col = '"' + rn + '"';
			Item rownum = provider().provideStringItem("ROWNUM");

			Expression skip = this.skip();

			if (skip == null)
			{
				skip = provider().provideStringItem("0");
			}

			Select inner = this.as("t");
			inner.limit(null, null);

			Select semi = provider().provideSelect() //
					.from(inner) //
					.select(inner.all(), //
							rownum.as(rn)) //
					.where(rownum.le(skip.plus(rows))) //
			;

			List<Expression> list = new LinkedList<Expression>();

			list.addAll(semi.items().values());
			list.remove(list.size() - 1);

			return (OracleSelect) provider().provideSelect() //
					.from(semi) //
					.select(list.toArray(new Expression[list.size()])) //
					.where(provider().provideStringItem(col).gt(skip));
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
