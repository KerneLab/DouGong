package org.kernelab.dougong.core;

import org.kernelab.dougong.core.dml.Join;
import org.kernelab.dougong.core.dml.ListItem;
import org.kernelab.dougong.core.dml.Select;
import org.kernelab.dougong.core.dml.Subquery;
import org.kernelab.dougong.core.dml.StringItem;
import org.kernelab.dougong.core.dml.cond.ComparisonCondition;
import org.kernelab.dougong.core.dml.cond.LikeCondition;
import org.kernelab.dougong.core.dml.cond.MembershipCondition;
import org.kernelab.dougong.core.dml.cond.NullCondition;
import org.kernelab.dougong.core.dml.cond.RangeCondition;

public interface Provider
{
	public Column provideColumn(View view, String name);

	public ComparisonCondition provideComparisonCondition();

	public Join provideJoin();

	public LikeCondition provideLikeCondition();

	public ListItem provideListItem();

	public MembershipCondition provideMembershipCondition();

	public NullCondition provideNullCondition();

	public RangeCondition provideRangeCondition();

	public Select provideSelect();

	public StringItem provideStringItem(String item);

	public <T extends Subquery> T provideSubquery(Class<T> cls, Select select);

	public <T extends Table> T provideTable(Class<T> cls);

	public String provideTableName(Table table);

	public String provideTableNameAliased(Table table);
}
