package edu.learning.framework.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/** Chrome implementation of the common Browser contract (polymorphism). */
public final class ChromeBrowser implements Browser {
    @Override public WebDriver createDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--window-size=1440,1000", "--disable-dev-shm-usage");
        return new ChromeDriver(options); // Selenium Manager locates/downloads a matching driver when needed.
    }
}
