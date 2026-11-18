package edu.learning.framework.browser;

import java.util.Locale;

/** Central creation point keeps browser choice out of tests and page objects. */
public final class BrowserFactory {
    private BrowserFactory() { }
    public static Browser from(String name) {
        return switch (name == null ? "chrome" : name.toLowerCase(Locale.ROOT)) {
            case "chrome" -> new ChromeBrowser();
            case "firefox" -> new FirefoxBrowser();
            default -> throw new IllegalArgumentException("Unsupported browser '" + name + "'. Choose chrome or firefox.");
        };
    }
}
