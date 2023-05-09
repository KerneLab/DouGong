package org.kernelab.dougong.core.dml.opr;

public interface ArithmeticOperable
		extends PlusOperable, MinusOperable, NegativeOperable, MultiplyOperable, DivideOperable, ModuloOperable
{
	public static final String	PLUS		= "+";

	public static final String	MINUS		= "-";

	public static final String	MULTIPLY	= "*";

	public static final String	DIVIDE		= "/";

	public static final String	MODULO		= "%";
}
