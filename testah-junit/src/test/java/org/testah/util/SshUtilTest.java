package org.testah.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testah.util.unittest.dtotest.DtoTest;

import static org.junit.Assert.*;

public class SshUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void runShellEnhanced() {
    }

    @Test
    public void runShell() {
    }

    @Test
    public void cleanOutput() {
    }

    @Test
    public void runShellRtnInfo() {
    }



    @Test
    public void runExec() {

    }

    @Test
    public void testGettersAndSetters() throws Exception {
        DtoTest test = new DtoTest();
        test.addToAnnotationsToIgnore(JsonIgnore.class);
        test.addToIgnoredGetFields("getMergestreams");
        test.testGettersAndSetters(new SshUtil());
    }

}