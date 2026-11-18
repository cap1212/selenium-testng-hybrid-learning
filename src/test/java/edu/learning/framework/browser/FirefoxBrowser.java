package edu.learning.framework.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Firefox implementation of Browser; select with -Dbrowser=firefox. */
public final class FirefoxBrowser implements Browser {
    @Override public WebDriver createDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("-headless");
        return new FirefoxDriver(options);
    }
}
