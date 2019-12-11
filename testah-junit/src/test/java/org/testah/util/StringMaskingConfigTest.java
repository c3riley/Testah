package org.testah.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
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
import static org.testah.util.StringMaskingConfig.USING_DEFAULT_CONFIG;

class StringMaskingConfigTest
{
    @Test
    @Ignore
    void testNotification()
    {
        Configuration config = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
        try (SystemOutCapture systemOutCapture = new SystemOutCapture().start())
        {
            StringMaskingConfig.INSTANCE.reset();
            config.getRootLogger().setLevel(Level.DEBUG);
            StringMaskingConfig.INSTANCE.reset();

            assertEquals(StringMaskingConfig.DEFAULT_MIN_STRING_LENGTH,
                    StringMaskingConfig.INSTANCE.getInstance().getMinStringLength());
            assertThat(systemOutCapture.getSystemOut(), containsString(USING_DEFAULT_CONFIG));
        } finally
        {
            config.getRootLogger().setLevel(Level.INFO);
        }
    }

    @Test
    void testInstance()
    {
        StringMaskingConfig.INSTANCE.reset();

        assertEquals(StringMaskingConfig.DEFAULT_MIN_STRING_LENGTH,
                StringMaskingConfig.INSTANCE.getInstance().getMinStringLength());
        assertEquals(StringMaskingConfig.DEFAULT_FIRST_N, StringMaskingConfig.INSTANCE.getInstance().getFirstN());
        assertEquals(StringMaskingConfig.DEFAULT_LAST_N, StringMaskingConfig.INSTANCE.getInstance().getLastN());

        int minStringLength = 11;
        int firstN = 4;
        int lastN = 5;
        StringMaskingConfig.INSTANCE.reset();
        assertEquals(minStringLength,
                StringMaskingConfig.INSTANCE.createInstance(minStringLength, firstN,lastN).getMinStringLength());
        assertEquals(firstN, StringMaskingConfig.INSTANCE.getInstance().getFirstN());
        assertEquals(lastN, StringMaskingConfig.INSTANCE.getInstance().getLastN());
    }

    @Test
    void testConcurrency()
    {
        List<Callable<Integer>> callables = new ArrayList<>();
        for (int icount = 1; icount < 100; icount++)
        {
            final int kcount = icount;
            StringMaskingConfig.INSTANCE.reset();
            callables.add(() -> StringMaskingConfig.INSTANCE.createInstance(kcount, 2, 3).getMinStringLength());
            StringMaskingConfig.INSTANCE.reset();
            callables.add(() -> StringMaskingConfig.INSTANCE.getInstance().getMinStringLength());
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