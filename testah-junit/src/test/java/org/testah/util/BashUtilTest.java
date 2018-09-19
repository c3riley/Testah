package org.testah.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Joiner;
import org.junit.*;
import org.testah.TS;
import org.testah.framework.cli.Params;
import org.testah.framework.report.asserts.AssertFile;
import org.testah.framework.report.asserts.AssertStrings;
import org.testah.util.unittest.dtotest.DtoTest;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class BashUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Ignore
    @Test
    public void executeCommands() throws IOException, InterruptedException {
        BashUtil bashUtil = new BashUtil();
        if(Params.isWindows()) {
            Assume.assumeTrue("Bash will not work on windows", true);
        } else {
            String output = bashUtil.executeCommands("echo TEST2");
            new AssertStrings(output).equalsTo("TEST2");
        }
    }

    @Test
    public void createBashFile() throws IOException {
        runBaseCreateTest("ls -an");
        runBaseCreateTest("ls -an","echo hello", "# this is a test run");
    }

    private void runBaseCreateTest(final String... commands) throws IOException {
        BashUtil bashUtil = new BashUtil();
        File baseFile = bashUtil.createBashFile(commands);
        Assert.assertTrue(baseFile.exists());
        AssertFile assertFile =  new AssertFile(baseFile);

        assertFile.exists().contentEquals(
        "#!/bin/bash" + System.lineSeparator() + "source ~/.bashrc" + System.lineSeparator() + "source ~/profile"
                + System.lineSeparator() + Joiner.on(System.lineSeparator()).join(commands) + System.lineSeparator());
    }


    @Test
    public void testGettersAndSetters() throws Exception {
        DtoTest test = new DtoTest();
        test.addToAnnotationsToIgnore(JsonIgnore.class);
        test.testGettersAndSetters(new BashUtil());
    }
}