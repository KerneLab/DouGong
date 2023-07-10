package org.kernelab.dougong.core.dml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.Relation;
import org.kernelab.dougong.core.View;

public interface Join extends Relation
{
	public static final String			NATURAL	= "NATURAL";

	public static final byte			DEFAULT	= -1;

	// DIRECT
	public static final byte			INNER	= 0;

	public static final byte			LEFT	= 1;

	public static final byte			RIGHT	= 2;

	public static final byte			FULL	= 3;

	public static final byte			CROSS	= 4;

	public static final List<String>	DIRECTS	= Collections
			.unmodifiableList(Tools.listOfArray(new ArrayList<String>(), "INNER", "LEFT", "RIGHT", "FULL", "CROSS"));

	// TYPE
	public static final byte			OUTER	= 0;

	public static final byte			SEMI	= 1;

	public static final byte			ANTI	= 2;

	public static final List<String>	TYPES	= Collections
			.unmodifiableList(Tools.listOfArray(new ArrayList<String>(), "OUTER", "SEMI", "ANTI"));

	public byte direct();

	public Join join(View leading, Join former, boolean natural, byte direct, byte type, View view, String alias);

	public boolean natural();

	public Condition on();

	public Join on(Condition condition);

	/**
	 * Spread items that using by join to this and former join object.
	 * 
	 * @param labels
	 * @return
	 */
	public Join spread(String... labels);

	public byte type();

	public Item[] using();

	public Join using(Item... items);

	public View view();

	/**
	 * To determine whether the items in this join view could be selected in
	 * {@code SELECT *}. Typically, the view on the right side of semi/anti join
	 * can not be selected any item.
	 * 
	 * @return
	 */
	public boolean viewSelectable();
}
