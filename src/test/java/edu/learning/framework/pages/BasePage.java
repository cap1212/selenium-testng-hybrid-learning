package edu.learning.framework.pages;

import edu.learning.framework.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Shared page behavior. Child page objects inherit the managed driver and explicit wait helper. */
public abstract class BasePage {
    private final WebDriver driver; // Encapsulated driver reference; no external mutation.
    protected BasePage() { this.driver = DriverManager.get(); }
    protected WebDriver driver() { return driver; }
    protected WebDriverWait waitFor() { return new WebDriverWait(driver, Duration.ofSeconds(5)); }
}
