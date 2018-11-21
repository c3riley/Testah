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

/**
 * The type Consume event stream.
 */
public class ConsumeEventStream {

    private static final String TIMEOUT_MSG = "Server Side event took to long, went over %dmax";

    private final String url;
    private final Long intervalTime = 1000L;
    private String chunkType = "text/event-stream";
    private int waitTimeout = 120;
    private ObjectMapper map = null;
    private List<String> rawMessages = new ArrayList<String>();
    private boolean verbose;
    private boolean assertTimeout = false;
    @SuppressWarnings("unchecked")
    private ICloseStream closeStream = null;

    /**
     * Instantiates a new Consume event stream.
     *
     * @param url the url
     */
    public ConsumeEventStream(final String url) {
        this(url, true);
    }

    /**
     * Instantiates a new Consume event stream.
     *
     * @param url     the url
     * @param verbose the verbose
     */
    public ConsumeEventStream(final String url, final boolean verbose) {
        this.url = url;
        this.verbose = verbose;
    }

    /**
     * Consume json list.
     *
     * @return the list
     * @throws Exception the exception
     */
    public List<JsonNode> consumeJson() throws Exception {
        return consume(JsonNode.class);
    }

    /**
     * Consume list.
     *
     * @param <H>          the type parameter
     * @param messageClass the message class
     * @return the list
     * @throws Exception the exception
     */
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

    /**
     * Consume list.
     *
     * @return the list
     * @throws Exception the exception
     */
    public List<String> consume() throws Exception {
        return consume(String.class);
    }

    /**
     * Is verbose boolean.
     *
     * @return the boolean
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets verbose.
     *
     * @param verbose the verbose
     * @return the verbose
     */
    public ConsumeEventStream setVerbose(final boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets chunk type.
     *
     * @return the chunk type
     */
    public String getChunkType() {
        return chunkType;
    }

    /**
     * Sets chunk type.
     *
     * @param chunkType the chunk type
     * @return the chunk type
     */
    public ConsumeEventStream setChunkType(final String chunkType) {
        this.chunkType = chunkType;
        return this;
    }

    @SuppressWarnings("unchecked")
    private <H> List<H> readStream(final EventInput eventInput, Class<H> messageClass) {
        final List<H> lstData = new ArrayList<H>();
        int ctr = 0;
        while (true) {
            final String data = readData(eventInput.read());
            if (null == data) {
                break;
            }
            this.getRawMessages().add(data);
            try {
                H dataValue;
                if (messageClass.getClass().equals(String.class)) {

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
            if (ctr++ > getWaitTimeout()) {
                if (isAssertTimeout()) {
                    TS.asserts().fail(String.format(TIMEOUT_MSG, getWaitTimeout()));
                } else {
                    TS.log().warn(String.format(TIMEOUT_MSG, getWaitTimeout()));
                }
            }
            TS.util().pause(getIntervalTime(), "Waiting for SseFeature", ctr);
        }
        if (isVerbose()) {
            printRawMessages();
        }
        return lstData;
    }

    /**
     * Read data string.
     *
     * @param inboundEvent the inbound event
     * @return the string
     */
    public String readData(final InboundEvent inboundEvent) {
        if (inboundEvent == null) {
            return null;
        } else {
            return inboundEvent.readData();
        }
    }

    /**
     * Gets raw messages.
     *
     * @return the raw messages
     */
    public List<String> getRawMessages() {
        if (null == this.rawMessages) {
            this.rawMessages = new ArrayList<String>();
        }
        return rawMessages;
    }

    /**
     * Sets raw messages.
     *
     * @param rawMessages the raw messages
     * @return the raw messages
     */
    public ConsumeEventStream setRawMessages(final List<String> rawMessages) {
        this.rawMessages = rawMessages;
        return this;
    }

    /**
     * Gets map.
     *
     * @return the map
     */
    public ObjectMapper getMap() {
        if (null == map) {
            this.map = TS.util().getMap();
        }
        return map;
    }

    @SuppressWarnings("unchecked")
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

    /**
     * Gets wait timeout.
     *
     * @return the wait timeout
     */
    public int getWaitTimeout() {
        return waitTimeout;
    }

    /**
     * Sets wait timeout.
     *
     * @param waitTimeout the wait timeout
     * @return the wait timeout
     */
    public ConsumeEventStream setWaitTimeout(final int waitTimeout) {
        this.waitTimeout = waitTimeout;
        return this;
    }

    /**
     * Is assert timeout boolean.
     *
     * @return the boolean
     */
    public boolean isAssertTimeout() {
        return assertTimeout;
    }

    /**
     * Sets assert timeout.
     *
     * @param assertTimeout the assert timeout
     * @return the assert timeout
     */
    public ConsumeEventStream setAssertTimeout(final boolean assertTimeout) {
        this.assertTimeout = assertTimeout;
        return this;
    }

    /**
     * Gets interval time.
     *
     * @return the interval time
     */
    public Long getIntervalTime() {
        return intervalTime;
    }

    /**
     * Print raw messages consume event stream.
     *
     * @return the consume event stream
     */
    public ConsumeEventStream printRawMessages() {
        final AtomicInteger counter = new AtomicInteger(0);
        TS.log().info("ConsumeEventStream Messages. Found: " + getRawMessages().size());
        getRawMessages().forEach(msg -> {
            TS.log().info(counter.getAndIncrement() + "] " + msg);
        });
        return this;
    }

    /**
     * Process data object.
     *
     * @param data the data
     * @return the object
     */
    protected Object processData(final String data) {
        try {
            getMap().readTree(data);
        } catch (Throwable throwable) {
            TS.asserts().fail("Issue trying to process data[" + data + "] - " + throwable.getMessage());
        }
        return null;
    }

    /**
     * Sets close stream.
     *
     * @param closeStream the close stream
     * @return the close stream
     */
    public ConsumeEventStream setCloseStream(final ICloseStream<?> closeStream) {
        this.closeStream = closeStream;
        return this;
    }
}
