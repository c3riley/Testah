package org.testah.runner.performance.dto

import spock.lang.Specification

class SequenceExecDataTest extends Specification {
    def "GetCount"() {
        given:
        SequenceExecData sequenceExecData = new SequenceExecData()
        sequenceExecData
                .add(1, new ExecData().withReceiveCount(1).withSendCount(2))
                .add(2, new ExecData().withReceiveCount(2).withSendCount(3))
                .add(3, new ExecData().withReceiveCount(3).withSendCount(3))
                .add(4, new ExecData().withReceiveCount(4).withSendCount(4))

        expect:
        sequenceExecData.getReceiveCount() == 10
        sequenceExecData.getSendCount() == 12
    }

    def "GetStep"() {
        given:
        SequenceExecData sequenceExecData = new SequenceExecData()
        sequenceExecData
                .add(1, new ExecData().withReceiveCount(1))
                .add(3, new ExecData().withSendCount(2))
                .add(7, new ExecData().withReceiveCount(3))

        expect:
        sequenceExecData.getByStepCount(0).getStepId() == 1
        sequenceExecData.getByStepCount(0).getReceiveCount() == 1
        sequenceExecData.getByStepCount(1).getStepId() == 3
        sequenceExecData.getByStepCount(1).getSendCount() == 2
        sequenceExecData.getByStepCount(2).getStepId() == 7
        sequenceExecData.getByStepCount(2).getReceiveCount() == 3
    }
}
