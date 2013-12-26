package org.jmotor.util;

import org.jmotor.util.exception.PropertyException;
import org.jmotor.util.type.MethodType;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Component:Utility
 * Description:Object utilities
 * Date: 11-8-16
 *
 * @author Andy.Ai
 */
public class ObjectUtilities {
    private ObjectUtilities() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object[] parameters)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Constructor constructor = clazz.getConstructor(parameterTypes);
        return (T) constructor.newInstance(parameters);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Object object) {
        try {
            return ClassUtilities.getPropertyDescriptors(getClass(object));
        } catch (IntrospectionException e) {
            throw new PropertyException(e);
        }
    }

    public static Object getPropertyValue(Object object, String propertyName) {
        Object result;
        try {
            result = ClassUtilities.getAccessible(getClass(object), propertyName, MethodType.GETTER).invoke(object);
        } catch (Exception e) {
            throw new PropertyException(e);
        }
        return result;
    }

    public static void setPropertyValue(Object object, String propertyName, Object value) {
        try {
            ClassUtilities.getAccessible(getClass(object), propertyName, MethodType.SETTER).invoke(object, value);
        } catch (Exception e) {
            throw new PropertyException(e);
        }
    }

    public static Class<?> getPropertyType(Object object, String propertyName) {
        try {
            return ClassUtilities.getPropertyType(getClass(object), propertyName);
        } catch (NoSuchFieldException e) {
            throw new PropertyException(e);
        }
    }

    public static void cloneProperties(Object sourceObject, Object destinationObject) {
        cloneProperties(sourceObject, destinationObject, true);
    }

    public static void cloneProperties(Object sourceObject, Object destinationObject, boolean excludeNullValue) {
        PropertyDescriptor[] sourcePropertyDescriptors = getPropertyDescriptors(sourceObject);
        PropertyDescriptor[] targetPropertyDescriptors = getPropertyDescriptors(destinationObject);
        for (PropertyDescriptor sourcePropertyDescriptor : sourcePropertyDescriptors) {
            for (PropertyDescriptor targetPropertyDescriptor : targetPropertyDescriptors) {
                if (sourcePropertyDescriptor.getName().equals(targetPropertyDescriptor.getName())
                        && sourcePropertyDescriptor.getPropertyType().equals(targetPropertyDescriptor.getPropertyType())) {
                    Object value = getPropertyValue(sourceObject, sourcePropertyDescriptor.getName());
                    if (excludeNullValue && value == null) {
                        continue;
                    }
                    setPropertyValue(destinationObject, targetPropertyDescriptor.getName(), value);
                }
            }
        }
    }

    public static Object invoke(Object object, String methodName, Object... parameters) {
        Class<?>[] parametersType = null;
        if (CollectionUtilities.isNotEmpty(parameters)) {
            parametersType = new Class<?>[parameters.length];
            int index = 0;
            for (Object parameter : parameters) {
                parametersType[index++] = getClass(parameter);
            }
        }
        try {
            if (CollectionUtilities.isNotEmpty(parametersType)) {
                Method invoker = ClassUtilities.getMethod(getClass(object), methodName, parametersType);
                return invoker.invoke(object, parameters);
            } else {
                Method invoker = ClassUtilities.getMethod(getClass(object), methodName);
                return invoker.invoke(object);
            }
        } catch (Exception e) {
            throw new PropertyException(e);
        }
    }

    public static String writeObject(Serializable serializable) {
        return StreamUtilities.writeObject(serializable);
    }

    public static void writeObject(Serializable serializable, String fileName) throws IOException {
        StreamUtilities.writeObject(serializable, fileName);
    }

    public static <T> T readObject(String fileName) throws ClassNotFoundException, IOException {
        return StreamUtilities.readObject(fileName);
    }

    public static boolean equals(Object o1, Object o2) {
        return o1 == null && o2 == null || replaceNull(o1).equals(o2);
    }

    public static boolean isDifferent(Object o1, Object o2) {
        return !equals(o1, o2);
    }

    public static Object replaceNull(Object object, String replacement) {
        if (object == null) {
            return replacement;
        }
        return object;
    }

    public static Object replaceNull(Object object) {
        return replaceNull(object, "");
    }

    public static String toString(Object object) {
        return object == null ? "" : object.toString();
    }

    public static Class<?> getClass(Object object) {
        return ClassUtilities.getCanonicalClass(object.getClass());
    }
}
