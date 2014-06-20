package org.jmotor.util.persistence.parser.impl;

import org.jmotor.util.ClassUtilities;
import org.jmotor.util.CollectionUtilities;
import org.jmotor.util.StringUtilities;
import org.jmotor.util.exception.EntityParseException;
import org.jmotor.util.persistence.annotation.Ignore;
import org.jmotor.util.persistence.dto.EntityMapper;
import org.jmotor.util.persistence.dto.PropertyMapper;
import org.jmotor.util.persistence.parser.EntityParser;
import org.jmotor.util.persistence.parser.EntityParserCallback;

import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Component:
 * Description:
 * Date: 12-11-7
 *
 * @author Andy.Ai
 */
public class EntityParserImpl implements EntityParser {
    private EntityParserCallback callback;

    @Override
    public EntityMapper getEntityMapper(Class<?> entityClass) throws EntityParseException {
        try {
            PropertyDescriptor[] propertyDescriptors = ClassUtilities.getPropertyDescriptors(entityClass);
            String[] filters = null;
            String tableName = entityClass.getName();
            String[] appendColumns = null;
            String[] uniqueNames = null;
            if (null != callback) {
                filters = callback.filter(entityClass);
                tableName = callback.getTableName(entityClass);
                appendColumns = callback.appendColumns(entityClass);
                uniqueNames = callback.getPrimaryKeys(entityClass);
            }
            String identityName = getIdentityName(entityClass, propertyDescriptors);
            PropertyMapper propertyMapper = new PropertyMapper();
            Map<Ignore.IgnoreType, Set<String>> ignores = new HashMap<>();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                boolean included = true;
                String propertyName = propertyDescriptor.getName();
                if (CollectionUtilities.isNotEmpty(filters)) {
                    for (String filter : filters) {
                        if (propertyName.equals(filter)) {
                            included = false;
                            break;
                        }
                    }
                }
                if (included) {
                    String columnName;
                    Ignore.IgnoreType ignoreType;
                    if (callback != null) {
                        columnName = callback.getColumnName(propertyName, entityClass);
                        ignoreType = callback.getIgnoreType(propertyName, entityClass);
                    } else {
                        columnName = StringUtilities.nameOfDatabase(propertyName);
                        ignoreType = Ignore.IgnoreType.NONE;
                    }
                    propertyMapper.put(propertyName, columnName);
                    switch (ignoreType) {
                        case NONE:
                            break;
                        case CREATE:
                        case UPDATE:
                            Set<String> ignoreProperties = ignores.get(ignoreType);
                            if (ignoreProperties == null) {
                                ignoreProperties = new HashSet<>();
                                ignores.put(ignoreType, ignoreProperties);
                            }
                            ignoreProperties.add(propertyName);
                            break;
                        default:
                            break;
                    }
                }
            }
            if (CollectionUtilities.isNotEmpty(appendColumns)) {
                for (String column : appendColumns) {
                    propertyMapper.put(column, column);
                }
            }
            EntityMapper entityMapper = new EntityMapper();
            entityMapper.setTableName(tableName);
            entityMapper.setIdentityName(identityName);
            entityMapper.setUniqueNames(uniqueNames);
            entityMapper.setPropertyMapper(propertyMapper);
            return entityMapper;
        } catch (Exception e) {
            throw new EntityParseException(e.getMessage(), e);
        }
    }

    private String getIdentityName(Class<?> entityClass, PropertyDescriptor[] propertyDescriptors) throws NoSuchFieldException {
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method reader = propertyDescriptor.getReadMethod();
            Id identity = reader.getAnnotation(Id.class);
            if (identity == null) {
                Field field = ClassUtilities.getField(entityClass, propertyDescriptor.getName());
                if (field != null) {
                    identity = field.getAnnotation(Id.class);
                }
            }
            if (identity != null) {
                return propertyDescriptor.getName();
            }
        }
        return null;
    }

    public void setCallback(EntityParserCallback callback) {
        this.callback = callback;
    }
}
