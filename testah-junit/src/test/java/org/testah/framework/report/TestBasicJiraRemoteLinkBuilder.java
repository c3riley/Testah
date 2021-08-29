package org.testah.framework.report;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.KnownProblemDto;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.client.enums.TypeOfKnown;
import org.testah.framework.report.jira.BasicJiraRemoteLinkBuilder;
import org.testah.framework.report.jira.dto.RemoteIssueLinkDto;

import java.util.ArrayList;
import java.util.List;

import static org.testah.client.dto.TestCaseDto.TEST_CASE_IGNORED_KNOWN_PROBLEM;
import static org.testah.client.dto.TestPlanDto.TEST_PLAN_IGNORED_KNOWN_PROBLEM;

public class TestBasicJiraRemoteLinkBuilder {

    private BasicJiraRemoteLinkBuilder builder;
    private TestPlanDto basicTp;
    private KnownProblemDto knownProblem;

    /**
     * Sets .
     */
    @Before
    public void setup() {
        builder = new BasicJiraRemoteLinkBuilder("test.ico");
        knownProblem = new KnownProblemDto().setLinkedIds(new ArrayList<String>());
        knownProblem.getLinkedIds().add("TEST-01");
        knownProblem.setDescription("This is a known Problem").setTypeOfKnown(TypeOfKnown.DEFECT);

        List<TestCaseDto> testcases = new ArrayList<>();
        testcases.add(new TestCaseDto().setName("Basic Testcase").setDescription("This is a basic testcase"));
        testcases.add(new TestCaseDto().setName("Basic Testcase").setDescription("This is a basic testcase").setKnownProblem(knownProblem));

        basicTp = new TestPlanDto().setName("Basic Testplan").setDescription("This is a basic testplan").setTestCases(testcases)
                .setSource("com.test.BasicTestPlan");
    }

    @Test
    public void test1IconConstructor() {
        final String icon1 = "test1.ico";
        BasicJiraRemoteLinkBuilder builderIcon = new BasicJiraRemoteLinkBuilder(icon1);
        Assert.assertEquals(icon1, builderIcon.getRemoteLinkForTestPlanResult(basicTp).getObject().getIcon().getUrl16x16());
        Assert.assertEquals(icon1, builderIcon.getRemoteLinkForTestPlanResult(basicTp).getObject().getIcon().getUrl16x16());
    }

    @Test
    public void test2IconConstructor() {
        final String icon1 = "test1.ico";
        final String icon2 = "test2.ico";
        BasicJiraRemoteLinkBuilder builderIcon = new BasicJiraRemoteLinkBuilder(icon1, icon2);

        Assert.assertEquals(icon1, builderIcon.getRemoteLinkForTestPlanResult(basicTp).getObject().getIcon().getUrl16x16());
        Assert.assertEquals(icon2, builderIcon.getRemoteLinkForTestPlanResult(basicTp).getObject().getStatus().getIcon().getUrl16x16());
    }

    @Test
    public void testGetRemoteLinkForTestPlanResult() {
        final RemoteIssueLinkDto remoteLink = builder.getRemoteLinkForTestPlanResult(basicTp);
        Assert.assertNotNull(remoteLink);
        Assert.assertEquals(0, remoteLink.getId());
        Assert.assertEquals("E2E Testplan-com.test.BasicTestPlan", remoteLink.getGlobalId());
        Assert.assertEquals("E2E Testplan", remoteLink.getRelationship());
        Assert.assertEquals("This is a basic testplan - status: NA - duration: 0", remoteLink.getObject().getSummary());
        Assert.assertEquals("Basic Testplan", remoteLink.getObject().getTitle());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=Use-Envir-Param=param_runLocation",
                remoteLink.getObject().getUrl());
        Assert.assertEquals("test.ico", remoteLink.getObject().getIcon().getUrl16x16());
        Assert.assertEquals("Basic Testplan", remoteLink.getObject().getIcon().getTitle());
        Assert.assertEquals(false, remoteLink.getObject().getStatus().getResolved());
        Assert.assertEquals("test.ico", remoteLink.getObject().getStatus().getIcon().getUrl16x16());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=IssueGetting_param_sourceUrl/com/test/BasicTestPlan.java",
                remoteLink.getObject().getStatus().getIcon().getLink());
        Assert.assertEquals("Basic Testplan",
                remoteLink.getObject().getStatus().getIcon().getTitle());
        Assert.assertEquals("Basic Testplan", remoteLink.getApplication().getName());
        Assert.assertEquals("E2E Testplan", remoteLink.getApplication().getType());
    }

    @Test
    public void testGetRemoteLinkForTestPlanResultKnownProblem() {
        final RemoteIssueLinkDto remoteLink = builder.getRemoteLinkForTestPlanResultKnownProblem(basicTp.setKnownProblem(knownProblem));
        Assert.assertNotNull(remoteLink);
        Assert.assertEquals(0, remoteLink.getId());
        Assert.assertEquals("E2E KP Testplan-com.test.BasicTestPlan", remoteLink.getGlobalId());
        Assert.assertEquals("E2E KP Testplan", remoteLink.getRelationship());
        TS.asserts().startsWith("", remoteLink.getObject().getSummary(), TEST_PLAN_IGNORED_KNOWN_PROBLEM);
        Assert.assertEquals("Basic Testplan", remoteLink.getObject().getTitle());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=Use-Envir-Param=param_runLocation",
                remoteLink.getObject().getUrl());
        Assert.assertEquals("test.ico", remoteLink.getObject().getIcon().getUrl16x16());
        Assert.assertEquals("This is a known Problem", remoteLink.getObject().getIcon().getTitle());
        Assert.assertEquals(false, remoteLink.getObject().getStatus().getResolved());
        Assert.assertEquals("test.ico", remoteLink.getObject().getStatus().getIcon().getUrl16x16());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=IssueGetting_param_sourceUrl/com/test/BasicTestPlan.java",
                remoteLink.getObject().getStatus().getIcon().getLink());
        Assert.assertEquals("This is a known Problem", remoteLink.getObject().getStatus().getIcon().getTitle());
        Assert.assertEquals("DEFECT-Basic Testplan", remoteLink.getApplication().getName());
        Assert.assertEquals("E2E KP Testplan", remoteLink.getApplication().getType());
    }

    @Test
    public void testGetRemoteLinkForTestCaseResult() {
        builder.setLastTestPlanDtoUsed(basicTp);
        final RemoteIssueLinkDto remoteLink = builder.getRemoteLinkForTestCaseResult(basicTp.getTestCases().get(0));
        Assert.assertNotNull(remoteLink);
        Assert.assertEquals(0, remoteLink.getId());
        Assert.assertEquals("E2E Testcase-com.test.BasicTestPlan", remoteLink.getGlobalId());
        Assert.assertEquals("E2E Testcase", remoteLink.getRelationship());
        Assert.assertEquals("This is a basic testcase - status: NA - duration: 0", remoteLink.getObject().getSummary());
        Assert.assertEquals("Basic Testcase", remoteLink.getObject().getTitle());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=Use-Envir-Param=param_runLocation",
                remoteLink.getObject().getUrl());
        Assert.assertEquals("test.ico", remoteLink.getObject().getIcon().getUrl16x16());
        Assert.assertEquals("Basic Testcase", remoteLink.getObject().getIcon().getTitle());
        Assert.assertEquals(false, remoteLink.getObject().getStatus().getResolved());
        Assert.assertEquals("test.ico", remoteLink.getObject().getStatus().getIcon().getUrl16x16());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=IssueGetting_param_sourceUrl/com/test/BasicTestPlan.java",
                remoteLink.getObject().getStatus().getIcon().getLink());
        Assert.assertEquals("Basic Testcase",
                remoteLink.getObject().getStatus().getIcon().getTitle());
        Assert.assertEquals("Basic Testcase", remoteLink.getApplication().getName());
        Assert.assertEquals("E2E Testcase", remoteLink.getApplication().getType());
    }

    @Test
    public void testGetRemoteLinkForTestCaseResultKnownProblem() {
        builder.setLastTestPlanDtoUsed(basicTp);
        final RemoteIssueLinkDto remoteLink = builder.getRemoteLinkForTestCaseResultKnownProblem(basicTp.getTestCases().get(1));
        Assert.assertNotNull(remoteLink);
        Assert.assertEquals(0, remoteLink.getId());
        Assert.assertEquals("E2E KP Testcase-com.test.BasicTestPlan", remoteLink.getGlobalId());
        Assert.assertEquals("E2E KP Testcase", remoteLink.getRelationship());
        TS.asserts().startsWith("", remoteLink.getObject().getSummary(), TEST_CASE_IGNORED_KNOWN_PROBLEM);
        Assert.assertEquals("Basic Testcase", remoteLink.getObject().getTitle());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=Use-Envir-Param=param_runLocation",
                remoteLink.getObject().getUrl());
        Assert.assertEquals("test.ico", remoteLink.getObject().getIcon().getUrl16x16());
        Assert.assertEquals("This is a known Problem", remoteLink.getObject().getIcon().getTitle());
        Assert.assertEquals(false, remoteLink.getObject().getStatus().getResolved());
        Assert.assertEquals("test.ico", remoteLink.getObject().getStatus().getIcon().getUrl16x16());
        Assert.assertEquals(
                "http://noLinkFoundToUsePlease.com/?errorTip=IssueGetting_param_sourceUrl/com/test/BasicTestPlan.java",
                remoteLink.getObject().getStatus().getIcon().getLink());
        Assert.assertEquals("This is a known Problem", remoteLink.getObject().getStatus().getIcon().getTitle());
        Assert.assertEquals("DEFECT-Basic Testcase", remoteLink.getApplication().getName());
        Assert.assertEquals("E2E KP Testcase", remoteLink.getApplication().getType());
    }

}
