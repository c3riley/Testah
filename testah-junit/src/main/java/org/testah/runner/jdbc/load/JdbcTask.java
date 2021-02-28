package org.testah.runner.jdbc.load;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JdbcTask implements Callable {

    private final ConcurrentLinkedQueue<String> queryStrings;
    private final JdbcDriver jdbcDriver;
    private final Duration duration;
    private final long stopTime;
    private long queryCounter = 0;

    public JdbcTask(String connectionString, ConcurrentLinkedQueue<String> queryStrings, Duration duration) throws SQLException {
        this.queryStrings = queryStrings;
        this.jdbcDriver = new JdbcDriver(connectionString);
        this.duration = duration;
        stopTime = System.currentTimeMillis() + duration.toMillis();
    }

    public void close() throws SQLException {
        jdbcDriver.close();
    }

    public long getQueryCount() {
        return queryCounter;
    }

    @Override
    public JdbcTask call() throws Exception {
        long startTime = System.nanoTime();
        long queryStartTime;
        ResultSet resultSet;
        String msgTemplate = "Time=%d, Thread=%s, Query=%s";
        while (stopTime > System.currentTimeMillis()) {
            queryCounter++;
            String queryString = queryStrings.poll();
            queryStartTime = System.nanoTime();
            resultSet = jdbcDriver.getPreparedStatement(queryString).executeQuery();
            jdbcDriver.getPreparedStatement(queryString).execute();
            queryStrings.add(queryString);
            System.out.println(String.format(msgTemplate,  System.nanoTime() - queryStartTime, Thread.currentThread().getName(), queryString));
        }
        return this;
    }
}
