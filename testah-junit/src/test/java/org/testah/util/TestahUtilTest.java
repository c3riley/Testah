package org.testah.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.testah.TS;
import org.testah.framework.report.asserts.AssertBigDecimal;
import org.testah.framework.report.asserts.AssertFile;
import org.testah.framework.report.asserts.AssertNumber;
import org.testah.framework.report.asserts.AssertStrings;
import org.testah.util.unittest.dtotest.SystemOutCapture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TestahUtilTest {

    private TestahUtil testahUtil = new TestahUtil();

    @Test
    public void urlEncode() {
        Assert.assertEquals("this+is+a+test", testahUtil.urlEncode("this is a test"));
        Assert.assertEquals("", testahUtil.urlEncode(""));
        Assert.assertEquals("%3Ca+href%3D%22http%3A%2F%2Fwww.goolge.com%22%3Etest%3C%2Fa%3E",
                testahUtil.urlEncode("<a href=\"http://www.goolge.com\">test</a>"));
        Assert.assertEquals("%3Ftest%3D20%25%26test2%3D2.34", testahUtil.urlEncode("?test=20%&test2=2.34"));
    }

    @Test
    public void htmlEncode() {
        Assert.assertEquals("this is a test", testahUtil.htmlEncode("this is a test"));
        Assert.assertEquals("", testahUtil.htmlEncode(""));
        Assert.assertEquals("&lt;a href=&quot;http://www.goolge.com&quot;&gt;test&lt;/a&gt;",
                testahUtil.htmlEncode("<a href=\"http://www.goolge.com\">test</a>"));
    }

    @Test
    public void getRandomInt() {
    }

    @Test
    public void getResourceAsString() {
        Assert.assertEquals("test1.json\n" +
                "test2.txt\n" +
                "test3.log\n", testahUtil.getResourceAsString("/util"));
        Assert.assertNull(testahUtil.getResourceAsString("util"));
        Assert.assertNull(testahUtil.getResourceAsString("util/test1.json"));
        Assert.assertNull(testahUtil.getResourceAsString("util/test5.json"));
        Assert.assertEquals("{\"hello\" : \"world test1\"}", testahUtil.getResourceAsString("/util/test1.json"));

        assertThat(testahUtil.getResourceFolderFiles("/utilNotFound", true),
            is(new ArrayList<File>()));

    }

    @Test
    public void getResourceFolderFile() {
        Assert.assertNull("get base files/folders", testahUtil.getResourceFolderFile("", ""));
        Assert.assertNull("get base files/folders", testahUtil.getResourceFolderFile("util", ""));
        Assert.assertNull("get base files/folders", testahUtil.getResourceFolderFile("/util", ""));
        Assert.assertNull("get base files/folders", testahUtil.getResourceFolderFile("", null));
        Assert.assertNull("get base files/folders", testahUtil.getResourceFolderFile("util", null));
        Assert.assertNull("get base files/folders", testahUtil.getResourceFolderFile("/util", null));
        Assert.assertEquals("get base files/folders", "test2.txt", testahUtil.getResourceFolderFile("util", "test2.txt").getName());
        Assert.assertEquals("get base files/folders", "test2.txt", testahUtil.getResourceFolderFile("/util", "test2.txt").getName());
    }


    @Test
    public void getResourceFolderFiles() {
        Assert.assertEquals("get base files only", 0, testahUtil.getResourceFolderFiles("").size());
        Assert.assertEquals("get base files/folders", 1, testahUtil.getResourceFolderFiles("", false).size());
        Assert.assertEquals("get util", 3, testahUtil.getResourceFolderFiles("util").size());
        Assert.assertEquals("get util", 3, testahUtil.getResourceFolderFiles("util", false).size());
        Assert.assertEquals("get util remove leading slash", 3, testahUtil.getResourceFolderFiles("/util").size());
        Assert.assertEquals("get util remove leading slash", 3, testahUtil.getResourceFolderFiles("/util", false).size());
    }

    @Test
    public void testToJson() {
        new AssertStrings(testahUtil.toJson(new String[]{"test"})).equalsTo("[ \"test\" ]");
        Assert.assertNull(testahUtil.toJson(null));
    }

    @Test
    public void testGetRandomInt() {
        new AssertStrings(testahUtil.toJson(new String[]{"test"})).equalsTo("[ \"test\" ]");
        new AssertBigDecimal(Integer.valueOf(testahUtil.getRandomInt(1, 5))).isWithinRange(1, 5);
        new AssertBigDecimal(Integer.valueOf(testahUtil.getRandomInt(1, 50))).isWithinRange(1, 50);
        new AssertBigDecimal(Integer.valueOf(testahUtil.getRandomInt(1, 500))).isWithinRange(1, 500);
        new AssertBigDecimal(Integer.valueOf(testahUtil.getRandomInt(-121, 5))).isWithinRange(-121, 5);
    }

    @Test
    public void testToDateString() {
        new AssertStrings(testahUtil.toDateString(1537329320L, "MM/dd/yyyy HH:mm:ss.S", "EST"))
                .equalsTo("01/18/1970 14:02:09.320");

        new AssertStrings(testahUtil.toDateString(1537329320L, "MM/dd/yyyy HH:mm:ss.S"))
                .contains("01/18/1970");
    }

    @Test
    public void testGetDurationPretty() {
        new AssertStrings(testahUtil.getDurationPretty(102325456L))
                .equalsTo("28 hours, 25 minutes, 25 seconds and 456 milliseconds");
    }

    @Test
    public void testSplitCamelCase() {
        new AssertStrings(testahUtil.splitCamelCase("thisIsCamelCase")).equalsTo("this Is Camel Case");
    }

    @Test
    public void testGetMap() throws JsonProcessingException {
        TS.asserts().notNull(testahUtil.getMap());
        TS.asserts().equalsTo("[ \"test\" ]", testahUtil.getMap().writeValueAsString(new String[]{"test"}));
    }

    @Test
    public void testToJsonPrint() throws JsonProcessingException {
        String content;
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start()) {

            TS.asserts().equalsTo("[ \"test\" ]", testahUtil.toJsonPrint(new String[]{"test"}));
            content = systemOutCapture.getSystemOut();
        }
        TS.log().info("test");
        new AssertStrings(content)
                .contains("JSON Output for class [Ljava.lang.String;\n[ \"test\" ]");
        TS.log().info(content);
    }

    @Test
    public void testDownload() throws IOException {
        final String uri = "https://github.com/c3riley/maven-repository/raw/master/org/testah/testah-client/0.0.1/testah-client-0.0.1.jar";
        AssertFile file = new AssertFile(testahUtil.downloadFile(uri)).exists();
        file.size().assertThat(greaterThan(0L));
        File dir = testahUtil.unZip(file.getActual(), Files.createTempDirectory("temp").toFile());
        TS.log().debug("zip file: " + dir.getAbsolutePath());

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(dir.listFiles()));
        assertThat(files.size(),equalTo(2));
        assertThat(files.stream().filter(fileInList-> fileInList.getName().equals("META-INF")).findFirst().isPresent(),is(true));
    }


}