package edu.learning.tests;

import edu.learning.framework.driver.DriverManager;
import org.testng.annotations.*;
import java.net.URL;

/** Test lifecycle base: inherited TestNG hooks start and clean up each method's own browser. */
public abstract class BaseTest {
    @BeforeSuite(alwaysRun = true) public void beforeSuite() { System.out.println("[lifecycle] @BeforeSuite"); }
    @BeforeTest(alwaysRun = true) public void beforeTest() { System.out.println("[lifecycle] @BeforeTest"); }
    @BeforeClass(alwaysRun = true) public void beforeClass() { System.out.println("[lifecycle] @BeforeClass: " + getClass().getSimpleName()); }
    @BeforeMethod(alwaysRun = true) public void beforeMethod() {
        System.out.println("[lifecycle] @BeforeMethod on " + Thread.currentThread().getName());
        DriverManager.start();
        URL page = getClass().getClassLoader().getResource("login.html");
        if (page == null) throw new IllegalStateException("login.html is missing from test resources");
        DriverManager.get().get(page.toExternalForm()); // file: URL keeps the demo independent of a web server/network.
    }
    @AfterMethod(alwaysRun = true) public void afterMethod() {
        try { DriverManager.stop(); }
        finally { System.out.println("[lifecycle] @AfterMethod cleanup on " + Thread.currentThread().getName()); }
    }
    @AfterClass(alwaysRun = true) public void afterClass() { System.out.println("[lifecycle] @AfterClass: " + getClass().getSimpleName()); }
    @AfterTest(alwaysRun = true) public void afterTest() { System.out.println("[lifecycle] @AfterTest"); }
    @AfterSuite(alwaysRun = true) public void afterSuite() { System.out.println("[lifecycle] @AfterSuite"); }
}
