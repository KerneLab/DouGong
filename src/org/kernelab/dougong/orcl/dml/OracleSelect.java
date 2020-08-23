package org.kernelab.dougong.orcl.dml;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.Provider;
import org.kernelab.dougong.core.dml.Condition;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class OracleSelect extends AbstractSelect
{
	protected static final String	ROWNUM_ALIAS	= "+Dou" + ((char) 31) + "Gong-Limit*Orcl/Row" + ((char) 30)
			+ "Num|";

	private Condition				startWith;

	private Condition				connectBy;

	private boolean					nocycle;

	public Condition connectBy()
	{
		return connectBy;
	}

	public OracleSelect connectBy(Condition connectBy)
	{
		this.connectBy = connectBy;
		return this;
	}

	public boolean nocycle()
	{
		return nocycle;
	}

	public OracleSelect nocycle(boolean nocycle)
	{
		this.nocycle = nocycle;
		return this;
	}

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

	public Condition startWith()
	{
		return startWith;
	}

	public OracleSelect startWith(Condition startWith)
	{
		this.startWith = startWith;
		return this;
	}

	public void textOfConnectBy(StringBuilder buffer)
	{
		if (connectBy() != null)
		{
			if (startWith() != null)
			{
				buffer.append(" START WITH ");

				startWith().toString(buffer);
			}

			buffer.append(" CONNECT BY ");

			if (nocycle())
			{
				buffer.append("NOCYCLE ");
			}

			connectBy().toString(buffer);
		}
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		OracleSelect select = prepare();
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfHint(buffer);
		select.textOfUnique(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfJoin(buffer);
		select.textOfWhere(buffer);
		select.textOfConnectBy(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		select.textOfAbstractSetopr(buffer);
		select.textOfOrder(buffer);
		return buffer;
	}

	@Override
	public StringBuilder toStringScoped(StringBuilder buffer)
	{
		OracleSelect select = prepare();
		select.textOfWith(buffer);
		select.textOfHead(buffer);
		select.textOfHint(buffer);
		select.textOfUnique(buffer);
		select.textOfItems(buffer);
		select.textOfFrom(buffer);
		select.textOfJoin(buffer);
		select.textOfWhere(buffer);
		select.textOfConnectBy(buffer);
		select.textOfGroup(buffer);
		select.textOfHaving(buffer);
		select.textOfAbstractSetopr(buffer);
		return buffer;
	}
}
