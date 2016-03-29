package org.testah.client.dto;

import java.util.HashMap;

public class RunInfoDto {

    private String                  runId             = null;
    private String                  runType           = null;
    private String                  versionId         = null;
    private String                  runLocation       = null;
    private String                  buildNumber       = null;
    private int                     pass              = 0;
    private int                     fail              = 0;
    private int                     ignore            = 0;
    private int                     total             = 0;
    private HashMap<String, String> runTimeProperties = new HashMap<String, String>();

    public RunInfoDto() {

    }

    public RunInfoDto recalc(final TestPlanDto testPlans) {
        total = testPlans.getTestCases().size();
        pass = 0;
        fail = 0;
        ignore = 0;

        for (final TestCaseDto testCase : testPlans.getTestCases()) {
            if (null == testCase.getStatus()) {
                ignore++;
            } else if (testCase.getStatus()) {
                pass++;
            } else {
                fail++;
            }
        }
        return this;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(final int pass) {
        this.pass = pass;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(final int fail) {
        this.fail = fail;
    }

    public int getIgnore() {
        return ignore;
    }

    public void setIgnore(final int ignore) {
        this.ignore = ignore;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(final String runId) {
        this.runId = runId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    public String getRunLocation() {
        return runLocation;
    }

    public void setRunLocation(final String runLocation) {
        this.runLocation = runLocation;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(final String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(final String runType) {
        this.runType = runType;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(final String versionId) {
        this.versionId = versionId;
    }

    public HashMap<String, String> getRunTimeProperties() {
        return runTimeProperties;
    }

    public void setRunTimeProperties(final HashMap<String, String> runTimeProperties) {
        this.runTimeProperties = runTimeProperties;
    }

}
