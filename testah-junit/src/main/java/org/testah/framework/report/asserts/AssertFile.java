package org.testah.framework.report.asserts;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class AssertFile extends AbstractAssertBase<AssertFile, File> {

    private Charset defaultCharset = Charset.forName("UTF-8");

    public AssertFile(File actual) {
        super(actual);
    }

    public AssertFile(File actual, VerboseAsserts asserts) {
        super(actual, asserts);
    }

    public AssertFile exists() {
        return runAssert("Check that file[" + getAbsolutePath() + "] exists", "exists",
                () -> {
                    Assert.assertTrue(getActual().exists());
                }, true, getActual().exists());
    }

    public AssertFile notExists() {
        return runAssert("Check that file[" + getAbsolutePath() + "] exists", "exists",
                () -> {
                    Assert.assertFalse(getActual().exists());
                }, false, getActual().exists());
    }

    public AssertNumber size() {
        if (canAssertRun("fileSize", true)) {
            return new AssertNumber<Long>(getActual().length(), getAsserts());
        }
        return new AssertNumber<Integer>(null,getAsserts());
    }

    public AssertFile contentEquals(final File expected) {
        return contentEquals(expected, Charset.forName("UTF-8"));
    }

    public AssertFile contentEquals(final File expected, Charset charset) {
        return contentEquals(getContentAsString(expected, charset));
    }

    public AssertFile contentEquals(final String expected) {
        return contentEquals(expected, Charset.forName("UTF-8"));
    }

    public AssertFile contentEquals(final String expected, Charset charset) {
        return this.addStatus(new AssertStrings(getContentAsString(charset)).equalsTo(expected).isPassed());
    }

    public String getContentAsString() {
        return getContentAsString(defaultCharset);
    }

    public String getContentAsString(Charset charset) {
        return getContentAsString(getActual(), charset);
    }

    public String getContentAsString(final File file, Charset charset) {
        try {
            return FileUtils.readFileToString(getActual(), charset);
        } catch (IOException e) {
            getAsserts().fail("Issue getting the String Content of the file[" + file.getAbsolutePath() + "]", e);
        }
        return null;
    }

    public String getAbsolutePath() {
        if (getActual() != null) {
            return getActual().getAbsolutePath();
        }
        return null;
    }

    public String getName() {
        if (getActual() != null) {
            return getActual().getName();
        }
        return null;
    }

    /**
     * Gets defaultCharset.
     *
     * @return Value of defaultCharset.
     */
    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    /**
     * Sets new defaultCharset.
     *
     * @param defaultCharset New value of defaultCharset.
     */
    public AssertFile withDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }
}
