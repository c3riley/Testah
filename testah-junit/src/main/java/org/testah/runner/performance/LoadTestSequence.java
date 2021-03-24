package org.testah.runner.performance;

import org.testah.TS;
import org.testah.runner.performance.dto.LoadTestSequenceDto;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoadTestSequence {
    LoadTestSequenceDto[] loadTestSequence;

    public LoadTestSequence(String resourceFile) throws IOException
    {
        loadTestSequence =
            TS.util().getMap().readValue(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(resourceFile),
                    Charset.forName("UTF-8")),
                LoadTestSequenceDto[].class);
    }

    /**
     * Get the steps of the load test with parameters not set in the sequence resource file
     * defaulting to values from the runProperties.
     * @param runProperties properties for entire run
     * @return load test steps as list of LoadTestSequenceDto
     */
    public List<LoadTestSequenceDto> getSteps(TestRunProperties runProperties) {
        return Arrays.stream(loadTestSequence).map(step -> step.fillUndefined(runProperties)).collect(Collectors.toList());
    }

    /**
     * Get the number of steps.
     * @return the number of steps defined in the resource file
     */
    public int size() {
        return loadTestSequence.length;
    }
}
