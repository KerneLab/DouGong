package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Function;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.ListItem;
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

	public Column provideColumn(View view, String name);

	public ComparisonCondition provideComparisonCondition();

	public Delete provideDelete();

	public <T extends Function> T provideFunction(Class<T> cls);

	/**
	 * Provide function text including the function name and parameters' list.
	 * 
	 * @param function
	 * @return
	 */
	public String provideFunctionText(Function function);

	public Join provideJoin();

	public LikeCondition provideLikeCondition();

	public ListItem provideListItem();

	public MembershipCondition provideMembershipCondition();

	public NullCondition provideNullCondition();

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
	 * Output the order to the buffer including the white separating characters
	 * before the order.
	 * 
	 * @param buffer
	 * @param sort
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputOrder(StringBuilder buffer, Sortable sort);

	public Primitive providePrimitive();

	public RangeCondition provideRangeCondition();

	public Select provideSelect();

	public StringItem provideStringItem(String item);

	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select);

	public <T extends Table> T provideTable(Class<T> cls);

	/**
	 * Provide table name may be including schema name but excluding the alias
	 * name.
	 * 
	 * @param table
	 * @return
	 */
	public String provideTableName(Table table);

	/**
	 * Provide table name including alias name.
	 * 
	 * @param table
	 * @return
	 */
	public String provideTableNameAliased(Table table);

	public Update provideUpdate();
}
