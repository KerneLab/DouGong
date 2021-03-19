package org.kernelab.dougong.core.ddl;

/**
 * An AbsoluteKey should be an unchangeable unique key which refers to one
 * record row in its table. A typically AbsoluteKey is such column like
 * auto_increment or ROWID.
 */
public interface AbsoluteKey extends EntityKey
{
}
