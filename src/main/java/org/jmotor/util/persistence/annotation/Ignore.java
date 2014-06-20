package org.jmotor.util.persistence.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Component:
 * Description:
 * Date: 14-6-20
 *
 * @author Andy Ai
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Ignore {
    public static enum IgnoreType {
        CREATE, UPDATE
    }

    IgnoreType type();
}
