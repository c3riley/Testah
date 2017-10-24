package org.testah.runner.performance;

import org.testah.framework.report.performance.dto.ChunkStats;

public interface ExecutionStatsPublisher {
    public void push(ChunkStats chunkStats) throws Exception;
}
