package org.testah.framework.cli;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum IgnoredTestRecorder
{
    INSTANCE;

    private boolean isNewFile = true;
    private final Set<String> recordSet = new HashSet<>();

    public static void recordIgnoredTestPlan(String testPlan, int testCaseCount)
    {
        if (!testPlan.trim().isEmpty())
        {
            INSTANCE.record(String.format("PLAN,%s,%d", testPlan, testCaseCount));
        }
    }

    public static void recordIgnoredTestCase(String testCase)
    {
        if (StringUtils.isNotEmpty(testCase)) {
            List<String> normalized =
                Arrays.stream(testCase.split("[()]")).filter(item -> !item.trim().isEmpty()).collect(Collectors.toList());
            Collections.reverse(normalized);
            INSTANCE.record(String.format("CASE,%s,%d", String.join(".", normalized), 1));
        }
    }

    public static void recordIgnoredTestCase(String testPlan, String testCase)
    {
        if (StringUtils.isNotEmpty(testPlan) && StringUtils.isNotEmpty(testCase)) {
            INSTANCE.record(String.format("CASE,%s,%d", String.join(".", testPlan, testCase), 1));
        }
    }

    public static void recordTotalNumberExecutedTestPlans(int numberTestPlans)
    {
        INSTANCE.record(String.format("META,PLANS,%d", numberTestPlans));
    }

    public static void recordTotalNumberExecutedTestCases(int numberTestCases)
    {
        INSTANCE.record(String.format("META,CASES,%d", numberTestCases));
    }

    public static void recordFilterKnownProblems(String isFilterKnownFailures)
    {
        INSTANCE.record(String.format("META,FILTER_KNOWN_PROBLEMS,%b", Boolean.parseBoolean(isFilterKnownFailures)));
    }

    private synchronized void record(String test)
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
            if (!recordSet.contains(test)) {
                Files.write(Paths.get(ignoredTestCasesFile), String.format("%s%n", test).getBytes(StandardCharsets.UTF_8), openOption);
                recordSet.add(test);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
