package org.kernelab.dougong.test;

import java.sql.SQLException;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.meta.DataMeta;
import org.kernelab.dougong.core.meta.EntityMeta;
import org.kernelab.dougong.core.meta.Entitys;
import org.kernelab.dougong.demo.Config;

@EntityMeta(entity = DEMO_ENTITY.class)
public class DemoEntity
{
	public static void main(String[] args)
	{
		DemoEntity d = new DemoEntity();
		d.setGender("1");

		try
		{
			Tools.debug(d);

			Entitys.insertObjectAlone(Config.getSQLKit(), Config.SQL, d);

			Tools.debug(d);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@DataMeta(alias = "rowid")
	private Long	rowid;

	@DataMeta(alias = "id")
	private String	id;

	@DataMeta(alias = "gender")
	private String	gender;

	public String getGender()
	{
		return gender;
	}

	public String getId()
	{
		return id;
	}

	public Long getRowid()
	{
		return rowid;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setRowid(Long rowid)
	{
		this.rowid = rowid;
	}

	@Override
	public String toString()
	{
		return "rowid=" + this.getRowid() + ", id=" + this.getId() + ", gender=" + this.getGender();
	}
}
