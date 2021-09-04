package org.testah.framework.cli;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.testah.TS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum IgnoredTestRecorder
{
    INSTANCE;

    public static final String FILTER_KNOWN_PROBLEMS = "FILTER_KNOWN_PROBLEMS";
    private boolean isNewFile = true;
    private Boolean isFilterKnownProblems = null;
    private final Map<String, Map<String, Integer>> recordMap = new HashMap<>();
    public static final String TEST_PLAN = "PLAN";
    public static final String TEST_CASE = "CASE";
    public static final String META = "META";

    /**
     * Record the specified test plan as ignored.
     *
     * @param testPlan fully qualified test plan name, e.g. 'com.eagleinvsys.e2e.TestPlan'
     * @param testCaseCount count of test cases in the test plan
     */
    public static void recordIgnoredTestPlan(String testPlan, int testCaseCount)
    {
        if (StringUtils.isNotBlank(testPlan)) {
            INSTANCE.record(TEST_PLAN, testPlan, testCaseCount);
        }
        else {
            TS.log().warn("Not recording ignored test without a name.");
        }
    }

    /**
     * Record the specified test case as ignored.
     * Excepts the following two formats
     * <ul>
     *     <li>
     *         (&lt;simple_case_name&gt;)&lt;canonical_plan_name&gt;, e.g.
     *     'testCase(com.eagleinvsys.e2e.TestPlan)'
     *     </li>
     *     <li>
     *         &lt;canonical_case_name&gt;, e.g.
     *         'com.eagleinvsys.e2e.TestPlan.testCase'
     *     </li>
     * </ul>
     *
     * @param testName fully qualified/canonical test case name
     */
    public static void recordIgnoredTestCase(String testName)
    {
        if (StringUtils.isNotBlank(testName)) {
            List<String> normalizedTestName =
                Arrays.stream(testName.split("[()]")).filter(item -> !item.trim().isEmpty()).collect(Collectors.toList());
            Collections.reverse(normalizedTestName);
            INSTANCE.record(TEST_CASE, String.join(".", normalizedTestName), 1);
        }
        else {
            TS.log().warn("Not recording ignored test without a name.");
        }
    }

    /**
     * Record the specified test case as ignored.
     *
     * @param testPlanName fully qualified/canonical test plan name
     * @param testCaseName simple test case name
     */
    public static void recordIgnoredTestCase(String testPlanName, String testCaseName)
    {
        if (StringUtils.isNotBlank(testPlanName) && StringUtils.isNotBlank(testCaseName)) {
            String normalizedTestCaseName = testCaseName.split("\\(\\)")[0].trim();
            if (StringUtils.isNotEmpty(normalizedTestCaseName)) {
                INSTANCE.record(TEST_CASE, String.join(".", testPlanName, normalizedTestCaseName), 1);
            }
            else {
                TS.log().warn(String.format("Not recording ignored test. Test plan name '%s', invalid test case name '%s')",
                    testPlanName, testCaseName));
            }
        }
        else {
            TS.log().warn("Not recording ignored test without a name.");
        }
    }

    /**
     * Record the number of test plans that were executed.
     *
     * @param numberTestPlans the number of executed test plans
     */
    public static void recordTotalNumberExecutedTestPlans(int numberTestPlans)
    {
        INSTANCE.record(META, TEST_PLAN, numberTestPlans);
    }

    /**
     * Record the number of test cases that were executed.
     *
     * @param numberTestCases the number of executed test cases
     */
    public static void recordTotalNumberExecutedTestCases(int numberTestCases)
    {
        INSTANCE.record(META, TEST_CASE, numberTestCases);
    }

    /**
     * Set the value of isFilterKnownProblems, whether the KnownProblems annotation was honored.
     *
     * @param isFilterKnownProblems intented value of isFilterKnownProblems
     */
    public static void recordFilterKnownProblems(String isFilterKnownProblems)
    {
        INSTANCE.record(META, FILTER_KNOWN_PROBLEMS, Boolean.parseBoolean(isFilterKnownProblems));
    }

    /**
     * Get value of isFilterKnownProblems.
     *
     * @return value of isFilterKnownProblems parameter
     */
    public static boolean isRecordFilterKnownProblems() {
        return INSTANCE.isFilterKnownProblems.booleanValue();
    }

    /**
     * Get the list of ignored alphabetically sorted test cases.
     *
     * @return list of ignored test cases
     */
    public static List<String> getIgnoredTestCases() {
        Map<String, Integer> testMap = INSTANCE.getIgnoredMap(TEST_CASE);
        if (MapUtils.isNotEmpty(testMap)) {
            return testMap.keySet().stream().sorted().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Get the list of ignored test plan map entries alphabetically sorted by plan name.
     *
     * @return list of plan name/test case count map entries
     */
    public static List<Map.Entry<String, Integer>> getIgnoredTestPlans() {
        Map<String, Integer> testMap = INSTANCE.getIgnoredMap(TEST_PLAN);
        if (MapUtils.isNotEmpty(testMap)) {
            return testMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Map<String, Integer> getIgnoredMap(String type) {
        return INSTANCE.recordMap.get(type);
    }

    private synchronized void record(String type, String test, boolean isFilterKnownProblems) {
        if (this.isFilterKnownProblems == null) {
            this.isFilterKnownProblems = isFilterKnownProblems;
            writeToFile(String.format("%s,%s,%b", type, test, isFilterKnownProblems));
        }
    }

    private synchronized void record(String type, String test, int count) {
        if (!recordMap.containsKey(type)) {
            recordMap.put(type, new HashMap<>());
        }
        if (!recordMap.get(type).containsKey(test)) {
            recordMap.get(type).put(test, count);
            writeToFile(String.format("%s,%s,%d", type, test, count));
        }
    }

    private synchronized void writeToFile(String test)
    {
        String ignoredTestCasesFile = "fileName.txt";
        StandardOpenOption openOption = StandardOpenOption.APPEND;
        try
        {
            if (isNewFile)
            {
                Files.deleteIfExists(Paths.get(ignoredTestCasesFile));
                openOption = StandardOpenOption.CREATE_NEW;
                isNewFile = false;
            }
            Files.write(Paths.get(ignoredTestCasesFile), String.format("%s%n", test).getBytes(StandardCharsets.UTF_8), openOption);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
