package edu.learning.framework.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** Retries a failed test at most once to avoid masking persistent failures or looping forever. */
public final class RetryOnceAnalyzer implements IRetryAnalyzer {
    private int retries;
    @Override public boolean retry(ITestResult result) { return retries++ < 1; }
}
