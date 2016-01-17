package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class OracleSelect extends AbstractSelect
{
	@Override
	public OracleSelect provider(Provider provider)
	{
		super.provider(provider);
		return this;
	}

	public Select limit(Expression skip, Expression rows)
	{
		String rn = "+DouGong-Limit*Orcl/RowNum|";
		String col = '"' + rn + '"';
		Item item = provider().provideStringItem("ROWNUM");

		if (skip == null)
		{
			skip = provider().provideStringItem("0");
		}

		Select inner = this.as("t");

		return provider().provideSelect() //
				.from(provider().provideSelect() //
						.from(inner) //
						.select(inner.all(), //
								item.as(rn)) //
						.where(item.le(skip.plus(rows))) //
				) //
				.select(provider().provideAllColumns(null)) //
				.where(provider().provideStringItem(col).gt(skip));
	}
}
