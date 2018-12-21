package org.testah.util.mail;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GmailEmailUtilTest {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSimpleConstructor() {
        try (GmailEmailUtil gmail = new GmailEmailUtil()) {
            Properties props = gmail.getSendMailProps();
            assertThat(props.get("mail.smtp.auth"),equalTo("true"));
            assertThat(props.get("mail.smtp.starttls.enable"),equalTo("true"));
            assertThat(props.get("mail.smtp.host"),equalTo("smtp.gmail.com"));
            assertThat(props.get("mail.smtp.port"),equalTo("587"));
        }
    }


    @Test
    public void test() throws MessagingException, Exception {
        Assume.assumeTrue("If password is not found do not run the test", !StringUtils.isEmpty(TS.params().getEmailPassword()));
        try (GmailEmailUtil gmail = new GmailEmailUtil()) {
            Message message = gmail.connect().getAllMessages().get(0);
            TS.log().info(gmail.getMsgBodyHtml(message));
            TS.log().info(gmail.getMsgBodyHtml(message));
            TS.log().info(gmail.getMsgBody(message, null));
        }
    }
}