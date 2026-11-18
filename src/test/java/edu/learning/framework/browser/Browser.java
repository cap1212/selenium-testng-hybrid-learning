package edu.learning.framework.browser;

import org.openqa.selenium.WebDriver;

/** Abstraction for a browser implementation; tests depend on this contract, not a concrete driver. */
public interface Browser {
    /** Build a fresh, isolated browser session. */
    WebDriver createDriver(boolean headless);
}
