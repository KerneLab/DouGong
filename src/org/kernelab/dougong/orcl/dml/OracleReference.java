package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.core.dml.Expression;
import org.kernelab.dougong.semi.dml.AbstractReference;

public class OracleReference extends AbstractReference
{
	public OracleReference(View view, Expression refer)
	{
		super(view, refer);
	}

	@Override
	protected OracleReference replicate()
	{
		return new OracleReference(view(), refer());
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		String alias = this.view().provider().provideAliasLabel(view().alias());
		if (alias != null && !isUsingByJoin())
		{
			buffer.append(alias);
			buffer.append('.');
		}
		this.view().provider().provideOutputNameText(buffer, name());
		return buffer;
	}
}
