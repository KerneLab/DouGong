package org.kernelab.dougong.orcl.dml;

import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractReference;

public class OracleReference extends AbstractReference
{
	public OracleReference(View view, String name)
	{
		super(view, name);
	}

	@Override
	protected OracleReference replicate()
	{
		return (OracleReference) new OracleReference(view(), name()).replicateOrderOf(this);
	}

	@Override
	public StringBuilder toString(StringBuilder buffer)
	{
		if (!isUsingByJoin())
		{
			String alias = this.view().provider().provideAliasLabel(view().provider().provideViewAlias(view()));
			if (alias != null)
			{
				buffer.append(alias);
				buffer.append('.');
			}
		}
		this.view().provider().provideOutputNameText(buffer, name());
		return buffer;
	}
}
