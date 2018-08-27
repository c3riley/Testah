package org.testah.util.mail;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.testah.TS;
import org.testah.framework.dto.StepAction;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The type Abstract email util.
 *
 * @param <T> the type parameter
 * @param <M> the type parameter
 * @param <A> the type parameter
 * @param <F> the type parameter
 * @param <H> the type parameter
 */
public abstract class AbstractEmailUtil<T, M, A, F, H> implements Closeable {

    private static final String DEFAULT_EMAIL_CONTENT_TYPE = "text/html";
    private static final int DEFAULT_EXPECTED_FOUND_COUNT = 1;
    private static final long DEFAULT_POLL_PAUSE_ITERATION = 5000L;

    private long timeToPoll = TimeUnit.MINUTES.toMillis(5);
    private long timeToPauseBetweenPoll = TimeUnit.SECONDS.toMillis(10);
    private String domain;
    private String mailServerAddress;
    private boolean debug = true;
    /**
     * The Folder.
     */
    protected F folder;
    /**
     * The Auth.
     */
    protected A auth;

    /**
     * Instantiates a new Abstract email util.
     */
    public AbstractEmailUtil() {

    }

    /**
     * Instantiates a new Abstract email util.
     *
     * @param mailServerAddress the mail server address
     */
    public AbstractEmailUtil(final String mailServerAddress) {
        this.mailServerAddress = mailServerAddress;
    }

    /**
     * Connect t.
     *
     * @return the t
     */
    public abstract T connect();

    /**
     * Gets all messages.
     *
     * @return the all messages
     * @throws Exception the exception
     */
    public abstract List<M> getAllMessages() throws Exception;

    /**
     * Gets msg by subject filter.
     *
     * @param subject the subject
     * @return the msg by subject filter
     * @throws Exception the exception
     */
    public abstract EmailMessageFilter<M> getMsgBySubjectFilter(final String subject) throws Exception;

    /**
     * Gets msg by to email filter.
     *
     * @param toAddress the to address
     * @return the msg by to email filter
     * @throws Exception the exception
     */
    public abstract EmailMessageFilter<M> getMsgByToEmailFilter(final String toAddress) throws Exception;

    /**
     * Gets msg by cc email filter.
     *
     * @param ccAddress the cc address
     * @return the msg by cc email filter
     * @throws Exception the exception
     */
    public abstract EmailMessageFilter<M> getMsgByCcEmailFilter(final String ccAddress) throws Exception;

    /**
     * Gets msg by bcc email filter.
     *
     * @param bccAddress the bcc address
     * @return the msg by bcc email filter
     * @throws Exception the exception
     */
    public abstract EmailMessageFilter<M> getMsgByBccEmailFilter(final String bccAddress) throws Exception;

    /**
     * Gets msg by from email filter.
     *
     * @param fromAddress the from address
     * @return the msg by from email filter
     * @throws Exception the exception
     */
    public abstract EmailMessageFilter<M> getMsgByFromEmailFilter(final String fromAddress) throws Exception;

    /**
     * Gets msg by subject.
     *
     * @param subject the subject
     * @return the msg by subject
     * @throws Exception the exception
     */
    public List<M> getMsgBySubject(final String subject) throws Exception {
        return getMessages(getMsgBySubjectFilter(subject));
    }

    /**
     * Gets msg by to email.
     *
     * @param toAddress the to address
     * @return the msg by to email
     * @throws Exception the exception
     */
    public List<M> getMsgByToEmail(final String toAddress) throws Exception {
        return getMessages(getMsgByToEmailFilter(toAddress));
    }

    /**
     * Gets msg by cc email.
     *
     * @param ccAddress the cc address
     * @return the msg by cc email
     * @throws Exception the exception
     */
    public List<M> getMsgByCcEmail(final String ccAddress) throws Exception {
        return getMessages(getMsgByCcEmailFilter(ccAddress));
    }

    /**
     * Gets msg by bcc email.
     *
     * @param bccAddress the bcc address
     * @return the msg by bcc email
     * @throws Exception the exception
     */
    public List<M> getMsgByBccEmail(final String bccAddress) throws Exception {
        return getMessages(getMsgByBccEmailFilter(bccAddress));
    }

    /**
     * Gets msg by from email.
     *
     * @param fromAddress the from address
     * @return the msg by from email
     * @throws Exception the exception
     */
    public List<M> getMsgByFromEmail(final String fromAddress) throws Exception {
        return getMessages(getMsgByFromEmailFilter(fromAddress));
    }


    /**
     * Gets msg by index.
     *
     * @param index the index
     * @return the msg by index
     * @throws Exception the exception
     */
    public M getMsgByIndex(final int index) throws Exception {
        List<M> messages = getAllMessages();
        if (messages.size() >= index) {
            return messages.get(index);
        }
        TS.log().warn("No message found with index of " + index);
        return null;
    }

    /**
     * Gets messages.
     *
     * @param emailFilters the email filters
     * @return the messages
     */
    public List<M> getMessages(final EmailMessageFilter<M>... emailFilters) {
        final List<M> emailList = new ArrayList<>();
        TS.log().info("Looking for messages in " + getFolderName() + " for user: " + getUserName());
        try {
            final List<M> messages = getAllMessages();
            if (messages != null && !messages.isEmpty()) {
                emailList.addAll(messages.parallelStream().filter(message -> {
                    if (emailFilters != null) {
                        for (final EmailMessageFilter<M> emailFilter : emailFilters) {
                            if (!emailFilter.isMatch(message)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }).collect(Collectors.toList()));
                if (!emailList.isEmpty()) {
                    TS.addStepAction(StepAction.createInfo("Found Message", String.valueOf(emailList.size())));
                }
            }
        } catch (Exception e) {
            TS.log().warn("Issue with getting email", e);
        }
        return emailList;
    }

    /**
     * Gets messages with poll.
     *
     * @return the messages with poll
     */
    public List<M> getMessagesWithPoll() {
        return getMessagesWithPoll(null);
    }

    /**
     * Gets messages with poll.
     *
     * @param emailFilter the email filter
     * @return the messages with poll
     */
    public List<M> getMessagesWithPoll(final EmailMessageFilter<M>... emailFilter) {
        return getMessagesWithPoll(DEFAULT_EXPECTED_FOUND_COUNT, emailFilter);
    }

    /**
     * Gets messages with poll.
     *
     * @param expectedMatchCount the expected match count
     * @param emailFilter        the email filter
     * @return the messages with poll
     */
    public List<M> getMessagesWithPoll(final int expectedMatchCount, final EmailMessageFilter<M>... emailFilter) {
        return getMessagesWithPoll(DEFAULT_EXPECTED_FOUND_COUNT, true, emailFilter);
    }

    /**
     * Gets messages with poll.
     *
     * @param expectedMatchCount the expected match count
     * @param autoFail           the auto fail
     * @param emailFilter        the email filter
     * @return the messages with poll
     */
    public List<M> getMessagesWithPoll(final int expectedMatchCount, final boolean autoFail, final EmailMessageFilter<M>... emailFilter) {
        TS.log().info("Looking for message with polling");
        final long timeToLoop = timeToPoll / timeToPauseBetweenPoll;
        List<M> messages = new ArrayList<M>();
        for (int pollIndex = 0; pollIndex < timeToLoop; pollIndex++) {
            messages = getMessages(emailFilter);
            if (messages.size() >= expectedMatchCount) {
                return messages;
            } else if (!messages.isEmpty()) {
                TS.log().debug("Found " + messages.size() + " Messages but waiting till found: " + expectedMatchCount);
            }
            TS.util().pause(DEFAULT_POLL_PAUSE_ITERATION, "polling for message(s) to arrive", pollIndex);
        }
        if (autoFail) {
            TS.asserts().fail("Giving Up on polling, Found " + messages.size()
                    + " Messages but waiting till found: " + expectedMatchCount);
        }
        return new ArrayList<M>();
    }


    /**
     * Gets attachment as strings.
     *
     * @param messages the messages
     * @return the attachment as strings
     * @throws Exception the exception
     */
    public List<String> getAttachmentAsStrings(final List<M> messages) throws Exception {
        List<String> attachments = new ArrayList<>();
        getAttachmentFiles(messages, true).stream().forEach(file -> {
            try {
                attachments.add(FileUtils.readFileToString(file, Charset.forName(Charsets.UTF_8.name())));
            } catch (IOException e) {
                TS.log().debug("issue with attachment for message:" + file.getAbsolutePath(), e);
            }
        });
        return attachments;
    }


    /**
     * Gets attachment files.
     *
     * @param messages the messages
     * @return the attachment files
     * @throws Exception the exception
     */
    public List<File> getAttachmentFiles(final List<M> messages) throws Exception {
        return getAttachmentFiles(messages, false);
    }

    /**
     * Gets attachment files.
     *
     * @param messages     the messages
     * @param deleteOnExit the delete on exit
     * @return the attachment files
     */
    public List<File> getAttachmentFiles(final List<M> messages, final boolean deleteOnExit) {
        List<File> attachments = new ArrayList<>();
        if (messages != null && !messages.isEmpty()) {
            messages.parallelStream().forEach(message -> {
                try {
                    attachments.addAll(getAttachmentFiles(message, deleteOnExit));
                } catch (Exception e) {
                    TS.log().debug("issue with attachment for message:" + message, e);
                }
            });
        }
        return attachments;
    }

    /**
     * Gets attachment files.
     *
     * @param message the message
     * @return the attachment files
     * @throws Exception the exception
     */
    public List<File> getAttachmentFiles(final M message) throws Exception {
        return getAttachmentFiles(message, false);
    }

    /**
     * Delete on exit.
     * This is to allow unit test to check if its set correctly
     *
     * @param file               the file
     * @param shouldDeleteOnExit the should delete on exit
     */
    protected void deleteOnExit(final File file, final boolean shouldDeleteOnExit) {
        if (shouldDeleteOnExit) {
            file.deleteOnExit();
        }
        return;
    }

    /**
     * Gets attachment files.
     *
     * @param message      the message
     * @param deleteOnExit the delete on exit
     * @return the attachment files
     * @throws Exception the exception
     */
    public abstract List<File> getAttachmentFiles(final M message, final boolean deleteOnExit) throws Exception;

    /**
     * Gets msg body.
     *
     * @param message the message
     * @return the msg body
     * @throws Exception the exception
     */
    public String getMsgBody(final M message) throws Exception {
        return getMsgBody(message, DEFAULT_EMAIL_CONTENT_TYPE);
    }

    /**
     * Gets msg body text.
     *
     * @param message the message
     * @return the msg body text
     * @throws Exception the exception
     */
    public abstract String getMsgBodyText(final M message) throws Exception;

    /**
     * Gets msg body html.
     *
     * @param message the message
     * @return the msg body html
     * @throws Exception the exception
     */
    public abstract String getMsgBodyHtml(final M message) throws Exception;

    /**
     * Gets msg body.
     *
     * @param message     the message
     * @param contentType the content type
     * @return the msg body
     * @throws Exception the exception
     */
    public abstract String getMsgBody(final M message, final String contentType) throws Exception;

    /**
     * Delete msg t.
     *
     * @param message the message
     * @return the t
     * @throws Exception the exception
     */
    public abstract T deleteMsg(final M message) throws Exception;

    /**
     * Clear inbox t.
     *
     * @return the t
     * @throws Exception the exception
     */
    public abstract T clearInbox() throws Exception;

    /**
     * Send message t.
     *
     * @param sendMailDto the send mail dto
     * @return the t
     */
    public abstract T sendMessage(final SendMailDto sendMailDto);

    /**
     * Sets auth.
     *
     * @return the auth
     */
    public T setAuth() {
        return setAuth(TS.params().getEmailUserName(), TS.params().getEmailPassword(), TS.params().getEmailDomain());
    }

    public T setAuth(A auth) {
        this.auth = auth;
        return (T) this;
    }

    /**
     * Sets auth.
     *
     * @param userName the user name
     * @param password the password
     * @param domain   the domain
     * @return the auth
     */
    public abstract T setAuth(final String userName, final String password, final String domain);

    /**
     * Gets auth.
     *
     * @param onNullThrowError the on null throw error
     * @return the auth
     */
    protected A getAuth(final boolean onNullThrowError) {
        if (auth == null) {
            throw new RuntimeException("Auth is not set yet, please call setAuth!");
        }
        return getAuth();
    }

    /**
     * Gets auth.
     *
     * @return the auth
     */
    protected A getAuth() {
        return auth;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    protected abstract String getUserName();

    /**
     * Gets password.
     *
     * @return the password
     */
    protected abstract String getPassword();


    public abstract void close();

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
    public T setTimeToPoll(long timeToPoll) {
        this.timeToPoll = timeToPoll;
        return (T) this;
    }

    /**
     * Sets time to poll in minutes.
     *
     * @param timeToPollInMinutes the time to poll in minutes
     * @return the time to poll in minutes
     */
    public T setTimeToPollInMinutes(int timeToPollInMinutes) {
        this.timeToPoll = TimeUnit.MINUTES.toMillis(timeToPollInMinutes);
        return (T) this;
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
    public T setTimeToPauseBetweenPoll(long timeToPauseBetweenPoll) {
        this.timeToPauseBetweenPoll = timeToPauseBetweenPoll;
        return (T) this;
    }

    /**
     * Sets time to pause between poll in seconds.
     *
     * @param timeToPollInSeconds the time to poll in seconds
     * @return the time to pause between poll in seconds
     */
    public T setTimeToPauseBetweenPollInSeconds(int timeToPollInSeconds) {
        this.timeToPauseBetweenPoll = TimeUnit.SECONDS.toMillis(timeToPollInSeconds);
        return (T) this;
    }

    /**
     * Gets domain.
     *
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets domain.
     *
     * @param domain the domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Gets mail server address.
     *
     * @return the mail server address
     */
    public String getMailServerAddress() {
        return mailServerAddress;
    }

    /**
     * Sets mail server address.
     *
     * @param mailServerAddress the mail server address
     */
    public void setMailServerAddress(String mailServerAddress) {
        this.mailServerAddress = mailServerAddress;
    }

    /**
     * Is debug boolean.
     *
     * @return the boolean
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Sets debug.
     *
     * @param debug the debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Gets folder name.
     *
     * @return the folder name
     */
    public abstract String getFolderName();

    /**
     * Gets folder.
     *
     * @return the folder
     */
    public F getFolder() {
        return folder;
    }

    /**
     * Sets folder.
     *
     * @param folder the folder
     * @return the folder
     */
    public T setFolder(F folder) {
        this.folder = folder;
        return (T) this;
    }

    /**
     * Gets message headers as map.
     *
     * @param message the message
     * @return the message headers as map
     */
    public abstract HashMap<String, List<String>> getMessageHeadersAsMap(final M message);

    /**
     * Gets message headers.
     *
     * @param message the message
     * @return the message headers
     */
    public abstract List<H> getMessageHeaders(final M message);

    /**
     * Gets email dto.
     *
     * @param message the message
     * @return the email dto
     * @throws Exception the exception
     */
    public EmailDto<M> getEmailDto(final M message) throws Exception {
        final EmailDto<M> emailDto = new EmailDto();
        emailDto.setAssertments(getAttachmentFiles(message));
        emailDto.setBodyHtml(getMsgBodyHtml(message));
        emailDto.setBodyText(getMsgBodyText(message));
        emailDto.setHeaders(getMessageHeadersAsMap(message));
        emailDto.setMessage(message);
        emailDto.setBody(getMsgBody(message, null));
        return fillEmailDto(message, emailDto);
    }

    /**
     * Fill email dto email dto.
     *
     * @param message  the message
     * @param emailDto the email dto
     * @return the email dto
     * @throws Exception the exception
     */
    protected abstract EmailDto<M> fillEmailDto(final M message, final EmailDto<M> emailDto) throws Exception;

}
