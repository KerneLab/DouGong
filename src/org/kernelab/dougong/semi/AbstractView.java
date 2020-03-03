package org.kernelab.dougong.semi;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;

public abstract class AbstractView extends AbstractProvidable implements View
{
	private String				alias		= null;

	private List<Item>			items		= new LinkedList<Item>();

	private Map<String, Item>	itemsMap	= new LinkedHashMap<String, Item>();

	public AbstractView()
	{
		super();
	}

	public String alias()
	{
		return alias;
	}

	public AbstractView alias(String alias)
	{
		this.alias = Tools.notNullOrWhite(alias) ? alias : null;
		return this;
	}

	public Delete delete()
	{
		return this.provider().provideDelete().from(this);
	}

	public Insert insert()
	{
		return this.provider().provideInsert().into((Insertable) this);
	}

	public Item item(String refer)
	{
		return itemsMap().get(refer);
	}

	public List<Item> items()
	{
		return items;
	}

	public Map<String, Item> itemsMap()
	{
		return itemsMap;
	}

	public Select select()
	{
		return this.provider().provideSelect().from(this);
	}

	public Update update()
	{
		return this.provider().provideUpdate().from(this);
	}
}
