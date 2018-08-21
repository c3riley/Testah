package org.testah.util.mail;

import org.apache.commons.lang.StringUtils;
import org.testah.TS;
import org.testah.framework.dto.StepAction;

import java.io.Closeable;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The type Email util.
 */
public class EmailUtil implements Closeable {

    private static final String DEFAULT_MAIL_PROTOCAL = "imaps";
    private static final String DEFAULT_MAILBOX_FOLDER = "INBOX";
    private static final String DEFAULT_EMAIL_CONTENT_TYPE = "text/html";
    private Session session = null;
    private Store store = null;
    private Folder folder = null;
    private String mailDomain = null;
    private String mailStoreProtocol = DEFAULT_MAIL_PROTOCAL;

    private long timeToPoll = TimeUnit.MINUTES.toMillis(5);
    private long timeToPauseBetweenPoll = TimeUnit.SECONDS.toMillis(10);

    private Properties sendMailProps = null;
    private PasswordAuthentication sendMailAuth;

    private Message lastMessageFound = null;

    /**
     * Instantiates a new Email util.
     */
    public EmailUtil() {

    }

    /**
     * Connect email util.
     *
     * @return the email util
     */
    public EmailUtil connect() {
        return connect(DEFAULT_MAILBOX_FOLDER);
    }

    /**
     * Connect email util.
     *
     * @param mailBoxFolder the mail box folder
     * @return the email util
     */
    public EmailUtil connect(final String mailBoxFolder) {
        resetLastMessageFound();
        final Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", getMailStoreProtocol());
        final String emailUserName = getSendMailAuth(true).getUserName();
        try {
            session = Session.getDefaultInstance(props, null);
            store = session.getStore(getMailStoreProtocol());
            store.connect(getMailDomain(), emailUserName, getSendMailAuth().getPassword());
            setMailFromFolder(mailBoxFolder);
            TS.addStepAction(StepAction.createInfo("Connected to Mailbox", "email: " + emailUserName));
        } catch (final Exception e) {
            throw new RuntimeException("Conneting to Mail Domain: " + mailDomain + " with user: "
                    + emailUserName, e);
        }
        return this;
    }

    /**
     * Sets mail from folder.
     *
     * @param folderName the folder name
     * @return the mail from folder
     * @throws MessagingException the messaging exception
     */
    @SuppressWarnings("UnusedReturnValue")
    public EmailUtil setMailFromFolder(final String folderName) throws MessagingException {
        folder = store.getFolder(folderName);
        folder.open(Folder.READ_WRITE);
        TS.addStepAction(StepAction.createInfo("Opened mailbox", folderName));
        return this;
    }

    /**
     * Gets msg count.
     *
     * @return the msg count
     * @throws MessagingException the messaging exception
     */
    public int getMsgCount() throws MessagingException {
        return folder.getMessageCount();
    }

    /**
     * Gets new msg count.
     *
     * @return the new msg count
     * @throws MessagingException the messaging exception
     */
    public int getNewMsgCount() throws MessagingException {
        return folder.getNewMessageCount();
    }

    /**
     * Gets all messages.
     *
     * @return the all messages
     * @throws MessagingException the messaging exception
     */
    public List<Message> getAllMessages() throws MessagingException {
        return Arrays.stream(folder.getMessages()).collect(Collectors.toList());
    }

    /**
     * Gets msg by subject.
     *
     * @param subject the subject
     * @return the msg by subject
     * @throws MessagingException the messaging exception
     */
    public Message getMsgBySubject(final String subject) throws MessagingException {
        return getMsgBySubject(null, subject);
    }

    /**
     * Gets msg by subject.
     *
     * @param toEmail the to email
     * @param subject the subject
     * @return the msg by subject
     * @throws MessagingException the messaging exception
     */
    public Message getMsgBySubject(final String toEmail, final String subject)
            throws MessagingException {

        resetLastMessageFound();

        final String infoMessage = (subject != null ? " SubjectLine[" + subject + "]" : "")
                + (toEmail != null ? " toEmail[" + toEmail + "]" : "");

        boolean found = false;
        TS.log().info("Looking for message with subject: " + infoMessage);
        long timeToLoop = timeToPoll / timeToPauseBetweenPoll;
        for (int pollIndex = 0; pollIndex < timeToLoop; pollIndex++) {
            for (final Message message : getAllMessages()) {
                if (!message.isExpunged()) {
                    if (subject != null) {
                        found = StringUtils.equals(message.getSubject(), subject);
                    }
                    if (toEmail != null && message.getRecipients(RecipientType.TO) != null) {
                        for (final Address email : message.getRecipients(RecipientType.TO)) {
                            if (StringUtils.equals(email.toString(), toEmail)) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) {
                        TS.addStepAction(StepAction.createInfo("Found Message", infoMessage));
                        setLastMessageFound(message);
                        return message;
                    }
                }
            }
            TS.util().pause(5000L, "polling for message to arrive", pollIndex);
        }
        TS.asserts().fail("No Found Message - " + infoMessage);
        return null;
    }

    /**
     * Gets msg by index.
     *
     * @param index the index
     * @return the msg by index
     * @throws MessagingException the messaging exception
     */
    public Message getMsgByIndex(final int index) throws MessagingException {
        resetLastMessageFound();
        Message message = folder.getMessage(index);
        setLastMessageFound(message);
        return message;
    }


    /**
     * Gets msg body.
     *
     * @return the msg body
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    public String getMsgBody() throws IOException, MessagingException {
        return getMsgBody(getLastMessageFound());
    }

    /**
     * Gets msg body.
     *
     * @param message the message
     * @return the msg body
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    public String getMsgBody(final Message message) throws IOException, MessagingException {
        return getMsgBody(message, DEFAULT_EMAIL_CONTENT_TYPE);
    }

    /**
     * Gets msg body.
     *
     * @param contentType the content type
     * @return the msg body
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    public String getMsgBody(final String contentType) throws IOException, MessagingException {
        return getMsgBody(getLastMessageFound(), contentType);
    }

    /**
     * Gets msg body.
     *
     * @param message     the message
     * @param contentType the content type
     * @return the msg body
     * @throws IOException        the io exception
     * @throws MessagingException the messaging exception
     */
    public String getMsgBody(final Message message, final String contentType) throws IOException, MessagingException {
        final Object content = message.getContent();
        final List<String> contentTypesFound = new ArrayList<>();
        if (content instanceof Multipart) {
            final Multipart multipart = (Multipart) content;
            for (int index = 0; index < multipart.getCount(); index++) {
                contentTypesFound.add(multipart.getContentType());
                if (multipart.getContentType().equals(contentType)
                        //allow to ignore content type check
                        || contentType == null
                        //allow for startsWith wild card for content type
                        || (StringUtils.endsWith(contentType, "*") && StringUtils.startsWith(multipart.getContentType(),
                        StringUtils.replace(contentType, "*", "")))) {
                    final BodyPart bodyPart = multipart.getBodyPart(index);
                    return bodyPart.getContent().toString();
                }
            }
        } else {
            return content.toString();
        }
        TS.log().warn("Unable to find email with contentType[" + contentType + "] - found contentTypes: " + contentTypesFound);
        return null;
    }

    /**
     * Delete msg email util.
     *
     * @param message the message
     * @return the email util
     * @throws MessagingException the messaging exception
     */
    public EmailUtil deleteMsg(final Message message) throws MessagingException {
        TS.addStepAction(StepAction.createInfo("Deleting Messag", message.getSubject()));
        message.setFlag(Flags.Flag.DELETED, true);
        folder.expunge();
        return this;
    }

    /**
     * Clear inbox email util.
     *
     * @return the email util
     * @throws MessagingException the messaging exception
     */
    public EmailUtil clearInbox() throws MessagingException {
        for (final Message m : folder.getMessages()) {
            m.setFlag(Flags.Flag.DELETED, true);
        }
        TS.addStepAction(StepAction.createInfo("Deleting all messages in folder:", folder.getFullName()));
        return this;
    }

    /**
     * Close the open items like folder and store.
     * This class should be used in a try(EmailUtil email = new EmailUtil()) {}
     * to ensure it closes any open connections.
     */
    public void close() {
        if (folder != null && folder.isOpen()) {
            try {
                folder.close(true);
            } catch (final MessagingException e) {
                TS.log().warn("Issue closing the folder mailbox connection", e);
            }
        }
        if (store != null && store.isConnected()) {
            try {
                store.close();
            } catch (final MessagingException e) {
                TS.log().warn("Issue closing the store mailbox connection", e);
            }
        }
        session = null;
    }

    /**
     * Gets send session.
     *
     * @return the send session
     */
    protected Session getSendSession() {
        return Session.getInstance(getSendMailProps(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return getSendMailAuth(true);
            }
        });
    }

    /**
     * Send message email util.
     *
     * @param sendMailDto the send mail dto
     * @return the email util
     */
    public EmailUtil sendMessage(final SendMailDto sendMailDto) {
        try {
            final Message message = new MimeMessage(getSendSession());
            message.setFrom(new InternetAddress(sendMailDto.getFrom()));

            message.setRecipients(Message.RecipientType.TO, addEmails(sendMailDto.getTo()));
            message.setRecipients(Message.RecipientType.CC, addEmails(sendMailDto.getCc()));
            message.setRecipients(RecipientType.BCC, addEmails(sendMailDto.getBcc()));

            if (sendMailDto.getSubject() != null) {
                message.setSubject(sendMailDto.getSubject());
            }

            if (sendMailDto.getBody() != null) {
                message.setContent(sendMailDto.getBody(), sendMailDto.getContent());
            }

            if (sendMailDto.getSentDate() != null) {
                message.setSentDate(sendMailDto.getSentDate());
            }

            Transport.send(message);

        } catch (final Exception issueWithMessage) {
            if (sendMailDto != null) {
                TS.util().toJsonPrint(sendMailDto);
            }
            throw new RuntimeException("Issue with sending message", issueWithMessage);
        }
        return this;
    }

    private InternetAddress[] addEmails(final String addressesString) throws AddressException {
        final List<InternetAddress> addresses = new ArrayList<>();
        if (!StringUtils.isEmpty(addressesString)) {
            for (final String address : addressesString.split(";")) {
                addresses.add(new InternetAddress(address));
            }
        }
        return addresses.toArray(new InternetAddress[0]);
    }

    /**
     * Gets mail domain.
     *
     * @return the mail domain
     */
    public String getMailDomain() {
        return mailDomain;
    }

    /**
     * Sets mail domain.
     *
     * @param mailDomain the mail domain
     * @return the mail domain
     */
    public EmailUtil setMailDomain(final String mailDomain) {
        this.mailDomain = mailDomain;
        this.mailStoreProtocol = DEFAULT_MAIL_PROTOCAL;
        return this;
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Sets session.
     *
     * @param session the session
     * @return the session
     */
    public EmailUtil setSession(Session session) {
        this.session = session;
        return this;
    }

    /**
     * Gets store.
     *
     * @return the store
     */
    public Store getStore() {
        return store;
    }

    /**
     * Sets store.
     *
     * @param store the store
     * @return the store
     */
    public EmailUtil setStore(Store store) {
        this.store = store;
        return this;
    }

    /**
     * Gets folder.
     *
     * @return the folder
     */
    public Folder getFolder() {
        return folder;
    }

    /**
     * Sets folder.
     *
     * @param folder the folder
     * @return the folder
     */
    public EmailUtil setFolder(Folder folder) {
        this.folder = folder;
        return this;
    }

    /**
     * Gets mail store protocol.
     *
     * @return the mail store protocol
     */
    public String getMailStoreProtocol() {
        return mailStoreProtocol;
    }

    /**
     * Sets mail store protocol.
     *
     * @param mailStoreProtocol the mail store protocol
     * @return the mail store protocol
     */
    public EmailUtil setMailStoreProtocol(String mailStoreProtocol) {
        this.mailStoreProtocol = mailStoreProtocol;
        return this;
    }

    /**
     * Gets time to poll.
     *
     * @return the time to poll
     */
    public long getTimeToPoll() {
        return timeToPoll;
    }

    /**
     * Sets time to poll.
     *
     * @param timeToPoll the time to poll
     * @return the time to poll
     */
    public EmailUtil setTimeToPoll(long timeToPoll) {
        this.timeToPoll = timeToPoll;
        return this;
    }

    /**
     * Sets time to poll in minutes.
     *
     * @param timeToPollInMinutes the time to poll in minutes
     * @return the time to poll in minutes
     */
    public EmailUtil setTimeToPollInMinutes(int timeToPollInMinutes) {
        this.timeToPoll = TimeUnit.MINUTES.toMillis(timeToPollInMinutes);
        return this;
    }

    /**
     * Gets time to pause between poll.
     *
     * @return the time to pause between poll
     */
    public long getTimeToPauseBetweenPoll() {
        return timeToPauseBetweenPoll;
    }

    /**
     * Sets time to pause between poll.
     *
     * @param timeToPauseBetweenPoll the time to pause between poll
     * @return the time to pause between poll
     */
    public EmailUtil setTimeToPauseBetweenPoll(long timeToPauseBetweenPoll) {
        this.timeToPauseBetweenPoll = timeToPauseBetweenPoll;
        return this;
    }

    /**
     * Sets time to pause between poll in seconds.
     *
     * @param timeToPollInSeconds the time to poll in seconds
     * @return the time to pause between poll in seconds
     */
    public EmailUtil setTimeToPauseBetweenPollInSeconds(int timeToPollInSeconds) {
        this.timeToPauseBetweenPoll = TimeUnit.SECONDS.toMillis(timeToPollInSeconds);
        return this;
    }

    /**
     * Gets send mail props.
     *
     * @return the send mail props
     */
    public Properties getSendMailProps() {
        return sendMailProps;
    }

    /**
     * Sets send mail props.
     *
     * @param sendMailProps the send mail props
     * @return the send mail props
     */
    public EmailUtil setSendMailProps(final Properties sendMailProps) {
        this.sendMailProps = sendMailProps;
        return this;
    }

    /**
     * Add send mail prop email util.
     *
     * @param name  the name
     * @param value the value
     * @return the email util
     */
    public EmailUtil addSendMailProp(final String name, final String value) {
        getSendMailProps().setProperty(name, value);
        return this;
    }

    /**
     * Sets send mail auth.
     *
     * @param userName the user name
     * @param password the password
     * @return the send mail auth
     */
    public EmailUtil setSendMailAuth(final String userName, final String password) {
        sendMailAuth = new PasswordAuthentication(userName, password);
        return this;
    }

    /**
     * Sets send mail auth.
     *
     * @param sendMailAuth the send mail auth
     * @return the send mail auth
     */
    public EmailUtil setSendMailAuth(PasswordAuthentication sendMailAuth) {
        this.sendMailAuth = sendMailAuth;
        return this;
    }

    /**
     * Gets send mail auth.
     *
     * @return the send mail auth
     */
    public PasswordAuthentication getSendMailAuth() {
        return getSendMailAuth(false);
    }

    /**
     * Gets send mail auth.
     *
     * @param onNullThrowError the on null throw error
     * @return the send mail auth
     */
    public PasswordAuthentication getSendMailAuth(final boolean onNullThrowError) {
        if (sendMailAuth == null) {
            throw new RuntimeException("PasswordAuthentication is not set yet, please call setSendMailAuth!");
        }
        return sendMailAuth;
    }


    /**
     * Gets last message found.
     *
     * @return the last message found
     */
    public Message getLastMessageFound() {
        return lastMessageFound;
    }

    /**
     * Sets last message found.
     *
     * @param lastMessageFound the last message found
     */
    public void setLastMessageFound(Message lastMessageFound) {
        this.lastMessageFound = lastMessageFound;
    }

    /**
     * Reset last message found.
     */
    public void resetLastMessageFound() {
        this.lastMessageFound = null;
    }
}
