package org.kernelab.dougong.maria.dml;

import org.kernelab.dougong.core.Expression;
import org.kernelab.dougong.core.View;
import org.kernelab.dougong.semi.dml.AbstractReference;

public class MariaReference extends AbstractReference
{
	public MariaReference(View view, Expression refer)
	{
		super(view, refer);
	}

	@Override
	protected MariaReference replicate()
	{
		return new MariaReference(view(), refer());
	}

	public StringBuilder toString(StringBuilder buffer)
	{
		String alias = this.view().provider().provideAliasLabel(view().alias());
		if (alias != null)
		{
			buffer.append(alias);
			buffer.append('.');
		}
		this.view().provider().provideOutputNameText(buffer, name());
		return buffer;
	}
}
