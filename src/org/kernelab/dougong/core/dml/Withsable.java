package org.kernelab.dougong.core.dml;

import java.util.List;

public interface Withsable
{
	public List<Withable> with();

	public Withsable with(List<Withable> views);

	public Withsable with(Withable... views);
}
