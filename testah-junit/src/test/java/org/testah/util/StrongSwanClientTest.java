package org.testah.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class StrongSwanClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void sendMessage() throws Exception {
        StrongSwanClient swan = new StrongSwanClient("testPath");
        assertThat(swan.getSocketFullPath(), equalTo("testPath"));
        swan.setSocketFullPath("testPath2");
        assertThat(swan.getSocketFullPath(), equalTo("testPath2"));
    }

    @Test
    public void sendMessageWithNull() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Messages param is null");
        new StrongSwanClient("testPath").sendMessage((String) null);
    }

    @Test
    public void sendMessageWithMissingSocket() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Missing socket file:testPath");
        new StrongSwanClient("testPath").sendMessage("test");
    }

    @Test
    public void sendMessageWithBadSocket() throws Exception {
        thrown.expect(ExceptionInInitializerError.class);
        File tempSocket = File.createTempFile("socket","");
        tempSocket.deleteOnExit();
        new StrongSwanClient(tempSocket.getAbsolutePath()).sendMessage("test");
    }

    @Test
    public void testMessageEnum() throws Exception {
        assertThat(StrongSwanClient.Message.SECTION_START.getValue(), equalTo(1));
        assertThat(StrongSwanClient.Message.SECTION_END.getValue(), equalTo(2));
        assertThat(StrongSwanClient.Message.LIST_START.getValue(), equalTo(4));
        assertThat(StrongSwanClient.Message.LIST_ITEM.getValue(), equalTo(5));
        assertThat(StrongSwanClient.Message.LIST_END.getValue(), equalTo(6));

    }
}