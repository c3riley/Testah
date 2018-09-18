package org.testah.framework.report.asserts;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.testah.framework.report.VerboseAsserts;
import org.testah.framework.report.asserts.base.AbstractAssertBase;
import org.testah.framework.report.asserts.base.AssertFunctionReturnBooleanActual;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * The type Assert file.
 */
public class AssertFile extends AbstractAssertBase<AssertFile, File> {

    private Charset defaultCharset = Charset.forName("UTF-8");

    /**
     * Instantiates a new Assert file.
     *
     * @param actual the actual
     */
    public AssertFile(File actual) {
        super(actual);
    }

    /**
     * Instantiates a new Assert file.
     *
     * @param actual  the actual
     * @param asserts the asserts
     */
    public AssertFile(File actual, VerboseAsserts asserts) {
        super(actual, asserts);
    }

    /**
     * Exists assert file.
     *
     * @return the assert file
     */
    public AssertFile exists() {
        AssertFunctionReturnBooleanActual<File> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory(true);
            history.setActualForHistory(actual.exists());
            Assert.assertTrue(actual.exists());
            return true;
        };
        return runAssert("Check that file[" + getAbsolutePath() + "] exists", "exists",
                assertRun, null, getActual());
    }

    /**
     * Gets absolute path.
     *
     * @return the absolute path
     */
    public String getAbsolutePath() {
        if (getActual() != null) {
            return getActual().getAbsolutePath();
        }
        return null;
    }

    /**
     * Not exists assert file.
     *
     * @return the assert file
     */
    public AssertFile notExists() {
        AssertFunctionReturnBooleanActual<File> assertRun = (expected, actual, history) -> {
            history.setExpectedForHistory(false);
            history.setActualForHistory(actual.exists());
            Assert.assertFalse(actual.exists());
            return true;
        };
        return runAssert("Check that file[" + getAbsolutePath() + "] does not exist", "notExists",
                assertRun, null, getActual());
    }

    /**
     * Size assert number.
     *
     * @return the assert number
     */
    public AssertNumber size() {
        if (canAssertRun("fileSize", true)) {
            return new AssertNumber<Long>(getActual().length(), getAsserts());
        }
        return new AssertNumber<Integer>(null, getAsserts());
    }

    /**
     * Content equals assert file.
     *
     * @param expected the expected
     * @return the assert file
     */
    public AssertFile contentEquals(final File expected) {
        return contentEquals(expected, Charset.forName("UTF-8"));
    }

    /**
     * Content equals assert file.
     *
     * @param expected the expected
     * @param charset  the charset
     * @return the assert file
     */
    public AssertFile contentEquals(final File expected, Charset charset) {
        return contentEquals(getContentAsString(expected, charset));
    }

    /**
     * Content equals assert file.
     *
     * @param expected the expected
     * @return the assert file
     */
    public AssertFile contentEquals(final String expected) {
        return contentEquals(expected, Charset.forName("UTF-8"));
    }

    /**
     * Content equals assert file.
     *
     * @param expected the expected
     * @param charset  the charset
     * @return the assert file
     */
    public AssertFile contentEquals(final String expected, Charset charset) {
        return this.addStatus(new AssertStrings(getContentAsString(charset)).equalsTo(expected).isPassed());
    }

    /**
     * Gets content as string.
     *
     * @param file    the file
     * @param charset the charset
     * @return the content as string
     */
    public String getContentAsString(final File file, Charset charset) {
        try {
            return FileUtils.readFileToString(getActual(), charset);
        } catch (IOException e) {
            getAsserts().fail("Issue getting the String Content of the file[" + file.getAbsolutePath() + "]", e);
        }
        return null;
    }

    /**
     * Gets content as string.
     *
     * @param charset the charset
     * @return the content as string
     */
    public String getContentAsString(Charset charset) {
        return getContentAsString(getActual(), charset);
    }

    /**
     * Gets content as string.
     *
     * @return the content as string
     */
    public String getContentAsString() {
        return getContentAsString(defaultCharset);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
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
     * @return the assert file
     */
    public AssertFile withDefaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }
}