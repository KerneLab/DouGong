package org.kernelab.dougong.semi.dml;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kernelab.basis.Canal;
import org.kernelab.basis.Mapper;
import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Column;
import org.kernelab.dougong.core.Function;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Pivot;
import org.kernelab.dougong.core.util.Utils;
import org.kernelab.dougong.semi.AbstractView;

public class AbstractPivot extends AbstractView implements Pivot
{
	private View				on;

	private String				alias;

	private Function[]			aggs;

	private Column[]			fors;

	private Item[]				ins;

	private List<Item>			items		= null;

	private Map<String, Item>	itemsMap	= null;

	@Override
	public String alias()
	{
		return alias;
	}

	@Override
	public AbstractPivot alias(String alias)
	{
		this.alias = alias;
		return this;
	}

	@Override
	public AllItems all()
	{
		return this.provider().provideAllItems(this);
	}

	@SuppressWarnings("unchecked")
	public AbstractPivot as(String alias)
	{
		return this.replicate().alias(alias);
	}

	protected Set<String> eliminates()
	{
		return (Set<String>) Canal.of(this.pivotAggs()).flatMap(new Mapper<Function, Iterable<Expression>>()
		{
			@Override
			public Iterable<Expression> map(Function f)
			{
				return Canal.of(f.args());
			}
		}).union(Canal.of(this.pivotFor()).map(new Mapper<Column, Expression>()
		{
			@Override
			public Expression map(Column c)
			{
				return c;
			}
		})).map(new Mapper<Expression, String>()
		{
			@Override
			public String map(Expression expr)
			{
				return Utils.getNameOfExpression(expr);
			}
		}).collect(new HashSet<String>());
	}

	@Override
	public List<Item> items()
	{
		if (this.items == null)
		{
			this.refreshItems();
		}
		return items;
	}

	protected String makeLabelOfPivotItem(Item item, Function func)
	{
		String label = item.alias() != null ? item.alias() : item.toStringExpress(new StringBuilder()).toString();
		if (func.alias() != null)
		{
			label += "_" + func.alias();
		}
		return label;
	}

	@Override
	public Function[] pivotAggs()
	{
		return aggs;
	}

	@Override
	public AbstractPivot pivotAggs(Function... aggs)
	{
		this.aggs = aggs;
		return this;
	}

	@Override
	public Column[] pivotFor()
	{
		return fors;
	}

	@Override
	public AbstractPivot pivotFor(Column... fors)
	{
		this.fors = fors;
		return this;
	}

	@Override
	public Item[] pivotIn()
	{
		return ins;
	}

	@Override
	public AbstractPivot pivotIn(Item... ins)
	{
		this.ins = ins;
		return this;
	}

	@Override
	public View pivotOn()
	{
		return on;
	}

	@Override
	public AbstractPivot pivotOn(View view)
	{
		this.on = view;
		return this;
	}

	@Override
	public Map<String, Item> referItems()
	{
		if (this.itemsMap == null)
		{
			this.refreshItems();
		}
		return this.itemsMap;
	}

	protected void refreshItems()
	{
		this.items = new LinkedList<Item>();
		this.itemsMap = new LinkedHashMap<String, Item>();

		Set<String> removes = this.eliminates();

		String label = null;
		Column col = null;

		for (Item item : this.pivotOn().referItems().values())
		{
			label = Utils.getLabelOfExpression(item);
			if (!removes.contains(label))
			{
				col = provider().provideReference(this.pivotOn(), label);
				this.items.add(col);
				this.itemsMap.put(label, col);
			}
		}

		for (Item item : this.pivotIn())
		{
			for (Function func : this.pivotAggs())
			{
				label = this.makeLabelOfPivotItem(item, func);
				col = this.provider().provideColumn(this, label, null);
				this.items.add(col);
				this.itemsMap.put(label, col);
			}
		}
	}

	protected AbstractPivot replicate()
	{
		return provider().provideProvider(new AbstractPivot() //
				.pivotAggs(this.pivotAggs()) //
				.pivotFor(this.pivotFor()) //
				.pivotIn(this.pivotIn()) //
				.pivotOn(this.pivotOn()) //
		);
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		this.pivotOn().toStringViewed(buffer);
		return toStringPivot(buffer);
	}

	@Override
	public StringBuilder toStringDeletable(StringBuilder buffer)
	{
		return toStringViewed(buffer);
	}

	public StringBuilder toStringPivot(StringBuilder buffer)
	{
		buffer.append(" PIVOT (");
		boolean first = true;
		for (Function func : this.pivotAggs())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			func.toStringSelected(buffer);
		}

		buffer.append(" FOR ");
		if (this.pivotFor().length > 1)
		{
			buffer.append('(');
		}
		first = true;
		for (Column col : this.pivotFor())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			buffer.append(col.name());
		}
		if (this.pivotFor().length > 1)
		{
			buffer.append(')');
		}

		buffer.append(" IN (");
		first = true;
		for (Item item : this.pivotIn())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append(',');
			}
			if (item instanceof Items)
			{
				buffer.append('(');
			}
			item.toStringExpress(buffer);
			if (item instanceof Items)
			{
				buffer.append(')');
			}
			if (Tools.notNullOrEmpty(item.alias()))
			{
				Utils.outputAlias(provider(), buffer, item);
			}
		}
		buffer.append(')');

		buffer.append(')');

		return buffer;
	}

	@Override
	public StringBuilder toStringUpdatable(StringBuilder buffer)
	{
		return toStringViewed(buffer);
	}

	@Override
	public StringBuilder toStringViewed(StringBuilder buffer)
	{
		this.toString(buffer);
		return Utils.outputAlias(this.provider(), buffer, this);
	}
}
