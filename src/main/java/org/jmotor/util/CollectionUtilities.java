package org.jmotor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Component:Utility
 * Description:Collection utilities
 * Date: 11-8-16
 *
 * @author Andy.Ai
 */
public class CollectionUtilities {
    private CollectionUtilities() {
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    public static Object[] removeElement(Object[] array, Object element) {
        Object[] newArray = new Object[array.length - 1];
        int index = 0;
        for (Object entry : array) {
            if (!entry.equals(element)) {
                newArray[index++] = entry;
            }
        }
        return newArray;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean contains(Collection<?> collection, String propertyName, Object value) {
        return find(collection, propertyName, value) != null;
    }

    public static boolean contains(Object[] array, Object object) {
        for (Object entry : array) {
            if (entry.equals(object)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static Object find(Object[] array, Validator validator) {
        for (Object entry : array) {
            if (validator.validate(entry)) {
                return entry;
            }
        }
        return null;
    }

    public static void forAllExecute(Collection collection, Executor executor) {
        for (Object entry : collection) {
            executor.execute(entry);
        }
    }

    public static <T> T find(Collection<T> collection, String propertyName, Object value) {
        for (T object : collection) {
            Object existsValue = ObjectUtilities.getPropertyValue(object, propertyName);
            if (existsValue.equals(value)) {
                return object;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T find(Collection collection, Validator validator) {
        for (Object entry : collection) {
            if (validator.validate(entry)) {
                return (T) entry;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void filter(Collection collection, Validator validator) {
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            if (!validator.validate(iterator.next())) {
                iterator.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static List select(Collection collection, Validator validator) {
        List result = new ArrayList(collection.size());
        select(collection, result, validator);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void select(Collection data, Collection container, Validator validator) {
        for (Object entry : data) {
            if (validator.validate(entry)) {
                container.add(entry);
            }
        }
    }

    public static Map<Object, List<Object>> groupBy(Collection collection, Keyed keyed) {
        Map<Object, List<Object>> result = new HashMap<>();
        for (Object entry : collection) {
            Object key = keyed.key(entry);
            List<Object> values = result.get(key);
            if (null == values) {
                values = new ArrayList<>();
                result.put(key, values);
            }
            values.add(entry);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List map(Collection collection, Mapped mapped) {
        List result = new ArrayList(collection.size());
        for (Object entry : collection) {
            result.add(mapped.map(entry));
        }
        return result;
    }

    public static Object reduce(Collection collection, Reduced reduced) {
        Object reduce = null;
        boolean init = false;
        for (Object entry : collection) {
            if (!init) {
                reduce = entry;
                init = true;
                continue;
            }
            reduce = reduced.reduce(reduce, entry);
        }
        return reduce;
    }
}                                                             
