package edu.learning.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Page Object encapsulates private locators and UI interactions for login.html. */
public final class LoginPage extends BasePage {
    private final By username = By.id("username"); // Private locator: callers use page actions, not page internals.
    private final By password = By.id("password");
    private final By submit = By.id("login-button");
    private final By message = By.id("message");

    public void signIn(String user, String pass) {
        driver().findElement(username).clear(); driver().findElement(username).sendKeys(user);
        driver().findElement(password).clear(); driver().findElement(password).sendKeys(pass);
        driver().findElement(submit).click();
    }
    public String message() { return waitFor().until(ExpectedConditions.visibilityOfElementLocated(message)).getText(); }
    public String result() { WebElement element = waitFor().until(ExpectedConditions.visibilityOfElementLocated(message)); return element.getAttribute("data-result"); }
}
