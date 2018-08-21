package org.testah.util.mail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.testah.TS;

import javax.mail.Message;
import javax.mail.MessagingException;

import java.io.IOException;

import static org.junit.Assert.*;

public class GmailEmailUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Ignore
    @Test
    public void test() throws MessagingException, IOException {
        try (GmailEmailUtil gmail = new GmailEmailUtil()) {
            gmail.setSendMailAuth(TS.params().getJiraUserName(), TS.params().getJiraPassword());
            TS.log().info(gmail.connect().getMsgCount());
            Message msg = gmail.getMsgBySubject(TS.params().getJiraUserName(), null);
            TS.log().info(gmail.getMsgBody(msg, "multipart/ALTERNATIVE;*"));
        }
    }
}