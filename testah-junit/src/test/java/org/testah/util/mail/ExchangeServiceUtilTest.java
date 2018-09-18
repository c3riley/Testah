package org.testah.util.mail;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import microsoft.exchange.webservices.data.core.enumeration.misc.XmlNamespace;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testah.TS;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExchangeServiceUtilTest {

    @Test
    public void testGetMessage() throws Exception {

        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        List<EmailMessage> messages = getEmails(exchange, 5, 0);

        doReturn(messages).when(exchange).getAllMessages();
        doCallRealMethod().when(exchange).getMsgByBccEmail(any());
        doCallRealMethod().when(exchange).getMessages(any());

        Assert.assertEquals(messages.get(1), exchange.getMsgByToEmail("Test1to1@nullmailer.com").get(0));
        Assert.assertEquals(0, exchange.getMsgByToEmail("TestMissing@nullmailer.com").size());

        Assert.assertEquals(messages.get(2), exchange.getMsgByCcEmail("Test2cc1@nullmailer.com").get(0));
        Assert.assertEquals(0, exchange.getMsgByCcEmail("Testbcc2@nullmailer.com").size());

        Assert.assertEquals(messages.get(3), exchange.getMsgByBccEmail("Test3bcc0@nullmailer.com").get(0));
        Assert.assertEquals(0, exchange.getMsgByBccEmail("TestMissing@nullmailer.com").size());

        Assert.assertEquals(messages.get(4), exchange.getMsgBySubject("this is a test 4").get(0));
        Assert.assertEquals(0, exchange.getMsgBySubject("NotFound").size());

        Assert.assertEquals(messages.get(0), exchange.getMsgByIndex(0));

        Assert.assertEquals(true, exchange.getAttachmentFiles(messages.get(0)).isEmpty());

    }

    private List<EmailMessage> getEmails(final ExchangeMailTest exchange, final int number,
                                         final int numOfAttachments) throws Exception {
        List<EmailMessage> messages = new ArrayList<>();
        for (int ctr = 0; ctr < number; ctr++) {
            final EmailMessage message = mock(EmailMessage.class);
            doReturn("this is a test " + ctr).when(message).getSubject();
            doReturn(getEmails(ctr + "to", 3)).when(message).getToRecipients();
            doReturn(getEmails(ctr + "cc", 2)).when(message).getCcRecipients();
            doReturn(getEmails(ctr + "bcc", 1)).when(message).getBccRecipients();
            doReturn(getInternetMessageHeaderCollection()).when(message).getInternetMessageHeaders();
            doReturn(getEmails(ctr + "from", 1).iterator().next()).when(message).getFrom();

            doReturn(getAttachmentCollection(numOfAttachments)).when(message).getAttachments();

            messages.add(message);
        }
        return messages;
    }

    private EmailAddressCollection getEmails(final String suffix, final int number) {
        EmailAddressCollection addresses = new EmailAddressCollection();
        String name;
        for (int ctr = 0; ctr < number; ctr++) {
            name = "Test" + suffix + ctr;
            addresses.add(name, name + "@nullmailer.com");
        }
        return addresses;
    }

    private InternetMessageHeaderCollection getInternetMessageHeaderCollection() {
        Collection<InternetMessageHeader> internetMessageHeaderList = new ArrayList<>();
        for (int ctr = 0; ctr < 5; ctr++) {
            internetMessageHeaderList.add(getInternetMessageHeader(ctr));
        }
        InternetMessageHeaderCollection internetMessageHeaderCollection = spy(new InternetMessageHeaderCollection());
        doReturn(internetMessageHeaderList.iterator()).when(internetMessageHeaderCollection).iterator();
        return internetMessageHeaderCollection;
    }

    @SuppressFBWarnings
    private AttachmentCollection getAttachmentCollection(final int numOfAttachments) throws Exception {
        Collection<Attachment> attachments = new ArrayList<>();
        if (numOfAttachments > 0) {
            File outputDir = new File(TS.params().getOutput());
            for (int ctr = 0; ctr < numOfAttachments; ctr++) {
                FileAttachment attachment = mock(FileAttachment.class);
                File file = File.createTempFile("_mailTestAttachmentFile" + 1, ".txt", outputDir);
                FileUtils.writeStringToFile(file, "This is a test " + ctr, Charset.forName("UTF-8"));
                doReturn(file.getAbsolutePath()).when(attachment).getName();

                doAnswer(new Answer() {
                    public Object answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        try {
                            FileUtils.writeStringToFile(new File(args[0].toString()), "This is a test ", Charset.forName("UTF-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Mock mock = invocation.getMock();
                        return null;
                    }
                }).when(attachment).load(anyString());
                attachments.add(attachment);
            }
        }

        AttachmentCollection attachmentCollection = spy(new AttachmentCollection());
        doReturn(attachments.iterator()).when(attachmentCollection).iterator();
        return attachmentCollection;
    }

    @SuppressFBWarnings
    private InternetMessageHeader getInternetMessageHeader(int suffix) {
        InternetMessageHeader internetMessageHeader = mock(InternetMessageHeader.class);
        doReturn("header" + suffix).when(internetMessageHeader).getName();
        doReturn("value1" + suffix).when(internetMessageHeader).getValue();
        doReturn(XmlNamespace.Messages).when(internetMessageHeader).getNamespace();
        return internetMessageHeader;
    }

    @Test
    public void testGetMessageWithAttachmentsAsStrings() throws Exception {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        List<EmailMessage> messages = getEmails(exchange, 5, 3);

        doReturn(messages).when(exchange).getAllMessages();
        doCallRealMethod().when(exchange).getMsgByBccEmail(any());
        doCallRealMethod().when(exchange).getMessages(any());

        List<String> attachmentFiles = exchange.getAttachmentAsStrings(messages);

        Assert.assertEquals(15, attachmentFiles.size());

        for (String attachmentString : attachmentFiles) {
            Assert.assertFalse(attachmentString.isEmpty());
            TS.log().debug(attachmentString);
            Assert.assertEquals(15L, attachmentString.length());
            Assert.assertEquals("This is a test ", attachmentString);
        }

    }

    @Test
    public void testGetMessageWithAttachments() throws Exception {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        List<EmailMessage> messages = getEmails(exchange, 5, 3);

        doReturn(messages).when(exchange).getAllMessages();
        doCallRealMethod().when(exchange).getMsgByBccEmail(any());
        doCallRealMethod().when(exchange).getMessages(any());

        List<File> attachmentFiles = exchange.getAttachmentFiles(messages.get(0));

        Assert.assertEquals(3, attachmentFiles.size());

        for (File attachmentFile : attachmentFiles) {
            Assert.assertTrue(attachmentFile.exists());
            Assert.assertEquals(15L, attachmentFile.length());
        }

        verify(exchange, Mockito.times(3)).deleteOnExit(any(), eq(false));
    }

    @Test
    public void testGetMessageWithAttachmentsWithDeleteOnExit() throws Exception {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        List<EmailMessage> messages = getEmails(exchange, 5, 3);

        doReturn(messages).when(exchange).getAllMessages();
        doCallRealMethod().when(exchange).getMsgByBccEmail(any());
        doCallRealMethod().when(exchange).getMessages(any());

        List<File> attachmentFiles = exchange.getAttachmentFiles(messages.get(0), true);

        Assert.assertEquals(3, attachmentFiles.size());

        for (File attachmentFile : attachmentFiles) {
            Assert.assertTrue(attachmentFile.exists());
            Assert.assertEquals(15L, attachmentFile.length());
        }

        verify(exchange, Mockito.times(3)).deleteOnExit(any(), eq(true));
    }

    @Test
    public void testGetMessageWithAttachmentsWithDeleteOnExitFalse() throws Exception {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        List<EmailMessage> messages = getEmails(exchange, 5, 3);

        doReturn(messages).when(exchange).getAllMessages();
        doCallRealMethod().when(exchange).getMsgByBccEmail(any());
        doCallRealMethod().when(exchange).getMessages(any());

        List<File> attachmentFiles = exchange.getAttachmentFiles(messages.get(0), false);

        Assert.assertEquals(3, attachmentFiles.size());

        for (File attachmentFile : attachmentFiles) {
            Assert.assertTrue(attachmentFile.exists());
            Assert.assertEquals(15L, attachmentFile.length());
        }

        verify(exchange, Mockito.times(3)).deleteOnExit(any(), eq(false));
    }

    @Test
    public void testGetEmailDto() throws Exception {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        doReturn("this is html").when(exchange).getMsgBodyText(any());
        doReturn("<h1>this is html</h1>").when(exchange).getMsgBodyHtml(any());
        doReturn("<h1>this is html with null</h1>").when(exchange).getMsgBody(any(), eq(null));

        List<EmailMessage> messages = getEmails(exchange, 1, 0);
        exchange.getEmailDto(messages.get(0));
    }

    @Test
    public void testAuth() {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        final WebCredentials auth = exchange.setAuth().getAuth(true);

        Assert.assertEquals(auth, exchange.getAuth(true));
        Assert.assertEquals(auth, exchange.getAuth(false));

        Assert.assertEquals(TS.params().getEmailUserName(), auth.getUser());
        Assert.assertEquals(TS.params().getEmailPassword(), auth.getPwd());
        Assert.assertEquals(TS.params().getEmailDomain(), auth.getDomain());

        Assert.assertEquals(TS.params().getEmailUserName(), exchange.getUserName());
        Assert.assertEquals(TS.params().getEmailPassword(), exchange.getPassword());
        Assert.assertEquals(TS.params().getEmailDomain(), exchange.getDomain());

        final WebCredentials auth1 = exchange.setAuth("name1", "password1", "domain1").getAuth();

        Assert.assertEquals("name1", auth1.getUser());
        Assert.assertEquals("password1", auth1.getPwd());
        Assert.assertEquals("domain1", auth1.getDomain());

        Assert.assertEquals("name1", exchange.getUserName());
        Assert.assertEquals("password1", exchange.getPassword());
        Assert.assertEquals("domain1", exchange.getDomain());

    }

    @Test
    public void testGettersSetters() {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());

        Assert.assertEquals("default TimeToPauseBetweenPoll", 10000L, exchange.getTimeToPauseBetweenPoll());
        exchange.setTimeToPauseBetweenPoll(1L);
        Assert.assertEquals("setTimeToPauseBetweenPoll", 1L, exchange.getTimeToPauseBetweenPoll());

        exchange.setTimeToPauseBetweenPollInSeconds(1);
        Assert.assertEquals("setTimeToPauseBetweenPoll", 1000L, exchange.getTimeToPauseBetweenPoll());

        Assert.assertEquals("default TimeToPoll", 300000L, exchange.getTimeToPoll());
        exchange.setTimeToPoll(23L);
        Assert.assertEquals("setTimeToPoll", 23L, exchange.getTimeToPoll());

        exchange.setTimeToPollInMinutes(1);
        Assert.assertEquals("setTimeToPoll", 60000, exchange.getTimeToPoll());

        Assert.assertEquals("default MaxNumberOfMessages", exchange.MAX_NUMBER_OF_MESSAGES,
                exchange.getMaxNumberOfMessages());
        exchange.setMaxNumberOfMessages(1);
        Assert.assertEquals("getMaxNumberOfMessages", 1, exchange.getMaxNumberOfMessages());

        Assert.assertEquals("default debug", true,
                exchange.isDebug());
        exchange.setDebug(false);
        Assert.assertEquals("isDebug", false, exchange.isDebug());

    }

    @Test(expected = RuntimeException.class)
    public void testAuthIfNull() {
        ExchangeMailTest exchange = spy(new ExchangeMailTest());
        exchange.setAuth(null);
        exchange.getAuth(true);
    }

    @SuppressWarnings("checkstyle:lineWrappingIndentation")
    @Test
    public void test() throws Exception {
        Assume.assumeTrue("If password is not found do not run the test", !StringUtils.isEmpty(TS.params().getEmailPassword()));
        try (ExchangeMailTest exchange = new ExchangeMailTest()) {
            exchange.setMaxNumberOfMessages(100).connect()
                    .getMsgByFromEmail(TS.params().getEmailUserName()).stream().forEach(message -> {
                try {
                    TS.util().toJsonPrint(exchange.getEmailDto(message));
                    TS.log().info(message.getSubject());
                    TS.log().info(exchange.getMsgBody(message, "HTML"));
                    TS.log().info(exchange.getMsgBody(message, "Text"));
                    exchange.getAttachmentFiles(message).stream().forEach(file -> {
                        TS.log().info(file.getAbsolutePath());
                        try {
                            TS.log().info(FileUtils.readFileToString(file, Charset.forName("UTF-8")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    TS.log().error(e);
                }
            });
        }
    }

}


