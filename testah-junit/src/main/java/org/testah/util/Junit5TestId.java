package org.testah.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.TestInfo;
import org.testah.TS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Junit5TestId
{
    private String uniqueId;
    private String baseName;
    private String dataSetId = "";
    private String randomPart;
    private Integer dataSetIndex;

    /**
     * JUnit5 has a TestInfo class that includes information about the test. In particular,
     * it has the class, method name and display name. In case of the data provider, the display
     * name includes the test case index. Class, method name and index make the id fairly specific
     * to the test case. In addition, a random string of given length is appended to make the id
     * unique over executions.
     * Note that in parametrized tests, TestInfo must be declared after the list of parameters
     * resolved by the resolver, see e.g. section 'Formal Parameter List' in
     * @see <a href="https://junit.org/junit5/docs/5.3.0/api/org/junit/jupiter/params/ParameterizedTest.html">
     * https://junit.org/junit5/docs/5.3.0/api/org/junit/jupiter/params/ParameterizedTest.html</a>
     *
     * @param testInfo     TestInfo object from JUnit5 test case
     * @param maxLength    overall maximal length of unique string
     * @param randomLength part of string that is random alphanumeric; if larger or equal to maxLength
     *                     the returned string is random alphanumeric of maxLength
     */
    public Junit5TestId(TestInfo testInfo, int maxLength, int randomLength)
    {
        setDataSetIndex(testInfo.getDisplayName());
        baseName = "";
        dataSetId = "";
        if (maxLength > randomLength)
        {
            String className = testInfo.getTestClass().isPresent() ? testInfo.getTestClass().get().getSimpleName() : "CLASS_UNDEFINED";
            String methodName = testInfo.getTestMethod().isPresent() ? testInfo.getTestMethod().get().getName() : "METHOD_UNDEFINED";
            dataSetId = dataSetIndex != null && dataSetIndex > 0 ? dataSetIndex.toString() : "";
            baseName = shortenString(className + methodName, (maxLength - randomLength - dataSetId.length()));
        }
        randomPart = RandomStringUtils.randomAlphanumeric(Math.min(maxLength, randomLength));
        uniqueId = baseName + dataSetId + randomPart;
    }

    /**
     * If necessary shorten a given string to the specified length:
     * first remove vowels consecutively, then take a substring.
     *
     * @param origString the original String
     * @param maxLength  maximal length of output string
     * @return shortened String.
     */
    public static String shortenString(String origString, int maxLength)
    {
        String shortenedString = origString;
        String[] vowels = {"e", "a", "i", "o", "u"};

        for (String vowel : vowels)
        {
            while (shortenedString.contains(vowel))
            {
                if (shortenedString.length() <= maxLength)
                {
                    break;
                }
                shortenedString = shortenedString.replaceFirst(vowel, "");
            }
        }
        return shortenedString.substring(0, Math.min(maxLength, shortenedString.length()));
    }

    /**
     * Return a String that is unique and identifies the test it belongs to.
     *
     * @return the unique string
     */
    public String getTestCaseId()
    {
        return uniqueId;
    }

    /**
     * Return the deterministic portion of the unique id, i.e. everything but the random portion.
     *
     * @param includeDatasetIndex if true include the dataset count
     * @return the deterministic portion of the unique id
     */
    public String getDeterministicPart(boolean includeDatasetIndex)
    {
        return includeDatasetIndex ? baseName + dataSetId : baseName;
    }

    /**
     * Return a regex that matches the whole unique id, in particular, it matches the deterministic part exactly.
     *
     * @param datasetIndex if true the pattern includes the dataset index if case of a data provider
     * @return a regex pattern consisting of the deterministic portion of the unique id and any string of any length
     */
    public String getRegexMatcher(boolean datasetIndex)
    {
        return getDeterministicPart(datasetIndex) + ".*";
    }

    /**
     * Return a regex pattern that matches the whole unique id, in particular, it matches the deterministic part exactly.
     * A possible dataset index is not called out.
     *
     * @return a regex pattern consisting of the deterministic portion of the unique id and any string of any length
     */
    public String getRegexMatcher()
    {
        return getRegexMatcher(false);
    }

    protected void setDataSetIndex(String displayName) {
        dataSetIndex = 0;

        // Extract the index from
        //@ParameterizedTest(name = "{displayName} => {index} - {arguments}")
        Pattern pattern = Pattern.compile("^.* => (\\d+) - .*$");
        Matcher matcher = pattern.matcher(displayName);
        if (matcher.find())
        {
            String indexString = matcher.group(1);
            try {
                dataSetIndex = Integer.parseInt(indexString);
            } catch (Exception e) {
                TS.log().warn(String.format("Cannot parse %s into Integer.", indexString));
            }
        }
    }

    public String getDataSetId() {
        return dataSetId;
    }

    public int getDataSetIndex() {
        return dataSetIndex;
    }
}
