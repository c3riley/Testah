package org.testah.driver.http.eventstream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.testah.TS;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumeEventStream {

    private final static String TIMEOUT_MSG = "SseFeature Took to long, went over 120 max";

    private final String url;
    private String chunkType = "text/event-stream";
    private int waitTimeout = 120;
    private final Long intervailTime = 1000L;
    private ObjectMapper map = null;
    private List<String> rawMessages = new ArrayList<String>();
    private boolean verbose;
    private boolean assertTimeout = false;
    private ICloseStream closeStream = null;

    public ConsumeEventStream(final String url) {
        this(url, true);
    }

    public ConsumeEventStream(final String url, final boolean verbose) {
        this.url = url;
        this.verbose = verbose;
    }

    public List<String> consume() throws Exception {
        return consume(String.class);
    }

    public List<JsonNode> consumeJson() throws Exception {
        return consume(JsonNode.class);
    }

    public <H> List<H> consume(Class<H> messageClass) throws Exception {
        if (isVerbose()) {
            TS.log().info("Consuming Sse for url: " + getUrl());
        }
        this.rawMessages = new ArrayList<String>();
        Client client = ClientBuilder.newBuilder().register(new SseFeature()).build();
        WebTarget target = client.target(url);
        EventInput eventInput = target.request().get(EventInput.class);
        eventInput.setChunkType(getChunkType());
        return readStream(eventInput, messageClass);
    }

    private <H> List<H> readStream(final EventInput eventInput, Class<H> messageClass) {
        final List<H> lstData = new ArrayList<H>();
        int i = 0;
        while (true) {
            final String data = readData(eventInput.read());
            if (null == data) {
                break;
            }
            this.getRawMessages().add(data);
            try {
                H dataValue;
                if ((H) new Object() instanceof String) {
                    dataValue = (H) data;
                } else {
                    dataValue = getMap().readValue(data, messageClass);
                }
                lstData.add(dataValue);
                if (getCloseStream(messageClass).shouldCloseStream(dataValue)) {
                    if (isVerbose()) {
                        TS.log().info("Closing EventInput due to shouldCloseStream returning true");
                    }
                    eventInput.close();
                    break;
                }
            } catch (Throwable serializeError) {
                TS.log().warn(serializeError);
            }
            if (i++ > getWaitTimeout()) {
                if (isAssertTimeout()) {
                    TS.asserts().fail(TIMEOUT_MSG);
                } else {
                    TS.log().warn(TIMEOUT_MSG);
                }
            }
            TS.util().pause(getIntervailTime(), "Waiting for SseFeature", i);
        }
        if (isVerbose()) {
            printRawMessages();
        }
        return lstData;
    }

    public String readData(final InboundEvent inboundEvent) {
        if (inboundEvent == null) {
            return null;
        } else {
            return inboundEvent.readData();
        }
    }

    protected Object processData(final String data) {
        try {
            getMap().readTree(data);
        } catch (Throwable throwable) {
            TS.asserts().fail("Issue trying to process data[" + data + "] - " + throwable.getMessage());
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public String getChunkType() {
        return chunkType;
    }

    public ConsumeEventStream setChunkType(final String chunkType) {
        this.chunkType = chunkType;
        return this;
    }

    public int getWaitTimeout() {
        return waitTimeout;
    }

    public ConsumeEventStream setWaitTimeout(final int waitTimeout) {
        this.waitTimeout = waitTimeout;
        return this;
    }

    public Long getIntervailTime() {
        return intervailTime;
    }

    public ObjectMapper getMap() {
        if (null == map) {
            this.map = TS.util().getMap();
        }
        return map;
    }

    public ConsumeEventStream printRawMessages() {
        final AtomicInteger counter = new AtomicInteger(0);
        TS.log().info("ConsumeEventStream Messages. Found: " + getRawMessages().size());
        getRawMessages().forEach(msg -> {
            TS.log().info(counter.getAndIncrement() + "] " + msg);
        });
        return this;
    }

    public List<String> getRawMessages() {
        if (null == this.rawMessages) {
            this.rawMessages = new ArrayList<String>();
        }
        return rawMessages;
    }

    public ConsumeEventStream setRawMessages(final List<String> rawMessages) {
        this.rawMessages = rawMessages;
        return this;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public ConsumeEventStream setVerbose(final boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public boolean isAssertTimeout() {
        return assertTimeout;
    }

    public ConsumeEventStream setAssertTimeout(final boolean assertTimeout) {
        this.assertTimeout = assertTimeout;
        return this;
    }

    private <J> ICloseStream<J> getCloseStream(Class<J> dataObj) {
        if (null == closeStream) {
            closeStream = new ICloseStream<J>() {
                @Override
                public boolean shouldCloseStream(final J data) {
                    return false;
                }
            };
        }
        return this.closeStream;
    }

    public ConsumeEventStream setCloseStream(final ICloseStream<?> closeStream) {
        this.closeStream = closeStream;
        return this;
    }
}
