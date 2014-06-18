package org.jmotor.util;

/**
 * Component:Utility
 * Description:Data validator
 * Date: 11-8-16
 *
 * @author Andy.Ai
 */
public interface Validator<T> {
    public boolean validate(T t);
}
