package org.jmotor.util.persistence.parser;

import org.jmotor.util.exception.EntityParseException;
import org.jmotor.util.persistence.dto.EntityMapper;

/**
 * Component:
 * Description:
 * Date: 12-11-7
 *
 * @author Andy.Ai
 */
public interface EntityParser {
    EntityMapper getEntityMapper(Class<?> entityClass) throws EntityParseException;
}
