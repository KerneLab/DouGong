package org.kernelab.dougong.demo;

import java.util.LinkedList;
import java.util.List;

import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.ManyToOneMeta;
import org.kernelab.dougong.core.meta.OneToManyMeta;

@EntityMeta(entity = DEPT.class)
public class Department
{
	@DataMeta(alias = "rowid")
	private String		rowid;

	@ManyToOneMeta(model = Company.class, key = DEPT.FK_DEPT)
	private Company		company;

	@DataMeta(alias = "compId", serialize = false)
	private String		compId;

	private String		compName;

	@DataMeta(alias = "deptId")
	private String		id;

	@DataMeta(alias = "deptName")
	private String		name;

	@OneToManyMeta(model = Staff.class, key = STAF.FK_STAF, referred = false)
	private List<Staff>	staffs	= new LinkedList<Staff>();

	public Department()
	{
	}

	public Department(Company company, String id, String name)
	{
		this.setCompany(company);
		this.setId(id);
		this.setName(name);
	}

	public Company getCompany()
	{
		return company;
	}

	public String getCompId()
	{
		return this.getCompany() != null ? this.getCompany().getId() : this.compId;
	}

	public String getCompName()
	{
		return this.getCompany() != null ? this.getCompany().getName() : compName;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getRowid()
	{
		return rowid;
	}

	public List<Staff> getStaffs()
	{
		return staffs;
	}

	public void setCompany(Company company)
	{
		this.company = company;
	}

	public void setCompId(String compId)
	{
		this.compId = compId;
	}

	public void setCompName(String compName)
	{
		this.compName = compName;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setRowid(String rowid)
	{
		this.rowid = rowid;
	}

	public void setStaffs(List<Staff> staffs)
	{
		this.staffs = staffs;
	}

	@Override
	public String toString()
	{
		String str = "Depart: rowid=" + this.getRowid() + " id=" + this.getId() + ", name=" + this.getName()
				+ ", compId=" + this.getCompId() + ", compName=" + this.getCompName();

		if (this.getStaffs() != null)
		{
			str += "\n  [Staffs]";
			for (Staff s : this.getStaffs())
			{
				str += "\n    " + s.toString();
			}
		}

		return str;
	}
}
