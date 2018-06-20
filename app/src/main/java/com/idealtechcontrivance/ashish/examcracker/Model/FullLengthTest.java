package com.idealtechcontrivance.ashish.examcracker.Model;

public class FullLengthTest {
    private int testId, duration;
    private String cnt,testType, testName, testSubject, packageType, testStatus, publishedDate;

    public FullLengthTest() {
    }

    public FullLengthTest(int testId, int duration, String cnt, String testType, String testName, String testSubject, String packageType, String testStatus, String publishedDate) {
        this.testId = testId;
        this.duration = duration;
        this.cnt = cnt;
        this.testType = testType;
        this.testName = testName;
        this.testSubject = testSubject;
        this.packageType = packageType;
        this.testStatus = testStatus;
        this.publishedDate = publishedDate;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestSubject() {
        return testSubject;
    }

    public void setTestSubject(String testSubject) {
        this.testSubject = testSubject;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}
