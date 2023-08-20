package org.kernelab.dougong.core.meta;

import java.sql.SQLException;
import java.util.Map;

import org.kernelab.basis.JSON;
import org.kernelab.dougong.core.ddl.ForeignKey;

public abstract class ForeignKeyException extends SQLException
{
	public static class ExistingReferenceException extends ForeignKeyException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3544784591539362947L;

		public ExistingReferenceException(String reason)
		{
			super(reason);
		}
	}

	public static class MissingReferenceException extends ForeignKeyException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6225716004960647013L;

		public MissingReferenceException(String reason)
		{
			super(reason);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5037853551978101225L;

	public static ExistingReferenceException existingReference(ForeignKey key, Map<String, Object> params)
	{
		return new ExistingReferenceException(
				"Still existing reference " + key.toString() + " " + new JSON().attrAll(params).toString());
	}

	public static MissingReferenceException missingReference(ForeignKey key, Map<String, Object> params)
	{
		return new MissingReferenceException(
				"Missing reference target " + key.toString() + " " + new JSON().attrAll(params).toString());
	}

	public ForeignKeyException(String reason)
	{
		super(reason);
	}
}
