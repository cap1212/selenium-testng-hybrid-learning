package edu.learning.framework.config;

/** Reads explicit JVM settings once per call, with safe local defaults. */
public final class Settings {
    private Settings() { }
    public static String browser() { return System.getProperty("browser", "chrome"); }
    public static boolean headless() { return Boolean.parseBoolean(System.getProperty("headless", "false")); }
}
