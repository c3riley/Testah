package org.testah.runner.performance;

import java.util.List;

import org.testah.TS;
import org.testah.driver.http.response.ResponseDto;
import org.testah.framework.report.performance.dto.ChunkStats;
import org.testah.runner.http.load.HttpAkkaStats;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ChunkStatsLogPublisher implements ExecutionStatsPublisher {

    /**
     * Write stats to log.
     *
     * @see org.testah.runner.performance.ExecutionStatsPublisher#push(java.util.List)
     */
    @Override
    public void push(List<ResponseDto> responses) throws Exception {
        HttpAkkaStats stats = new HttpAkkaStats(responses);
        ChunkStats chunkStats = new ChunkStats(stats);

        // no pretty print to save space in log file
        TS.log().info(new ObjectMapper().writeValueAsString((ChunkStats) chunkStats));
    }

    @Override
    public void cleanup()
    {
        // no post processing required
    }
}
