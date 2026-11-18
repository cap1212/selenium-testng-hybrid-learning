package edu.learning.framework.listeners;

import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Simple TestNG listener demonstrates suite/test callbacks and result reporting. */
public final class LearningListener implements ITestListener, IExecutionListener {
    @Override public void onExecutionStart() { System.out.println("[listener] TestNG execution started"); }
    @Override public void onExecutionFinish() { System.out.println("[listener] TestNG execution finished"); }
    @Override public void onTestSuccess(ITestResult result) { System.out.println("[PASS] " + result.getName()); }
    @Override public void onTestFailure(ITestResult result) { System.err.println("[FAIL] " + result.getName() + ": " + result.getThrowable()); }
    @Override public void onTestSkipped(ITestResult result) { System.out.println("[SKIP] " + result.getName()); }
}
