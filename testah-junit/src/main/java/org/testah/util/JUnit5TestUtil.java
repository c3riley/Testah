package org.testah.util;

import org.junit.jupiter.api.TestInfo;

public class JUnit5TestUtil
{
    public static final int MAX_LENGTH = 55;
    public static final int RANDOM_LENGTH = 5;

    /**
     * JUnit5 has a TestInfo class that includes information about the test. In particular,
     * it has the class, method name and display name. In case of the data provider, the display
     * name includes the test case index. Class, method name and index make a test specific
     * make the id fairly specific to the test case. In addition, a random string of given
     * length is appended to make the id unique over executions.
     * Note that in parametrized tests, TestInfo must be declared after the list of parameters
     * resolved by the resolver, see e.g. section 'Formal Parameter List' in
     * @see <a href="https://junit.org/junit5/docs/5.3.0/api/org/junit/jupiter/params/ParameterizedTest.html">
     *     https://junit.org/junit5/docs/5.3.0/api/org/junit/jupiter/params/ParameterizedTest.html</a>
     * @param testInfo TestInfo object from JUnit5 test case
     * @param maxLength overall maximal length of unique string
     * @param randomLength part of string that is random alphanumeric; if larger or equal to maxLength
     *                     the returned string is random alphanumeric of maxLength
     * @return unique string for test case
     */
    public static String getTestCaseId(TestInfo testInfo, int maxLength, int randomLength) {
        return new Junit5TestId(testInfo, maxLength, randomLength).getTestCaseId();
    }

    /**
     * Construct a Junit5UniqueTestId object with default max length=55 and random length=5.
     * @param testInfo TestInfo object from JUnit5 test case
     * @return Junit5UniqueTestId object with unique id, and regex pattern for the deterministic part of the id
     */
    public static Junit5TestId getJunit5TestId(TestInfo testInfo) {
        return new Junit5TestId(testInfo, MAX_LENGTH, RANDOM_LENGTH);
    }

    /**
     * Construct a Junit5UniqueTestId object.
     * @param testInfo TestInfo object from JUnit5 test case
     * @param maxLength overall maximal length of unique string
     * @param randomLength part of string that is random alphanumeric; if larger or equal to maxLength
     *                     the returned string is random alphanumeric of maxLength
     * @return Junit5UniqueTestId object with unique id, and regex pattern for the deterministic part of the id
     */
    public static Junit5TestId getJunit5TestId(TestInfo testInfo, int maxLength, int randomLength) {
        return new Junit5TestId(testInfo, maxLength, randomLength);
    }
}
