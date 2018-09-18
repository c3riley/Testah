package org.testah;

import com.fasterxml.jackson.annotation.JsonIgnore;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.client.dto.*;
import org.testah.framework.cli.Params;
import org.testah.framework.dto.Result;
import org.testah.framework.dto.ResultDto;
import org.testah.framework.dto.Step;
import org.testah.framework.report.asserts.base.AssertHistoryItem;
import org.testah.runner.performance.dto.LoadTestSequenceDto;
import org.testah.util.database.dto.SqlExecutionDto;
import org.testah.util.dto.ShellInfoDto;
import org.testah.util.mail.EmailDto;
import org.testah.util.mail.SendMailDto;

import javax.mail.Message;
import java.util.Date;

/**
 * The type Test project dtos.
 */
public class TestProjectDtos {

    private org.testah.util.unittest.dtotest.DtoTest test;

    /**
     * Set up for testing.
     */
    @Before
    public void setup() {
        test = new org.testah.util.unittest.dtotest.DtoTest();
        test.addToAnnotationsToIgnore(JsonIgnore.class);
    }

    /**
     * Test step action dto.
     *
     * @throws Exception the exception
     */
    @Test
    public void testStepActionDto() throws Exception {
        StepActionDto dto = new StepActionDto();
        test.addToIgnoredGetFields("getExceptionString");
        test.testGettersAndSetters(dto);
    }

    @Test
    public void testParams() throws Exception {
        Params dto = new Params();
        test.addToIgnoredGetFields("getComputerName");
        test.addToIgnoredGetFields("getLevel");
        test.addToIgnoredGetFields("isMac");
        test.addToIgnoredGetFields("isUnix");
        test.addToIgnoredGetFields("isWindows");
        test.addToIgnoredGetFields("getOutput");
        test.testGettersAndSetters(dto);
    }

    /**
     * Test run time dto.
     *
     * @throws Exception the exception
     */
    @Test
    public void testRunTimeDto() throws Exception {
        RunTimeDto dto = new RunTimeDto();
        Date start = new Date();
        dto.start();
        TS.util().pause(1000L);
        Date end = new Date();
        dto.stop(end.getTime());

        Assert.assertEquals("getStartDate", dto.formatDate(start), dto.getStartDate());
        Assert.assertEquals("getEndDate", dto.formatDate(end), dto.getEndDate());

        Assert.assertEquals("getStartTime", (Long) start.getTime(), dto.getStartTime());
        Assert.assertEquals("getEndTime", (Long) end.getTime(), dto.getEndTime());

        test.addToIgnoredGetFields("getEndDate");
        test.addToIgnoredGetFields("getStartDate");
        test.testGettersAndSetters(new RunTimeDto());

    }


    /**
     * Test many dtos in the project.
     *
     * @throws Exception the exception
     */
    @Test()
    public void test() throws Exception {
        test.testGettersAndSetters(new EmailDto<Message>());
        test.testGettersAndSetters(new EmailDto<EmailMessage>());

        test.testGettersAndSetters(new SendMailDto("test@testah.com"));

        test.testGettersAndSetters(new KnownProblemDto());
        test.testGettersAndSetters(new KnownProblemDto());
        test.testGettersAndSetters(new KnownProblemDto());
        test.testGettersAndSetters(new RunInfoDto());

        test.addToIgnoredGetFields("getEndDate");
        test.addToIgnoredGetFields("getStartDate");

        test.testGettersAndSetters(new TestCaseDto());
        test.testGettersAndSetters(new TestPlanDto());
        test.testGettersAndSetters(new TestStepDto());
        test.testGettersAndSetters(new LoadTestSequenceDto());
        test.testGettersAndSetters(new SqlExecutionDto("SELECT 1"));
        test.testGettersAndSetters(new ShellInfoDto());
        test.testGettersAndSetters(new AssertHistoryItem());

        test.testGettersAndSetters(new ResultDto());
        test.testGettersAndSetters(new Result());
        test.testGettersAndSetters(new Step());


    }

}