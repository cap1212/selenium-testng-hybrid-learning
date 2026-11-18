package edu.learning.tests;

import edu.learning.framework.pages.LoginPage;
import edu.learning.framework.service.LoginService;
import edu.learning.framework.service.LocalLoginService;
import edu.learning.framework.listeners.RetryOnceAnalyzer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** Login scenarios are workflows; selectors and browser mechanics remain in the page/framework layers. */
public class LoginTest extends BaseTest {
    private LoginPage loginPage() { return new LoginPage(); }

    @Test(priority = 1, groups = {"smoke", "positive"}, description = "Valid user can sign in")
    public void validLoginShowsWelcome() {
        LoginPage page = loginPage(); page.signIn("student", "selenium123");
        Assert.assertEquals(page.message(), "Welcome, student!");
        Assert.assertEquals(page.result(), "success");
    }

    @Test(priority = 2, dependsOnMethods = "validLoginShowsWelcome", groups = {"regression"}, retryAnalyzer = RetryOnceAnalyzer.class, description = "Invalid password is rejected; retry is capped at one")
    public void invalidLoginShowsError() {
        LoginPage page = loginPage(); page.signIn("student", "wrong");
        Assert.assertEquals(page.message(), "Invalid username or password");
        Assert.assertEquals(page.result(), "error");
    }

    /** TestNG invokes each credential row as its own test invocation and its own browser session. */
    @DataProvider(name = "invalidCredentials", parallel = true)
    public Object[][] invalidCredentials() {
        return new Object[][] {{"unknown", "selenium123"}, {"student", ""}, {"", ""}};
    }

    @Test(dataProvider = "invalidCredentials", groups = {"regression", "data-driven"})
    public void invalidCredentialRowsAreRejected(String username, String password) {
        LoginPage page = loginPage(); page.signIn(username, password);
        Assert.assertEquals(page.result(), "error");
    }

    /** A tiny service-level assertion demonstrates programming to an interface (not UI automation). */
    @Test(groups = {"unit"}, enabled = true)
    public void localLoginServiceImplementsContract() {
        LoginService service = new LocalLoginService();
        Assert.assertTrue(service.credentialsAccepted("student", "selenium123"));
        Assert.assertFalse(service.credentialsAccepted("student", "bad"));
    }
}
