package org.jmotor.util.persistence.dto;

import org.jmotor.util.persistence.annotation.Ignore;

import java.util.Map;
import java.util.Set;

/**
 * Component:
 * Description:
 * Date: 12-11-7
 *
 * @author Andy.Ai
 */
public class EntityMapper {
    private String tableName;
    private String identityName;
    private String[] uniqueNames;
    private PropertyMapper propertyMapper;
    private Map<Ignore.IgnoreType, Set<String>> ignores;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String[] getUniqueNames() {
        return uniqueNames;
    }

    public void setUniqueNames(String[] uniqueNames) {
        this.uniqueNames = uniqueNames;
    }

    public PropertyMapper getPropertyMapper() {
        return propertyMapper;
    }

    public void setPropertyMapper(PropertyMapper propertyMapper) {
        this.propertyMapper = propertyMapper;
    }

    public Map<Ignore.IgnoreType, Set<String>> getIgnores() {
        return ignores;
    }

    public void setIgnores(Map<Ignore.IgnoreType, Set<String>> ignores) {
        this.ignores = ignores;
    }
}
