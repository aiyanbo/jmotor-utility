package org.jmotor.util.persistence.parser.impl;

import org.jmotor.util.ClassUtilities;
import org.jmotor.util.CollectionUtilities;
import org.jmotor.util.StringUtilities;
import org.jmotor.util.exception.EntityParseException;
import org.jmotor.util.persistence.annotation.Ignore;
import org.jmotor.util.persistence.parser.EntityParserCallback;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Component:
 * Description:
 * Date: 12-11-7
 *
 * @author Andy.Ai
 */
public class JPAEntityParserCallback implements EntityParserCallback {
    @Override
    public String[] filter(Class<?> entityClass) throws EntityParseException {
        try {
            PropertyDescriptor[] propertyDescriptors = ClassUtilities.getPropertyDescriptors(entityClass);
            List<String> propertyNames = new ArrayList<String>(propertyDescriptors.length);
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method reader = propertyDescriptor.getReadMethod();
                Transient transientSymbol = reader.getAnnotation(Transient.class);
                if (transientSymbol == null) {
                    Field field = ClassUtilities.getField(entityClass, propertyDescriptor.getName());
                    if (field != null) {
                        transientSymbol = field.getAnnotation(Transient.class);
                    }
                }
                if (transientSymbol != null) {
                    propertyNames.add(propertyDescriptor.getName());
                }
            }
            return propertyNames.toArray(new String[propertyNames.size()]);
        } catch (Exception e) {
            throw new EntityParseException(e.getMessage(), e);
        }
    }

    @Override
    public String getColumnName(String propertyName, Class<?> entityClass) throws EntityParseException {
        String databaseColumnName = null;
        Field field;
        try {
            field = ClassUtilities.getField(entityClass, propertyName);
        } catch (NoSuchFieldException e) {
            throw new EntityParseException(e.getLocalizedMessage(), e);
        }
        if (field != null) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && StringUtilities.isNotBlank(column.name())) {
                databaseColumnName = column.name();
            }
        }
        if (StringUtilities.isBlank(databaseColumnName)) {
            databaseColumnName = StringUtilities.nameOfDatabase(propertyName);
        }
        return databaseColumnName;
    }

    @Override
    public Ignore.IgnoreType getIgnoreType(String propertyName, Class<?> entityClass) throws EntityParseException {
        Field field;
        try {
            field = ClassUtilities.getField(entityClass, propertyName);
        } catch (NoSuchFieldException e) {
            throw new EntityParseException(e.getLocalizedMessage(), e);
        }
        if (field != null) {
            Ignore ignore = field.getAnnotation(Ignore.class);
            if (ignore != null) {
                return ignore.type();
            }
        }
        return Ignore.IgnoreType.NONE;
    }

    @Override
    public String[] getPrimaryKeys(Class<?> entityClass) throws EntityParseException {
        Table tableSymbol = entityClass.getAnnotation(Table.class);
        if (tableSymbol != null) {
            UniqueConstraint[] uniqueConstraints = tableSymbol.uniqueConstraints();
            if (CollectionUtilities.isNotEmpty(uniqueConstraints)) {
                List<String> uniqueNames = new ArrayList<String>(5);
                for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
                    Collections.addAll(uniqueNames, uniqueConstraint.columnNames());
                }
                return uniqueNames.toArray(new String[uniqueNames.size()]);
            }
        }
        return null;
    }

    @Override
    public String getTableName(Class<?> entityClass) throws EntityParseException {
        String entityName = null;
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            entityName = tableAnnotation.name();
        }
        if (StringUtilities.isBlank(entityName)) {
            Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
            if (entityAnnotation != null && StringUtilities.isNotBlank(entityAnnotation.name())) {
                entityName = entityAnnotation.name();
            }
        }
        return StringUtilities.isNotBlank(entityName) ? entityName : entityClass.getSimpleName();
    }

    @Override
    public String[] appendColumns(Class<?> entityClass) throws EntityParseException {
        return new String[0];
    }
}
