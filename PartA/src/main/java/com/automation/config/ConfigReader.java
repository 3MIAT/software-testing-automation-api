package com.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found on the test classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(value.trim());
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    public static String getBrowser() {
        return get("browser", "chrome");
    }

    public static String getHomeUrl() {
        String configured = get("base.url", get("url", "http://tutorialsninja.com/demo/index.php?route=common/home"));
        if (configured.contains("route=")) {
            return configured;
        }
        return normalizeBaseUrl(configured) + "index.php?route=common/home";
    }

    public static String getRouteUrl(String routeAndParameters) {
        String configured = get("base.url", get("url", "http://tutorialsninja.com/demo/"));
        String base = configured.contains("index.php")
                ? configured.substring(0, configured.indexOf("index.php"))
                : normalizeBaseUrl(configured);
        return base + "index.php?" + routeAndParameters;
    }

    private static String normalizeBaseUrl(String url) {
        return url.endsWith("/") ? url : url + "/";
    }
}
