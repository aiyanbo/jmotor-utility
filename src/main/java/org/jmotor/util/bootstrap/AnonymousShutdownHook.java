package org.jmotor.util.bootstrap;

import org.jmotor.util.ClassUtilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Component:
 * Description:
 * Date: 15/7/6
 *
 * @author Andy.Ai
 */
public class AnonymousShutdownHook {
    /**
     * De-registers a previously-registered virtual-machine shutdown hook by name.
     *
     * @param name {@link Thread#name}
     * @see Runtime#addShutdownHook(Thread)
     */
    public static void removeShutdownHook(String name) {
        IdentityHashMap<Thread, Thread> hooks = getShutdownHooks();
        if (null == hooks || hooks.isEmpty()) {
            return;
        }
        List<Thread> removes = new ArrayList<>();
        for (Map.Entry<Thread, Thread> thread : hooks.entrySet()) {
            if (thread.getKey().getName().equals(name)) {
                removes.add(thread.getValue());
            }
        }
        for (Thread hook : removes) {
            Runtime.getRuntime().removeShutdownHook(hook);
        }
    }

    /**
     * De-registers a previously-registered virtual-machine shutdown hook by class.
     *
     * @param clazz anonymous class
     * @see Runtime#addShutdownHook(Thread)
     */
    public static void removeShutdownHook(Class clazz) {
        IdentityHashMap<Thread, Thread> hooks = getShutdownHooks();
        if (null == hooks || hooks.isEmpty()) {
            return;
        }
        List<Thread> removes = new ArrayList<>();
        for (Map.Entry<Thread, Thread> thread : hooks.entrySet()) {
            if (clazz.getName().contains(thread.getKey().getClass().getName())) {
                removes.add(thread.getValue());
            }
        }
        for (Thread hook : removes) {
            Runtime.getRuntime().removeShutdownHook(hook);
        }
    }

    @SuppressWarnings("unchecked")
    private static IdentityHashMap<Thread, Thread> getShutdownHooks() {
        try {
            Class clazz = Class.forName("java.lang.ApplicationShutdownHooks");
            Field hooksFiled = ClassUtilities.getField(clazz, "hooks");
            hooksFiled.setAccessible(true);
            return (IdentityHashMap<Thread, Thread>) hooksFiled.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            //ignore
        }
        return null;
    }
}
