package org.testah.runner.jdbc.load;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JdbcRunner {
    private final ConcurrentLinkedQueue<String> queryQueue;
    final String urlString;

    public JdbcRunner(String urlString, ConcurrentLinkedQueue<String> queryQueue) {
        this.urlString = urlString;
        this.queryQueue = queryQueue;
    }

    public void runParallel(int numberOfConnections, Duration duration)
            throws SQLException {
        List<Callable<JdbcTask>> jdbcTasks = new ArrayList<>();

        for (int i = 0; i < numberOfConnections; i++) {
            JdbcTask jdbcTask = new JdbcTask(urlString, queryQueue, duration);
            jdbcTasks.add(jdbcTask);
        }
        List<Future<JdbcTask>> futures = null;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfConnections);
        try {
            futures = executorService.invokeAll(jdbcTasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            for (Callable<JdbcTask> jdbcTask : jdbcTasks) {
                ((JdbcTask)jdbcTask).close();
            }
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        long queryCount = 0;
        for (Future<JdbcTask> future : futures) {
            try {
                queryCount += future.get(500L, TimeUnit.MILLISECONDS).getQueryCount();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total number of queries: " + queryCount);
    }
}
