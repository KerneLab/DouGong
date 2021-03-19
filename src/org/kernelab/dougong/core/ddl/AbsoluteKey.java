package org.kernelab.dougong.core.ddl;

/**
 * An AbsoluteKey should be an unchangeable unique key which refers to one
 * record row in its table. Usually, the AbsoluteKey column should not be the
 * column to be inserted. A typical AbsoluteKey is such column like
 * auto_increment or ROWID.
 */
public interface AbsoluteKey extends EntityKey
{
}
