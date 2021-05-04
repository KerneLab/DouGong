package org.kernelab.dougong.test;

import org.kernelab.basis.Tools;
import org.kernelab.dougong.core.meta.MappingMeta;

public class TestSub extends TestObject
{
	@MappingMeta
	private int	val;

	public static void main(String[] args) throws SecurityException, NoSuchFieldException
	{
		Tools.debug(TestSub.class.getDeclaredField("val").isAnnotationPresent(MappingMeta.class));
	}
}
