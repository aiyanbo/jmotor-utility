package org.jmotor.util;

/**
 * Component:Utility
 * Description:Boolean utilities
 * Date: 11-11-23
 *
 * @author Andy.Ai
 */
public class BooleanUtilities {
    private BooleanUtilities() {
    }

    public static boolean valueOf(String value) {
        if (StringUtilities.isNotBlank(value)) {
            return Boolean.valueOf(value);
        }
        return false;
    }

    public static boolean valueOf(Integer value) {
        return value != null && 0 != value;
    }

    public static boolean valueOf(Boolean value) {
        return value != null && value;
    }
}
