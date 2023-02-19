package org.kernelab.dougong.semi;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Insertable;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Pivot;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Update;

public abstract class AbstractView extends AbstractText implements View
{
	private String				alias		= null;

	private List<Item>			items		= new LinkedList<Item>();

	private Map<String, Item>	itemsMap	= new LinkedHashMap<String, Item>();

	private Set<String>			usingLabels	= null;

	public AbstractView()
	{
		super();
	}

	@Override
	public Reference $(String refer)
	{
		return ref(refer);
	}

	@Override
	public String alias()
	{
		return alias;
	}

	@Override
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

	@Override
	public boolean isJoinUsing(String label)
	{
		return this.usingLabels != null && this.usingLabels.contains(label);
	}

	@Override
	public Item item(String name)
	{
		return referItems().get(name);
	}

	@Override
	public List<Item> items()
	{
		return items;
	}

	@Override
	public void joinUsing(String... labels)
	{
		if (labels != null && labels.length > 0)
		{
			if (this.usingLabels == null)
			{
				this.usingLabels = new LinkedHashSet<String>();
			}
			this.usingLabels.addAll(Tools.setOfArray(new LinkedHashSet<String>(), labels));
		}
	}

	public Pivot pivot(Function... aggs)
	{
		return provider().providePivot().pivotOn(this).pivotAggs(aggs);
	}

	@Override
	public Reference ref(String refer)
	{
		Item item = item(refer);

		if (item != null)
		{
			return this.provider().provideReference(this, item.label());
		}
		else
		{
			return null;
		}
	}

	@Override
	public Map<String, Item> referItems()
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
