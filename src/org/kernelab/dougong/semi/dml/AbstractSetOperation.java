package org.kernelab.dougong.semi.dml;


public class AbstractSetOperation
{
	public static final byte		UNION_ALL	= 0;

	public static final byte		UNION		= 1;

	public static final byte		INTERSECT	= 2;

	public static final byte		MINUS		= 3;

	public static final String[]	JOINS		= new String[] { "UNION ALL", "UNION", "INTERSECT", "MINUS" };

	public static void main(String[] args)
	{

	}

	public AbstractSetOperation(byte op, AbstractSelect select)
	{

	}
}
