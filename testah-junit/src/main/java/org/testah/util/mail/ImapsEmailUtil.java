package org.testah.util.mail;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.testah.TS;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Imaps email util.
 */
public class ImapsEmailUtil extends AbstractEmailUtil<ImapsEmailUtil, Message, PasswordAuthentication, Folder, Header> {

    private static final String MAIL_PROTOCAL = "imaps";
    private static final String DEFAULT_MAILBOX_FOLDER = "INBOX";

    private Session session = null;
    private Store store = null;
    private Properties sendMailProps = null;
    private Properties imapProps = null;

    /**
     * Instantiates a new Imaps email util.
     */
    public ImapsEmailUtil() {
        this(null);
    }

    /**
     * Instantiates a new Imaps email util.
     *
     * @param mailServerAddress the mail server address
     */
    public ImapsEmailUtil(final String mailServerAddress) {
        super(mailServerAddress);
        imapProps = System.getProperties();
        imapProps.setProperty("mail.store.protocol", MAIL_PROTOCAL);
        imapProps.setProperty("mail.imap.ssl.enable", "true");
    }

    @Override
    public EmailMessageFilter<Message> getMsgBySubjectFilter(final String subject) throws Exception {
        return message -> {
            try {
                return StringUtils.equals(message.getSubject(), subject);
            } catch (MessagingException e) {
                TS.log().debug("Issue filtering message with subject: " + message, e);
            }
            return false;
        };
    }

    @Override
    public String getFolderName() {
        if (folder != null) {
            return folder.getName();
        }
        return null;
    }

    @Override
    protected String getUserName() {
        return getAuth(true).getUserName();
    }

    @Override
    public List<Message> getAllMessages() throws Exception {
        return Arrays.stream(getFolder().getMessages()).collect(Collectors.toList());
    }

    @Override
    public EmailMessageFilter<Message> getMsgByToEmailFilter(String toAddress) throws Exception {
        return getMsgByEmailFilter(toAddress, Message.RecipientType.TO);
    }

    @Override
    public EmailMessageFilter<Message> getMsgByCcEmailFilter(String ccAddress) throws Exception {
        return getMsgByEmailFilter(ccAddress, Message.RecipientType.CC);
    }

    @Override
    public EmailMessageFilter<Message> getMsgByBccEmailFilter(String bccAddress) throws Exception {
        return getMsgByEmailFilter(bccAddress, Message.RecipientType.BCC);
    }

    @Override
    public EmailMessageFilter<Message> getMsgByFromEmailFilter(String fromAddress) throws Exception {
        return message -> {
            try {
                for (final Address address : message.getFrom()) {
                    if (StringUtils.equals(fromAddress, address.toString())) {
                        return true;
                    }
                }
            } catch (MessagingException e) {
                TS.log().debug("Issue filtering message with address[" + fromAddress + "]: " + message, e);
            }
            return false;
        };
    }

    @Override
    public List<File> getAttachmentFiles(Message message, boolean deleteOnExit) throws Exception {
        List<File> attachmentFiles = new ArrayList<>();
        getAttachments(message).parallelStream().forEach(attachment -> {
            File attachmentFile = new File(TS.params().getOutput(), attachment.getName());
            if (deleteOnExit) {
                attachmentFile.deleteOnExit();
            }
            try {
                FileUtils.copyInputStreamToFile(attachment.getInputStream(), attachmentFile);
                attachmentFiles.add(attachmentFile);
            } catch (IOException e) {
                TS.log().warn("issue with getting attachment: " + attachment.getName(), e);
            }
        });
        return attachmentFiles;
    }

    @Override
    public String getMsgBody(final Message message, final String contentType) throws Exception {
        final Object content = message.getContent();
        if (content instanceof Multipart) {
            final Multipart multipart = (Multipart) content;
            for (int index = 0; index < multipart.getCount(); index++) {
                final BodyPart bodyPart = multipart.getBodyPart(index);
                if (contentType == null || bodyPart.isMimeType(contentType)) {
                    return bodyPart.getContent().toString();
                }
            }
            return getMultipartText(message);
        } else {
            return content.toString();
        }
    }

    @Override
    public ImapsEmailUtil deleteMsg(Message message) throws Exception {
        TS.step().action().createInfo("Deleting Messag", message.getSubject());
        message.setFlag(Flags.Flag.DELETED, true);
        folder.expunge();
        return this;
    }

    @Override
    public ImapsEmailUtil clearInbox() throws Exception {
        for (final Message message : folder.getMessages()) {
            message.setFlag(Flags.Flag.DELETED, true);
        }
        TS.step().action().createInfo("Deleting all messages in folder:", getFolderName());
        return this;
    }

    /**
     * Send message email util.
     *
     * @param sendMailDto the send mail dto
     * @return the email util
     */
    @Override
    public ImapsEmailUtil sendMessage(final SendMailDto sendMailDto) {
        final Session session = getSendSession();

        try {

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendMailDto.getFrom()));

            message.setRecipients(Message.RecipientType.TO, addEmails(sendMailDto.getTo()));
            message.setRecipients(Message.RecipientType.CC, addEmails(sendMailDto.getCc()));
            message.setRecipients(Message.RecipientType.BCC, addEmails(sendMailDto.getBcc()));

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

    @Override
    public ImapsEmailUtil setAuth(final String userName, final String password, final String domain) {
        this.auth = new PasswordAuthentication(userName, password);
        this.setDomain(domain);
        return this;
    }

    @Override
    protected String getPassword() {
        return getAuth(true).getPassword();
    }

    @Override
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

    @Override
    public List<Header> getMessageHeaders(Message message) {
        try {
            return Collections.list(message.getAllHeaders());
        } catch (MessagingException e) {
            TS.log().warn("Issue closing the store mailbox connection", e);
        }
        return new ArrayList<>();
    }

    @Override
    public String getMsgBodyHtml(Message message) throws Exception {
        return getMsgBody(message, "text/html");
    }

    @Override
    public String getMsgBodyText(Message message) throws Exception {
        return getMsgBody(message, "text/plain");
    }

    @Override
    public HashMap<String, List<String>> getMessageHeadersAsMap(Message message) {
        HashMap<String, List<String>> headers = new HashMap<>();
        getMessageHeaders(message).forEach(header -> {
            if (headers.get(header.getName()) == null) {
                headers.put(header.getName(), new ArrayList<>());
            }
            headers.get(header.getName()).add(header.getValue());
        });
        return headers;
    }

    @Override
    protected EmailDto<Message> fillEmailDto(final Message message, final EmailDto<Message> emailDto) throws Exception {
        emailDto.setSubject(message.getSubject());
        emailDto.setToAddresses(getAddressList(message, Message.RecipientType.TO));
        emailDto.setCcAddresses(getAddressList(message, Message.RecipientType.CC));
        emailDto.setBccAddresses(getAddressList(message, Message.RecipientType.BCC));
        emailDto.setFromAddress(StringUtils.join(message.getFrom(), ","));
        return emailDto;
    }

    @Override
    public ImapsEmailUtil connect() {
        return connect(DEFAULT_MAILBOX_FOLDER);
    }

    /**
     * Connect imaps email util.
     *
     * @param mailBoxFolder the mail box folder
     * @return the imaps email util
     */
    public ImapsEmailUtil connect(final String mailBoxFolder) {
        final String emailUserName = getUserName();
        try {
            session = Session.getDefaultInstance(getImapProps(), null);
            session.setDebug(isDebug());
            store = session.getStore(MAIL_PROTOCAL);
            store.connect(getMailServerAddress(), emailUserName, getPassword());
            setFolder(mailBoxFolder);
            TS.step().action().createInfo("Connected to Mailbox: " + getFolderName(), "email: " + emailUserName);
        } catch (final Exception e) {
            throw new RuntimeException("Conneting to Mail Domain: " + getMailServerAddress() + " with user: "
                    + emailUserName, e);
        }
        return this;
    }

    /**
     * Gets imap props.
     *
     * @return the imap props
     */
    public Properties getImapProps() {
        return imapProps;
    }

    /**
     * Sets imap props.
     *
     * @param imapProps the imap props
     */
    public void setImapProps(Properties imapProps) {
        this.imapProps = imapProps;
    }

    /**
     * Sets folder.
     *
     * @param folderName the folder name
     * @return the folder
     * @throws MessagingException the messaging exception
     */
    public ImapsEmailUtil setFolder(final String folderName) throws MessagingException {
        setFolder(store.getFolder(folderName));
        getFolder().open(Folder.READ_WRITE);
        TS.step().action().createInfo("Opened mailbox", getFolderName());
        return this;
    }

    private EmailMessageFilter<Message> getMsgByEmailFilter(String addressExpected, Message.RecipientType recipientType) throws Exception {
        return message -> {
            try {
                for (final Address address : message.getRecipients(recipientType)) {
                    if (StringUtils.equals(addressExpected, address.toString())) {
                        return true;
                    }
                }
            } catch (MessagingException e) {
                TS.log().debug("Issue filtering message with address[" + addressExpected + "]: " + message, e);
            }
            return false;
        };
    }

    private List<String> getAddressList(final Message message, Message.RecipientType recipientType) throws Exception {
        return Arrays.asList(message.getRecipients(recipientType)).stream().map(Address::toString).collect(Collectors.toList());
    }

    /**
     * Gets send session.
     *
     * @return the send session
     */
    protected Session getSendSession() {
        return Session.getInstance(getSendMailProps(), new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return getAuth(true);
            }
        });
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
    public ImapsEmailUtil setSendMailProps(final Properties sendMailProps) {
        this.sendMailProps = sendMailProps;
        return this;
    }

    /**
     * Gets attachments.
     *
     * @param message the message
     * @return the attachments
     * @throws Exception the exception
     */
    public List<DataSource> getAttachments(final Message message) throws Exception {
        MimeMessageParser parser = new MimeMessageParser((MimeMessage) message).parse();
        return parser.getAttachmentList();
    }

    /**
     * Get Multipart Test
     * Code is largely from https://javaee.github.io/javamail/FAQ#mainbody
     *
     * @param part Email part to check for text
     * @return return text found
     * @throws MessagingException Error with the message
     * @throws IOException        Error with the message
     */
    private String getMultipartText(final Part part) throws
            MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return (String) part.getContent();
        }
        if (part.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            final Multipart multipart = (Multipart) part.getContent();
            String text = null;
            for (int ctr = 0; ctr < multipart.getCount(); ctr++) {
                Part bodyPart = multipart.getBodyPart(ctr);
                if (bodyPart.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getMultipartText(bodyPart);
                    }
                    continue;
                } else if (bodyPart.isMimeType("text/html")) {
                    String content = getMultipartText(bodyPart);
                    if (content != null) {
                        return content;
                    }
                } else {
                    return getMultipartText(bodyPart);
                }
            }
            return text;
        } else if (part.isMimeType("multipart/*")) {
            final Multipart mp = (Multipart) part.getContent();
            for (int ctr = 0; ctr < mp.getCount(); ctr++) {
                String text = getMultipartText(mp.getBodyPart(ctr));
                if (text != null) {
                    return text;
                }
            }
        }
        return null;
    }

    /**
     * Is attachment boolean.
     *
     * @param bodyPart the body part
     * @return the boolean
     * @throws MessagingException the messaging exception
     */
    public boolean isAttachment(final BodyPart bodyPart) throws MessagingException {
        return StringUtils.equalsIgnoreCase(Part.ATTACHMENT, bodyPart.getDisposition())
                && StringUtils.isBlank(bodyPart.getFileName());
    }

    /**
     * Add send mail prop imaps email util.
     *
     * @param name  the name
     * @param value the value
     * @return the imaps email util
     */
    public ImapsEmailUtil addSendMailProp(final String name, final String value) {
        getSendMailProps().setProperty(name, value);
        return this;
    }
}
