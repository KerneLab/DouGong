package org.kernelab.dougong.core;

import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.opr.CaseDecideExpression;
import org.kernelab.dougong.core.dml.opr.CaseSwitchExpression;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.JointOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;

public interface Provider
{
	/**
	 * Provide label string of an alias name. The label string should including
	 * the boundary characters.
	 * 
	 * @return The label string or null if the name was an illegal alias name.
	 */
	public String provideAliasLabel(String name);

	/**
	 * Provide an AllItems object which stands for all items of the View. The
	 * view could be null which means all items from all source views.
	 */
	public AllItems provideAllItems(View view);

	public CaseDecideExpression provideCaseExpression();

	public CaseSwitchExpression provideCaseExpression(Expression value);

	public Column provideColumn(View view, String name);

	public ComparisonCondition provideComparisonCondition();

	public Delete provideDelete();

	public DivideOperator provideDivideOperator();

	public <T extends Function> T provideFunction(Class<T> cls);

	public Insert provideInsert();

	public Items provideItems();

	public Join provideJoin();

	public JointOperator provideJointOperator();

	public LikeCondition provideLikeCondition();

	public LogicalCondition provideLogicalCondition();

	public MembershipCondition provideMembershipCondition();

	public MinusOperator provideMinusOperator();

	public MultiplyOperator provideMultiplyOperator();

	/**
	 * Get the name text.<br />
	 * Usually the text might be surrounding with boundary characters.
	 * 
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

	public PlusOperator providePlusOperator();

	public Primitive providePrimitive();

	public RangeCondition provideRangeCondition();

	/**
	 * Provide a Reference which refers to the given expression within the given
	 * view.
	 */
	public Reference provideReference(View view, Expression expr);

	/**
	 * Get the reference name of an expression which is usually used by outer
	 * query.<br />
	 * First, its alias name should be considered if specified.<br />
	 * If it is a Column then the leading table alias must NOT appear.<br />
	 * Otherwise, the whole expression itself would be the refer string.<br />
	 * Different from label, the reference name must NOT be surrounded with
	 * boundary characters.
	 */
	public String provideReferName(Expression expr);

	public Select provideSelect();

	public SQL provideSQL();

	/**
	 * Provide a StringItem exactly according to the given expression.
	 */
	public StringItem provideStringItem(String expr);

	public <T extends Subquery> T provideSubquery(Class<T> cls);

	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select);

	public <T extends Table> T provideTable(Class<T> cls);

	public Update provideUpdate();
}
