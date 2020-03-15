package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.demo.COMP;
import org.kernelab.dougong.demo.Company;
import org.kernelab.dougong.demo.Config;
import org.kernelab.dougong.demo.DEPT;
import org.kernelab.dougong.demo.Department;
import org.kernelab.dougong.demo.EVNT;
import org.kernelab.dougong.demo.Event;

public class TestMapByForeignKey
{

	public static void main(String[] args)
	{
		Company comObj = new Company();
		comObj.setId("1");
		comObj.setName("Cm.1");

		Department depObj = new Department();
		depObj.setCompId("1");
		depObj.setId("a");
		depObj.setName("Dep.A");

		DEPT dep = Config.SQL.view(DEPT.class);
		COMP com = Config.SQL.view(COMP.class);

		ForeignKey key = dep.foreignKey("FRN_DEPT", com);

		Tools.debug(key.mapValuesToReferrer(comObj));
		Tools.debug(key.mapValuesToReferrer(depObj));
		Tools.debug(key.mapValuesToReference(comObj));
		Tools.debug(key.mapValuesToReference(depObj));

		Event evObj = new Event();
		evObj.setId("1");
		evObj.setName("one");
		evObj.setNextId("2");

		EVNT ev1 = Config.SQL.view(EVNT.class);
		EVNT ev2 = Config.SQL.view(EVNT.class);
		ForeignKey keyEV = ev1.foreignKey("FK_EVENT_SELF", ev2);

		Tools.debug(keyEV.mapValuesToReferrer(evObj));
		Tools.debug(keyEV.mapValuesToReference(evObj));
	}

}
