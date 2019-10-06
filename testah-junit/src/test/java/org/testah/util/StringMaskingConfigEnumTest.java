package org.testah.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.testah.util.unittest.dtotest.SystemOutCapture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.testah.util.StringMaskingConfigEnum.USING_DEFAULT_CONFIG;

class StringMaskingConfigEnumTest
{
    @Test
    void testInstance()
    {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start())
        {
            config.getRootLogger().setLevel(Level.DEBUG);
            StringMaskingConfigEnum.INSTANCE.reset();

            assertEquals(StringMaskingConfigEnum.DEFAULT_MIN_STRING_LENGTH,
                    StringMaskingConfigEnum.INSTANCE.getInstance().getMinStringLength());
            assertThat(systemOutCapture.getSystemOut(), containsString(USING_DEFAULT_CONFIG));

            assertEquals(StringMaskingConfigEnum.DEFAULT_FIRST_N, StringMaskingConfigEnum.INSTANCE.getInstance().getFirstN());
            assertEquals(StringMaskingConfigEnum.DEFAULT_LAST_N, StringMaskingConfigEnum.INSTANCE.getInstance().getLastN());

            int minStringLength = 11;
            int firstN = 4;
            int lastN = 5;
            StringMaskingConfigEnum.INSTANCE.reset();
            assertEquals(minStringLength,
                    StringMaskingConfigEnum.INSTANCE.createInstance(minStringLength, firstN,lastN).getMinStringLength());
            assertEquals(firstN, StringMaskingConfigEnum.INSTANCE.getInstance().getFirstN());
            assertEquals(lastN, StringMaskingConfigEnum.INSTANCE.getInstance().getLastN());

        } finally
        {
            config.getRootLogger().setLevel(Level.INFO);
        }
    }

    @Test
    void testConcurrency()
    {
        List<Callable<Integer>> callables = new ArrayList<>();
        for (int icount = 1; icount < 100; icount++)
        {
            final int kcount = icount;
            StringMaskingConfigEnum.INSTANCE.reset();
            callables.add(() -> StringMaskingConfigEnum.INSTANCE.createInstance(kcount, 2, 3).getMinStringLength());
            StringMaskingConfigEnum.INSTANCE.reset();
            callables.add(() -> StringMaskingConfigEnum.INSTANCE.getInstance().getMinStringLength());
        }
        Collections.shuffle(callables);
        List<Future<Integer>> futures = null;

        ExecutorService executor = Executors.newFixedThreadPool(10);
        try
        {
            futures = executor.invokeAll(callables);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        executor.shutdown();

        Set<Integer> minStringLengths = new HashSet<>();
        for (int icount = 0; icount < futures.size(); icount++)
        {
            Future<Integer> future = futures.get(icount);
            try
            {
                minStringLengths.add(future.get());
            } catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        assertEquals(String.format("Check distinct values [%s] in minStringLengths, there should be only 1.",
                minStringLengths.toArray()),
                1, minStringLengths.size());
    }
}