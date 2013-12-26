package org.jmotor.util.bootstrap;

import org.jmotor.util.CloseableUtilities;
import org.jmotor.util.CollectionUtilities;
import org.jmotor.util.ResourceUtilities;
import org.jmotor.util.StringUtilities;
import org.jmotor.util.converter.SimpleValueConverter;
import org.jmotor.util.exception.ParseSettingsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Component:
 * Description:
 * Date: 13-9-11
 *
 * @author Andy Ai
 */
public class SettingsUtilities {
    private SettingsUtilities() {
    }

    public static Properties loadProperties(String[] args, Class<?> launcherClass) throws IOException {
        String serverConfigPath;
        if (args.length > 0) {
            serverConfigPath = args[0];
        } else {
            String rootPath = getBaseDir(launcherClass);
            serverConfigPath = rootPath + File.separator + "config" + File.separator + "server.properties";
        }
        InputStream inputStream = null;
        try {
            Properties properties = new Properties(System.getProperties());
            inputStream = new FileInputStream(serverConfigPath);
            properties.load(inputStream);
            return properties;
        } finally {
            CloseableUtilities.closeQuietly(inputStream);
        }
    }

    public static String getBaseDir(Class<?> launcherClass) throws IOException {
        return new File(launcherClass.getProtectionDomain().getCodeSource().getLocation().getPath())
                .getParentFile().getParentFile().getCanonicalPath();
    }

    public static Properties getProperties(Properties properties, String namespace) {
        return ResourceUtilities.getProperties(properties, namespace);
    }

    public static Map<String, String> getPropertiesAsMap(Properties properties, String namespace) {
        return ResourceUtilities.getPropertiesAsMap(properties, namespace);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseSettings(Properties properties, Class<?> configClass) {
        T config;
        try {
            config = (T) configClass.newInstance();
        } catch (Exception e) {
            throw new ParseSettingsException(e.getLocalizedMessage(), e);
        }
        return parseSettings(properties, config);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseSettings(Properties properties, T config) {
        Class<T> configClass = (Class<T>) config.getClass();
        Field[] fields = configClass.getDeclaredFields();
        if (CollectionUtilities.isEmpty(fields)) {
            throw new ParseSettingsException(MessageFormat.format("Class [{0}] fields size is 0.", configClass));
        }
        String namespace = "";
        Namespace _namespace = configClass.getAnnotation(Namespace.class);
        if (null != _namespace) {
            namespace = _namespace.value();
            if (!namespace.endsWith(".")) {
                namespace += ".";
            }
        }
        for (Field field : fields) {
            field.setAccessible(true);
            boolean required = field.getAnnotation(Required.class) != null;
            Property property = field.getAnnotation(Property.class);
            String key = field.getName();
            if (null != property) {
                key = property.value();
            }
            key = namespace + key;
            String value = properties.getProperty(key);
            if (required && StringUtilities.isBlank(value)) {
                try {
                    Object defaultValue = field.get(config);
                    if (null == defaultValue) {
                        throw new NullPointerException(MessageFormat.format("Property({0}) can not be empty.", key));
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(MessageFormat.format("Can not read default value of property({0}).", key));
                }
            }
            if (StringUtilities.isNotBlank(value)) {
                try {
                    field.set(config, SimpleValueConverter.convert(field.getType(), value));
                } catch (IllegalAccessException e) {
                    throw new ParseSettingsException(e.getLocalizedMessage(), e);
                }
            }
        }
        return config;
    }

    public static Map<String, Object> getSettingValues(Object config) {
        Class<?> configClass = config.getClass();
        Field[] fields = configClass.getDeclaredFields();
        if (CollectionUtilities.isEmpty(fields)) {
            throw new ParseSettingsException(MessageFormat.format("Class [{0}] fields size is 0.", configClass));
        }
        String namespace = "";
        Namespace _namespace = configClass.getAnnotation(Namespace.class);
        if (null != _namespace) {
            namespace = _namespace.value();
            if (!namespace.endsWith(".")) {
                namespace += ".";
            }
        }
        Map<String, Object> settings = new HashMap<String, Object>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            Property property = field.getAnnotation(Property.class);
            String key = field.getName();
            if (null != property) {
                key = property.value();
            }
            try {
                Object value = field.get(config);
                if (null == value) {
                    continue;
                }
                key = namespace + key;
                settings.put(key, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(MessageFormat.format("Can not read value of property({0}).", key));
            }
        }
        return settings;
    }
}
