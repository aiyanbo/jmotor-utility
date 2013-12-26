package org.jmotor.util.exception;

/**
 * Component:
 * Description:
 * Date: 13-11-19
 *
 * @author Andy Ai
 */
public class ParseSettingsException extends RuntimeException {
    public ParseSettingsException() {
    }

    public ParseSettingsException(String message) {
        super(message);
    }

    public ParseSettingsException(String message, Throwable cause) {
        super(message, cause);
    }
}
