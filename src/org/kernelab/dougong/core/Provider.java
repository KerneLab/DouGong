package org.kernelab.dougong.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.kernelab.basis.Castable;
import org.kernelab.basis.JSON;
import org.kernelab.basis.JSON.JSAN;
import org.kernelab.basis.sql.SQLKit;
import org.kernelab.dougong.SQL;
import org.kernelab.dougong.core.ddl.AbsoluteKey;
import org.kernelab.dougong.core.ddl.ForeignKey;
import org.kernelab.dougong.core.ddl.PrimaryKey;
import org.kernelab.dougong.core.ddl.table.CreateTable;
import org.kernelab.dougong.core.ddl.table.DropTable;
import org.kernelab.dougong.core.dml.Alias;
import org.kernelab.dougong.core.dml.AllItems;
import org.kernelab.dougong.core.dml.Delete;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.core.dml.Insert;
import org.kernelab.dougong.core.dml.Item;
import org.kernelab.dougong.core.dml.Items;
import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.Merge;
import org.kernelab.dougong.core.dml.Pivot;
import org.kernelab.dougong.core.dml.Primitive;
import org.kernelab.dougong.core.dml.PriorExpression;
import org.kernelab.dougong.core.dml.Reference;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Setopr;
import org.kernelab.dougong.core.dml.Sortable;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.Update;
import org.kernelab.dougong.core.dml.WithDefinition;
import org.kernelab.dougong.core.dml.Withable;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.ComposableCondition;
import org.kernelab.dougong.core.dml.cond.ExistsCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.LogicalCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;
import org.kernelab.dougong.core.dml.cond.RegexpLikeCondition;
import org.kernelab.dougong.core.dml.opr.CaseDecideExpression;
import org.kernelab.dougong.core.dml.opr.CaseSwitchExpression;
import org.kernelab.dougong.core.dml.opr.DivideOperator;
import org.kernelab.dougong.core.dml.opr.JointOperator;
import org.kernelab.dougong.core.dml.opr.MinusOperator;
import org.kernelab.dougong.core.dml.opr.ModuloOperator;
import org.kernelab.dougong.core.dml.opr.MultiplyOperator;
import org.kernelab.dougong.core.dml.opr.NegativeOperator;
import org.kernelab.dougong.core.dml.opr.PlusOperator;
import org.kernelab.dougong.core.dml.opr.Result;
import org.kernelab.dougong.core.dml.param.ByteParam;
import org.kernelab.dougong.core.dml.param.CharParam;
import org.kernelab.dougong.core.dml.param.DateParam;
import org.kernelab.dougong.core.dml.param.DecimalParam;
import org.kernelab.dougong.core.dml.param.DoubleParam;
import org.kernelab.dougong.core.dml.param.FloatParam;
import org.kernelab.dougong.core.dml.param.IntParam;
import org.kernelab.dougong.core.dml.param.IterableParam;
import org.kernelab.dougong.core.dml.param.JSANParam;
import org.kernelab.dougong.core.dml.param.JSONParam;
import org.kernelab.dougong.core.dml.param.LongParam;
import org.kernelab.dougong.core.dml.param.MapParam;
import org.kernelab.dougong.core.dml.param.ObjectParam;
import org.kernelab.dougong.core.dml.param.Param;
import org.kernelab.dougong.core.dml.param.ShortParam;
import org.kernelab.dougong.core.dml.param.StringParam;
import org.kernelab.dougong.core.dml.param.TimestampParam;
import org.kernelab.dougong.core.dml.test.NegativeSemiTestable;
import org.kernelab.dougong.core.meta.Entitys.GenerateValueColumns;
import org.kernelab.dougong.core.util.KeysFetcher;
import org.kernelab.dougong.core.util.Recursor;

public interface Provider extends Castable, Serializable
{
	/**
	 * Indicate whether selected table column will be filled alias by its field
	 * name in class automatically or not.
	 * 
	 * @return
	 */
	public boolean isAutoFillSelectTableColumnAliasByFieldName();

	public AbsoluteKey provideAbsoluteKey(Entity entity, Column... columns);

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

	public Column provideColumn(View view, String name, Field field);

	/**
	 * Provide the type value in {@link java.sql.Types} of the given column.
	 * 
	 * @param column
	 * @return The value in Types according to the column's
	 *         {@code TypeMeta.type}, or {@code Types.NULL} if the column is
	 *         {@code null}, otherwise {@code Types.OTHER} would be returned.
	 */
	public int provideColumnType(Column column);

	public ComparisonCondition provideComparisonCondition();

	public CreateTable provideCreateTable();

	public <T> T provideDao(Class<T> cls);

	public <T> T provideDao(Class<T> face, Object real);

	/**
	 * Provide date time format according to the given format in
	 * {@link java.text.SimpleDateFormat}. Only numeric component will appear in
	 * the format.
	 * 
	 * @param format
	 *            A date time format in {@link java.text.SimpleDateFormat}
	 * @return
	 */
	public String provideDatetimeFormat(String format);

	public String provideDefaultLabel(Column column);

	public Delete provideDelete();

	public DivideOperator provideDivideOperator();

	/**
	 * Execute the Insert statement and return the generates ResultSet according
	 * to the given generate value columns.
	 * 
	 * @param kit
	 * @param sql
	 * @param insert
	 * @param params
	 * @param returns
	 * @return
	 * @throws SQLException
	 */
	public ResultSet provideDoInsertAndReturnGenerates(SQLKit kit, SQL sql, Insert insert, Map<String, Object> params,
			Column[] returns) throws SQLException;

	/**
	 * Execute the Insert statement and return the generates ResultSet which
	 * contains both AbsoluteKey and generate value columns.
	 * 
	 * @param kit
	 * @param sql
	 * @param insert
	 * @param params
	 * @param generates
	 * @param returns
	 * @return
	 */
	public ResultSet provideDoInsertAndReturnGenerates(SQLKit kit, SQL sql, Insert insert, Map<String, Object> params,
			GenerateValueColumns generates, Column[] returns) throws SQLException;

	public DropTable provideDropTable();

	public String provideEscapeValueLiterally(Object value);

	public ExistsCondition provideExistsCondition();

	/**
	 * Fill alias of select table column by its field name in table class. Item
	 * that has already been specified alias or non-table-column will be ignored
	 * by this method. Normally, this method is called when the items in Select
	 * being resolved (before ref(), toString() and so on). Subclass may turn
	 * off this feature by return the items argument directly.
	 * 
	 * @param item
	 * @return
	 */
	public Item provideFillSelectTableColumnAliasByFieldName(Item item);

	public ForeignKey provideForeignKey(PrimaryKey reference, Entity entity, Column... columns);

	public <T extends Function> T provideFunction(Class<T> cls);

	/**
	 * Get the full hint expression. If hint is null or white then returns null.
	 * 
	 * @param hint
	 * @return
	 */
	public String provideHint(String hint);

	public Insert provideInsert();

	public ComposableCondition provideIsEmptyCondition(Expression expr);

	public ComposableCondition provideIsNotEmptyCondition(Expression expr);

	public Items provideItems();

	public Join provideJoin();

	public JointOperator provideJointOperator();

	public KeysFetcher provideKeysFetcher();

	public Expression provideLikeAmongPattern(Expression pattern, String escape);

	public LikeCondition provideLikeCondition();

	public Expression provideLikeHeadPattern(Expression pattern, String escape);

	public Expression provideLikePatternDefaultEscape();

	public Expression provideLikePatternEscaped(Expression value, String escape);

	public Expression provideLikeTailPattern(Expression pattern, String escape);

	public LogicalCondition provideLogicalCondition();

	public MembershipCondition provideMembershipCondition();

	public Merge provideMerge();

	public MinusOperator provideMinusOperator();

	public ModuloOperator provideModuloOperator();

	public MultiplyOperator provideMultiplyOperator();

	/**
	 * Get the name text.<br />
	 * Usually the text might be surrounding with boundary characters.<br />
	 * Boundary character in the name should also be escaped.
	 * 
	 * @param name
	 *            The name String without boundary characters.
	 * @return The text or SQL.NULL if the name is null.
	 */
	public String provideNameText(String name);

	public NegativeOperator provideNegativeOperator();

	public NegativeSemiTestable provideNegativeSemiTestable(Expression expr);

	public <T> T provideNewInstance(Class<T> cls);

	public NullCondition provideNullCondition();

	/**
	 * Provide a null Item.
	 * 
	 * @return
	 */
	public Item provideNullItem();

	/**
	 * Provide literal of a given number.
	 * 
	 * @param number
	 * @return
	 */
	public String provideNumberLiteral(Number number);

	/**
	 * Output the alias name, if given, to the buffer including the white
	 * separating characters before the alias.
	 * 
	 * @param buffer
	 * @param alias
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputAlias(StringBuilder buffer, Alias alias);

	public StringBuilder provideOutputColumnExpress(StringBuilder buffer, Column column);

	/**
	 * Output the column reference which is typically used as insert target
	 * columns. This method should always excluding the column alias.
	 * 
	 * @param buffer
	 * @param column
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputColumnInsert(StringBuilder buffer, Column column);

	/**
	 * Output the column reference which is typically used as update columns.
	 * This method should always including the leading view alias (whether the
	 * column is using by join or not), but excluding the column alias.
	 * 
	 * @param buffer
	 * @param column
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputColumnReference(StringBuilder buffer, Column column);

	public StringBuilder provideOutputColumnSelect(StringBuilder buffer, Column column);

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
	 * @param level
	 * @return The given buffer.
	 */
	public StringBuilder provideOutputMember(StringBuilder buffer, Member member, int level);

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

	public StringBuilder provideOutputTableNameAliased(StringBuilder buffer, Table table);

	public StringBuilder provideOutputTableNameInsert(StringBuilder buffer, Table table);

	public StringBuilder provideOutputTablePartitionClause(StringBuilder buffer, Partitioned part);

	public StringBuilder provideOutputWithableAliased(StringBuilder buffer, Withable with);

	public StringBuilder provideOutputWithDefinition(StringBuilder buffer, WithDefinition with);

	public Param<?> provideParameter(Class<? extends Param<?>> type, String name, Object value);

	public StringItem provideParameter(String name);

	public DecimalParam provideParameter(String name, BigDecimal value);

	public ByteParam provideParameter(String name, Byte value);

	public CharParam provideParameter(String name, Character value);

	public DateParam provideParameter(String name, Date value);

	public DoubleParam provideParameter(String name, Double value);

	public FloatParam provideParameter(String name, Float value);

	public IntParam provideParameter(String name, Integer value);

	public IterableParam provideParameter(String name, Iterable<?> value);

	public JSANParam provideParameter(String name, JSAN value);

	public JSONParam provideParameter(String name, JSON value);

	public LongParam provideParameter(String name, Long value);

	public <K, V> MapParam<K, V> provideParameter(String name, Map<K, V> value);

	public ShortParam provideParameter(String name, Short value);

	public StringParam provideParameter(String name, String value);

	public <T> ObjectParam<T> provideParameter(String name, T value);

	public TimestampParam provideParameter(String name, Timestamp value);

	public Param<?> provideParameterByValue(String name, Object value);

	/**
	 * To provide an expression of parameter according to the given name.
	 * 
	 * @param name
	 * @return
	 */
	public String provideParameterExpression(String name);

	public Class<? extends Param<?>> provideParameterType(Class<?> type);

	/**
	 * To provide a pivot clause object.
	 * 
	 * @return
	 */
	public Pivot providePivot();

	public PlusOperator providePlusOperator();

	public PrimaryKey providePrimaryKey(Entity entity, Column... columns);

	public Primitive providePrimitive();

	public PriorExpression providePriorExpression(Expression expr);

	public <T extends Providable> T provideProvider(Providable providable);

	public RangeCondition provideRangeCondition();

	public Recursor provideRecursor(Primitive view);

	/**
	 * Provide a Reference which refers to the given expression within the given
	 * view.
	 */
	public Reference provideReference(View view, String name);

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

	public RegexpLikeCondition provideRegexpCondition();

	public Result provideResult(String expression);

	public Select provideSelect();

	public Setopr provideSetopr();

	public SQL provideSQL();

	/**
	 * Convert the given type name into standard type name defined in
	 * {@link java.sql.Types}.
	 * 
	 * @param name
	 * @return
	 */
	public String provideStandardTypeName(String name);

	/**
	 * Provide a StringItem exactly according to the given expression.
	 */
	public StringItem provideStringItem(String expr);

	public <T extends Subquery> T provideSubquery(Class<T> cls);

	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select);

	public <T extends Table> T provideTable(Class<T> cls);

	/**
	 * Provide content of a given string which excluding opening and closing
	 * marks.
	 * 
	 * @param text
	 * @return
	 */
	public String provideTextContent(CharSequence text);

	/**
	 * Provide literal of a given string which including opening and closing
	 * marks.
	 * 
	 * @param text
	 * @return
	 */
	public String provideTextLiteral(CharSequence text);

	public Result provideToLowerCase(Expression expr);

	/**
	 * Provide an AllItems object which stands for total items of all Views.
	 */
	public AllItems provideTotalItems();

	public Result provideToUpperCase(Expression expr);

	/**
	 * Provide type in {@link java.sql.Types} according to the given type name.
	 * 
	 * @param name
	 * @return The type value in Types or null if could not found.
	 */
	public Integer provideTypeByName(String name);

	public Update provideUpdate();

	public <T extends View> T provideView(Class<T> cls);

	public String provideViewAlias(View view);

	public WithDefinition provideWithDefinition(String name, String... columns);
}
