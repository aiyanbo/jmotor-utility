package org.jmotor.util.persistence.parser;

import org.jmotor.util.exception.EntityParseException;
import org.jmotor.util.persistence.annotation.Ignore;

/**
 * Component:
 * Description:
 * Date: 12-11-7
 *
 * @author Andy.Ai
 */
public interface EntityParserCallback {
    String[] filter(Class<?> entityClass) throws EntityParseException;

    String[] appendColumns(Class<?> entityClass) throws EntityParseException;

    String getColumnName(String propertyName, Class<?> entityClass) throws EntityParseException;

    Ignore.IgnoreType getIgnoreType(String propertyName, Class<?> entityClass) throws EntityParseException;

    String[] getPrimaryKeys(Class<?> entityClass) throws EntityParseException;

    String getTableName(Class<?> entityClass) throws EntityParseException;
}
