package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.AllColumns;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public interface Provider
{
	/**
	 * Provide label string of a alias name.
	 * 
	 * @param name
	 * @return The label string or null if the name was an illegal alias name.
	 */
	public String provideAliasLabel(String name);

	/**
	 * Provide an AllColumns object which stands for all columns of the View.
	 * 
	 * @return
	 */
	public AllColumns provideAllColumns(View view);

	public Column provideColumn(View view, String name);

	public ComparisonCondition provideComparisonCondition();

	public Delete provideDelete();

	public <T extends Function> T provideFunction(Class<T> cls);

	public Items provideItems();

	public Join provideJoin();

	public LikeCondition provideLikeCondition();

	public MembershipCondition provideMembershipCondition();

	/**
	 * Get the name text.<br />
	 * Usually the text might be surrounding with boundary characters.
	 * 
	 * @param buffer
	 * @param name
	 *            The name String without boundary characters.
	 * @return The text or SQL.NULL if the name is null.
	 */
	public String provideNameText(String name);

	public NullCondition provideNullCondition();

	/**
	 * Provide a null Item.
	 * 
	 * @return
	 */
	public Item provideNullItem();

	/**
	 * Output the alias name, if given, to the buffer including the white
	 * separating characters before the alias.
	 * 
	 * @param buffer
	 * @param alias
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputAlias(StringBuilder buffer, Alias alias);

	/**
	 * Output function text including the function name and parameters' list to
	 * the buffer.<br />
	 * The schema name should also be considered if given.
	 * 
	 * @param buffer
	 * @param function
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputFunction(StringBuilder buffer, Function function);

	/**
	 * Output the member text with the schema name, if given, to the buffer.
	 * 
	 * @param buffer
	 * @param member
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputMember(StringBuilder buffer, Member member);

	/**
	 * Output the name text to the buffer.
	 * 
	 * @param buffer
	 * @param name
	 *            The name without boundary characters.
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputNameText(StringBuilder buffer, String name);

	/**
	 * Output the order to the buffer including the white separating characters
	 * before the order.
	 * 
	 * @param buffer
	 * @param sort
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputOrder(StringBuilder buffer, Sortable sort);

	public StringBuilder provideOutputTableName(StringBuilder buffer, Table table);

	public StringBuilder provideOutputTableNameAliased(StringBuilder buffer, Table table);

	public Primitive providePrimitive();

	public RangeCondition provideRangeCondition();

	public Select provideSelect();

	public StringItem provideStringItem(String expr);

	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select);

	public <T extends Table> T provideTable(Class<T> cls);

	// /**
	// * Provide table name text may be including schema name but excluding the
	// * alias name.
	// *
	// * @param table
	// * @return
	// */
	// public String provideTableNameText(Table table);
	//
	// /**
	// * Provide table name text including alias name.
	// *
	// * @param table
	// * @return
	// */
	// public String provideTableNameTextAliased(Table table);

	public Update provideUpdate();
}
