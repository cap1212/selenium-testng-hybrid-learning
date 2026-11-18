# Selenium + TestNG Hybrid Learning Framework

A small, runnable Maven project for practicing Selenium, TestNG, Java OOP, and maintainable test design. It uses a local static login page, so the page under test does not need a web server or internet connection. Selenium Manager may need internet access the first time it obtains a browser driver.

## Prerequisites

- macOS with JDK 21 or newer. This project compiles to Java 21 bytecode.
- Maven 3.8+ (or IntelliJ IDEA's Maven integration).
- Chrome installed for the default browser. Firefox is also supported.
- Internet access on the first Maven dependency resolution and, if needed, Selenium Manager driver resolution.

The current workspace had Java 27 and did not have a `mvn` command installed. No software was installed. For a standard Mac setup, install a JDK and Maven using your preferred managed method, or open the project in IntelliJ and let its Maven integration import the `pom.xml` and run the lifecycle. IntelliJ's bundled Maven can be selected under **Settings → Build, Execution, Deployment → Build Tools → Maven**.

## Open and run in IntelliJ on Mac

1. Open the folder containing `pom.xml` with **File → Open**.
2. When prompted, choose **Open as Project** and import the Maven project.
3. In **Project Structure → Project**, select JDK 21 for the Project SDK and language level.
4. Allow Maven to resolve dependencies. This downloads libraries; it does not modify the synced `sources/` reference folder.
5. Open `src/test/resources/testng.xml` and run it, or right-click `LoginTest` and run the TestNG class.
6. To run from the IntelliJ Maven tool window, choose **Lifecycle → test**. The Surefire configuration points to `testng.xml`.

The browser session is visible by default. To run headless, set the Maven property `-Dheadless=true`. Browser selection is explicit: `-Dbrowser=chrome` or `-Dbrowser=firefox`.

## Commands

From a terminal in the project folder:

```bash
mvn clean test
mvn test -Dbrowser=chrome -Dheadless=true
mvn test -Dbrowser=firefox -Dheadless=true
```

Run a TestNG group with Surefire's group filter:

```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=regression
```

TestNG results are written beneath `target/surefire-reports/`.

## Project tree

```text
pom.xml
README.md
src/test/java/edu/learning/
  framework/
    browser/       Browser, ChromeBrowser, FirefoxBrowser, BrowserFactory
    config/        Settings
    driver/        DriverManager
    listeners/     LearningListener, RetryOnceAnalyzer
    pages/         BasePage, LoginPage
    service/       LoginService, LocalLoginService
  tests/           BaseTest, LoginTest
src/test/resources/
  login.html       Local login page under test
  testng.xml       Suite, listener, and parallel settings
```

## What the sample covers

### OOP and design

| Concept | Example | What to notice |
|---|---|---|
| Encapsulation | `LoginPage` private `By` fields; `DriverManager` private `ThreadLocal` | Callers use page actions and driver access methods instead of changing internal state. |
| Inheritance | `LoginPage extends BasePage`; `LoginTest extends BaseTest` | Shared page helpers and test lifecycle behavior are reused. |
| Abstraction | `Browser`, `LoginService` interfaces | Callers depend on a small contract. |
| Polymorphism | `ChromeBrowser` and `FirefoxBrowser` implement `Browser` | `BrowserFactory` returns the selected implementation through the common contract. |
| POM | `LoginPage` and `LoginTest` | Locators/actions stay in the page object; test methods describe behavior and assertions. |
| Factory | `BrowserFactory.from(...)` | Browser selection and construction are centralized. |
| SOLID | Browser, driver management, pages, tests, listener, and service have distinct jobs | The code favors focused classes and interface-based dependencies. This is a learning-sized example, not a claim of exhaustive SOLID compliance. |

### TestNG lifecycle and order

The suite uses lifecycle annotations in `BaseTest`. For a normal execution, TestNG runs setup from broadest scope to narrowest scope:

```text
@BeforeSuite → @BeforeTest → @BeforeClass → @BeforeMethod
  test invocation
@AfterMethod → @AfterClass → @AfterTest → @AfterSuite
```

`@BeforeMethod` creates and navigates a fresh browser for every invocation, including each DataProvider row. `@AfterMethod(alwaysRun = true)` quits it and removes the calling thread's `ThreadLocal` value, including after a failed assertion. The listener logs execution and test outcomes. Lifecycle callbacks can run once per configured scope; if you add more `<test>` elements/classes, review the console order to see their scope boundaries.

`priority` demonstrates ordering within a class; `dependsOnMethods` makes the invalid-password example depend on the successful-login example; `groups` labels smoke, regression, positive, data-driven, and unit examples. A dependency failure can cause a dependent method to be skipped.

### DataProvider, listener, and retry

`invalidCredentials` supplies three invalid username/password pairs to one test method. TestNG reports each row as an invocation. `LearningListener` is registered in `testng.xml` and logs starts, passes, failures, skips, and completion. `RetryOnceAnalyzer` is attached to one UI test and permits at most one retry after failure. This is intentionally bounded; use retries sparingly because a retry can hide flaky behavior.

### Parallel TestNG and ThreadLocal

`testng.xml` sets `parallel="methods"` and `thread-count="2"`. TestNG schedules methods on worker threads. `DriverManager` stores each worker's WebDriver in a `ThreadLocal`, so `DriverManager.get()` resolves to the browser created by that same worker. There is no static shared WebDriver. The base setup and cleanup run around each method invocation; DataProvider is also parallel-enabled, so TestNG can schedule its rows concurrently. `ThreadLocal.remove()` is essential because TestNG reuses worker threads: it prevents a later invocation on the same worker from seeing stale driver state.

Each invocation owns a separate browser process. Parallel mode therefore uses more memory and may open several windows. Reduce `thread-count` or run headless on a constrained Mac. When adding shared mutable test data, keep it immutable or synchronize its access; ThreadLocal isolates the driver only.

## Login scenarios

The local page is `src/test/resources/login.html`. Valid credentials are `student` / `selenium123`; success shows a welcome message. Invalid username, password, or blank values show an error. The tests navigate directly to the resource's `file:` URL, so no local web server is required.

## Troubleshooting

- **Maven command not found:** install Maven or use IntelliJ's configured Maven installation.
- **No browser / driver startup failure:** confirm Chrome or Firefox is installed. Selenium 4.6+ includes Selenium Manager, which resolves drivers automatically; the first resolution may require network access.
- **Maven cannot resolve dependencies:** check network/proxy settings and retry Maven import.
- **Parallel run is slow or unstable:** reduce `thread-count` in `testng.xml`; confirm no test stores a driver in a static field.
- **Wrong browser:** pass `-Dbrowser=chrome` or `-Dbrowser=firefox`; unsupported values fail early with an explanatory message.
- **Test reports:** inspect `target/surefire-reports/` for TestNG XML and text reports.

## Practice roadmap

1. Add a `LogoutPage` and test while keeping selectors private.
2. Add a browser setting through a properties file, with system properties taking precedence.
3. Add explicit waits for a delayed page element and compare them with fixed sleeps.
4. Add a new `Browser` implementation or a remote browser implementation without changing test workflows.
5. Add a listener that captures a screenshot on failure, saving it under `target/` with a unique test name.
6. Add valid and invalid datasets, then experiment with `parallel = false` and different suite thread counts.
7. Refactor one class at a time and explain which SOLID responsibility each class owns.

## Version notes

The project pins Selenium Java 4.52.0 and TestNG 7.9.0, and uses Maven Compiler Plugin 3.13.0 and Surefire 3.5.3. Selenium's current Java getting-started/download documentation lists 4.52.0 and notes that Selenium Manager handles drivers from Selenium 4.6 onward. TestNG's download page lists 7.9.0. References: [Selenium downloads](https://www.selenium.dev/downloads/), [Selenium driver setup](https://www.selenium.dev/documentation/webdriver/getting_started/using_selenium/), [TestNG downloads](https://testng.org/download).

## GitHub Actions CI/CD

The workflow at `.github/workflows/ci.yml` automates continuous integration for this framework. It runs on pushes and pull requests targeting `main` or `develop`, and it can also be started manually from the **Actions** tab.

### What the workflow does

- Sets up Temurin Java 21 and caches Maven dependencies.
- Runs `mvn clean test` with Chrome in headless mode.
- Generates an HTML summary from the Surefire XML results.
- Uploads the HTML summary and detailed test reports as an artifact, even when tests fail. Artifacts are retained for 14 days.

The workflow currently lives on `develop`. Merge it into `main` to enable the same workflow for pushes and pull requests on the default branch. This repository is an automation framework, so there is no application deployment job; the pipeline validates the framework and delivers its test reports.

### View GitHub Actions results

1. Open the repository's **Actions** tab and select **Selenium framework CI**.
2. Open a workflow run and review the test step and its summary.
3. Under **Artifacts**, download `test-reports-<run number>` to view the HTML report and detailed Surefire files.

The first successful CI run completed 6 tests with 0 failures: [view run](https://github.com/cap1212/selenium-testng-hybrid-learning/actions/runs/44290213902).

### Generate an HTML report locally

Run the tests and then build the HTML report from the generated Surefire results:

```bash
mvn clean test -Dheadless=true
mvn --batch-mode --no-transfer-progress org.apache.maven.plugins:maven-surefire-report-plugin:3.6.0:report-only
```

Open `target/reports/surefire.html` in a browser.
