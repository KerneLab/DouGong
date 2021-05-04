package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.param.IntParam;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.STAF;
import org.kernelab.dougong.orcl.OracleProvider;
import org.kernelab.dougong.semi.dml.AbstractSelect;

public class TestParam
{
	public static SQL SQL = new SQL(new OracleProvider());

	public static void main(String[] args)
	{
		Tools.debug(makeSelectAliasByMeta(SQL.param("a", 1)) //
				.toString(new StringBuilder()));
	}

	public static Select makeSelectAliasByMeta(IntParam a)
	{
		DEPT d = null;
		STAF s = null;

		return SQL.from(s = SQL.table(STAF.class, "s")) //
				.innerJoin(d = SQL.table(DEPT.class, "d"), s.DEPT_ID.eq(d.DEPT_ID)) //
				.select(d.COMP_ID, //
						s.STAF_ID, //
						s.STAF_NAME.joint(s.STAF_JOB).as("jj"), //
						s.STAF_NAME.as("name") //
				) //
				.to(AbstractSelect.class).fillAliasByMeta() //
				.where(d.COMP_ID.gt(a)) //
				.orderBy(d.COMP_ID) //
		;
	}
}
