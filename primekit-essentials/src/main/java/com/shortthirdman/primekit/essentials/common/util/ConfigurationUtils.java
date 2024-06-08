package com.shortthirdman.primekit.essentials.common.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class ConfigurationUtils {

    private ConfigurationUtils() {
    }

    private static final Map<String, Properties> propertiesConfigMap = new LinkedHashMap<>();

    /**
     * Loads all the configuration properties from the file name
     * @param propertiesKey the file name key of properties configuration
     * @return the properties list in <K, V> pair
     */
    public static Properties loadConfig(String propertiesKey) {
        Properties config = null;
        String fileName = propertiesKey;
        if (!propertiesKey.endsWith(".properties")) {
            fileName = propertiesKey + ".properties";
        }
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (Objects.isNull(input)) {
                throw new IllegalStateException(propertiesKey + ".properties load failed, reason: ");
            }
            try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                config = new Properties();
                config.load(reader);
                propertiesConfigMap.put(propertiesKey, config);
            } catch (Exception e) {
                throw new IllegalStateException(propertiesKey + ".properties error: " + e.getMessage());
            }
        } catch (Exception e) {
//            Logger.getGlobal().log(Level.WARNING, propertiesKey + ".properties load failed, reason:", e);
            return config;
        }
        return config;
    }

    /**
     * @param fileKey the file name key of properties configuration
     * @param key the key name
     * @return the value of the key in the configuration file
     */
    public static String getProperty(String fileKey, String key) {
        Properties thisConfig = propertiesConfigMap.get(fileKey);
        if (null == thisConfig) {
            thisConfig = loadConfig(fileKey);
        }

        if (null == thisConfig) {
            return null;
        } else {
            return thisConfig.getProperty(key);
        }
    }

    /**
     * @param fileKey the file name key of properties configuration
     * @param key the key name
     * @param defaultValue the default value if no key found
     * @return the value of the key in the configuration file
     */
    public static String getProperty(String fileKey, String key, String defaultValue) {
        String result = getProperty(fileKey, key);
        if (null == result) {
            return defaultValue;
        } else {
            return result;
        }
    }

    /**
     * @param fileKey the file name key of properties configuration
     * @return the properties list in <K, V> pair
     */
    public static Properties getProperty(String fileKey) {
        Properties thisConfig = propertiesConfigMap.get(fileKey);
        if (null == thisConfig) {
            thisConfig = loadConfig(fileKey);
        }
        return thisConfig;
    }
}
