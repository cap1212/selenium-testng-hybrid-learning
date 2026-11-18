package edu.learning.framework.driver;

import edu.learning.framework.browser.BrowserFactory;
import edu.learning.framework.config.Settings;
import org.openqa.selenium.WebDriver;

/** Owns per-thread driver state; never exposes a static shared WebDriver instance. */
public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private DriverManager() { }

    /** Create a driver for the calling TestNG worker thread. */
    public static void start() { DRIVER.set(BrowserFactory.from(Settings.browser()).createDriver(Settings.headless())); }
    /** Fail early with a useful message if a page is used outside its test lifecycle. */
    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("No WebDriver is assigned to this test thread");
        return driver;
    }
    /** Quit and remove the ThreadLocal value even if quit throws. */
    public static void stop() {
        WebDriver driver = DRIVER.get();
        try { if (driver != null) driver.quit(); }
        finally { DRIVER.remove(); }
    }
}
