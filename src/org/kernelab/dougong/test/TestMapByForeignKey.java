package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
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

		Tools.debug(com.mapValuesToReferrer(comObj, "FRN_DEPT", dep));
		Tools.debug(com.mapValuesToReferrer(depObj, "FRN_DEPT", dep));
		Tools.debug(dep.mapValuesToReferrer(comObj, "FRN_DEPT", com));
		Tools.debug(dep.mapValuesToReferrer(depObj, "FRN_DEPT", com));
		Tools.debug(com.mapValuesToReference(comObj, "FRN_DEPT", dep));
		Tools.debug(com.mapValuesToReference(depObj, "FRN_DEPT", dep));
		Tools.debug(dep.mapValuesToReference(comObj, "FRN_DEPT", com));
		Tools.debug(dep.mapValuesToReference(depObj, "FRN_DEPT", com));

		Event obj = new Event();
		obj.setId("1");
		obj.setName("one");
		obj.setNextId("2");

		EVNT ev1 = Config.SQL.view(EVNT.class);
		EVNT ev2 = Config.SQL.view(EVNT.class);

		Tools.debug(ev1.mapValuesToReferrer(obj, "FK_EVENT_SELF", ev2));
		Tools.debug(ev2.mapValuesToReferrer(obj, "FK_EVENT_SELF", ev1));
		Tools.debug(ev1.mapValuesToReference(obj, "FK_EVENT_SELF", ev2));
		Tools.debug(ev2.mapValuesToReference(obj, "FK_EVENT_SELF", ev1));
	}

}
