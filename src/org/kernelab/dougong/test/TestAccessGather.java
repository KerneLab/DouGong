package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.util.AccessGather;
import org.kernelab.dougong.core.util.AccessGather.Access;
import org.kernelab.dougong.core.util.AccessGather.Gather;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class TestAccessGather
{
	static
	{
		AccessGather.GATHER = new Gather()
		{
			public void gather(Access access)
			{
				Tools.debug(access);
			}
		};
	}

	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectAllAliasByMeta().toString(new StringBuilder()));
	}

	public static Select makeSelectAllAliasByMeta()
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), //
						s.DEPT_ID.eq(d.DEPT_ID).and(s.COMP_ID.eq(d.COMP_ID))) //
				.select(SQL.all()) //
				.to(AbstractSelect.class).fillAliasByMeta() //
				.where(d.COMP_ID.gt(SQL.expr("0"))) //
				.orderBy(d.COMP_ID) //
		;
	}

}
