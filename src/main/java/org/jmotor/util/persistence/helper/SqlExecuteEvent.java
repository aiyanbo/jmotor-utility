package org.jmotor.util.persistence.helper;

/**
 * Component:
 * Description:
 * Date: 13-10-8
 *
 * @author Andy Ai
 */
public interface SqlExecuteEvent {
    void executeBefore();

    void executeAfter();
}
