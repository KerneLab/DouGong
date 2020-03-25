package org.kernelab.dougong.core.meta;

public class RelationMeta
{
	private Class<?>	model;

	private String		key;

	private boolean		referred;

	public RelationMeta(Class<?> model, String key, boolean referred)
	{
		this.model(model);
		this.key(key);
		this.referred(referred);
	}

	public RelationMeta(ManyToOneMeta meta)
	{
		this(meta.model(), meta.key(), meta.referred());
	}

	public RelationMeta(OneToManyMeta meta)
	{
		this(meta.model(), meta.key(), meta.referred());
	}

	public RelationMeta(OneToOneMeta meta)
	{
		this(meta.model(), meta.key(), meta.referred());
	}

	public String key()
	{
		return key;
	}

	public void key(String key)
	{
		this.key = key;
	}

	public Class<?> model()
	{
		return model;
	}

	public void model(Class<?> model)
	{
		this.model = model;
	}

	public boolean referred()
	{
		return referred;
	}

	public void referred(boolean referred)
	{
		this.referred = referred;
	}
}
