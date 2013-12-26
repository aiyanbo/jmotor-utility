package org.jmotor.util;

import org.jmotor.util.dto.ResourceDto;
import org.jmotor.util.type.ResourceType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Component:
 * Description:
 * Date: 12-6-22
 *
 * @author Andy.Ai
 */
public class ResourceUtilities {
    private ResourceUtilities() {
    }

    public static ResourceDto getResource(String path) throws IOException {
        int index = path.indexOf(':');
        if (index < 0) {
            index = path.indexOf('@');
        }
        String type = "";
        String _path = path;
        if (index > 0) {
            type = path.substring(0, index);
            _path = path.substring(index + 1);
        }
        InputStream inputStream;
        ResourceType resourceType;
        if (ResourceType.CLASS_PATH.toString().equalsIgnoreCase(type)) {
            inputStream = StreamUtilities.getStream4ClassPath(_path);
            resourceType = ResourceType.CLASS_PATH;
        } else if (ResourceType.FILE_SYSTEM.toString().equalsIgnoreCase(type)) {
            inputStream = StreamUtilities.getStream4FileSystem(_path);
            resourceType = ResourceType.FILE_SYSTEM;
        } else if (ResourceType.URL.toString().equalsIgnoreCase(type)) {
            inputStream = StreamUtilities.getStream4Url(_path);
            resourceType = ResourceType.URL;
        } else {
            inputStream = StreamUtilities.getStream4ClassPath(_path);
            resourceType = ResourceType.CLASS_PATH;
        }
        ResourceDto resource = new ResourceDto();
        resource.setData(inputStream);
        resource.setPath(_path);
        resource.setType(resourceType.name());
        return resource;
    }

    public static Properties loadProperties(InputStream inputStream) throws IOException {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } finally {
            inputStream.close();
        }
    }

    public static Properties loadProperties(String path) throws IOException {
        ResourceDto resource = getResource(path);
        return loadProperties(resource.getData());
    }

    public static Properties loadProperties(ClassLoader classLoader, String name) throws IOException {
        return loadProperties(name, classLoader, false);
    }

    public static Properties loadProperties(String name, boolean usedFilesystem) throws IOException {
        return loadProperties(name, Thread.currentThread().getContextClassLoader(), usedFilesystem);
    }

    public static Properties loadProperties(String name, ClassLoader classLoader, boolean usedFilesystem) throws IOException {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            if (usedFilesystem) {
                URL resource = classLoader.getResource(name);
                if (resource != null) {
                    inputStream = new FileInputStream(resource.getFile());
                }
            } else {
                inputStream = classLoader.getResourceAsStream(name);
            }
            properties.load(inputStream);
            return properties;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static String getProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        return StringUtilities.trim(value);
    }

    /**
     * Get properties by namespace as a map
     * <code>
     * props.setProperty("jdbc.username");
     * props.setProperty("jdbc.password");
     * props.setProperty("http.username");
     * props.setProperty("http.password");
     * <p/>
     * jdbcProperties=ResourcesUtilities.getProperties(props,"jdbc");
     * <p/>
     * jdbcProperties has to key-value pairs:
     * 1. jdbc.username
     * 2. jdbc.password
     * <code/>
     *
     * @param properties {@link java.util.Properties}
     * @param namespace  namespace
     * @return {@link java.util.Map}
     */
    public static Map<String, String> getPropertiesAsMap(Properties properties, String namespace) {
        Map<String, String> pairs = new HashMap<String, String>(properties.size());
        int index = namespace.length() + 1;
        for (String property : properties.stringPropertyNames()) {
            if (property.startsWith(namespace)) {
                pairs.put(property.substring(index), properties.getProperty(property));
            }
        }
        return pairs;
    }

    /**
     * Get properties by namespace
     * <code>
     * props.setProperty("jdbc.username");
     * props.setProperty("jdbc.password");
     * props.setProperty("http.username");
     * props.setProperty("http.password");
     * <p/>
     * jdbcProperties=ResourcesUtilities.getProperties(props,"jdbc");
     * <p/>
     * jdbcProperties has to key-value pairs:
     * 1. jdbc.username
     * 2. jdbc.password
     * <code/>
     *
     * @param properties {@link java.util.Properties}
     * @param namespace  namespace
     * @return {@link java.util.Map}
     */
    public static Properties getProperties(Properties properties, String namespace) {
        Properties props = new Properties();
        int index = namespace.length() + 1;
        for (String property : properties.stringPropertyNames()) {
            if (property.startsWith(namespace)) {
                props.setProperty(property.substring(index), properties.getProperty(property));
            }
        }
        return props;
    }
}
