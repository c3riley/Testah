package org.testah.util.mail;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;
import microsoft.exchange.webservices.data.property.complex.InternetMessageHeader;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.testah.TS;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Exchange email util.
 */
public class ExchangeEmailUtil extends AbstractEmailUtil<ExchangeEmailUtil,
        EmailMessage, WebCredentials, WellKnownFolderName, InternetMessageHeader> {

    /**
     * The constant MAX_NUMBER_OF_MESSAGES.
     */
    public static final int MAX_NUMBER_OF_MESSAGES = 20;
    private static final WellKnownFolderName DEFAULT_FOLDER = WellKnownFolderName.Inbox;
    private int maxNumberOfMessages = MAX_NUMBER_OF_MESSAGES;
    private ExchangeService service;
    private ExchangeVersion exchangeVersion;

    /**
     * Instantiates a new Exchange email util.
     *
     * @param mailServerAddress the mail server address
     */
    public ExchangeEmailUtil(final String mailServerAddress) {
        this(mailServerAddress, ExchangeVersion.Exchange2010_SP1);
    }

    /**
     * Instantiates a new Exchange email util.
     *
     * @param uri             the uri
     * @param exchangeVersion the exchange version
     */
    public ExchangeEmailUtil(final String uri, ExchangeVersion exchangeVersion) {
        super(uri);
        this.setFolder(DEFAULT_FOLDER);
        this.exchangeVersion = exchangeVersion;
    }

    @Override
    public ExchangeEmailUtil connect() {
        try {
            this.service = new ExchangeService(exchangeVersion);
            this.service.setUrl(new URI(getMailServerAddress()));
            this.service.setCredentials(getAuth());
        } catch (Exception e) {
            throw new RuntimeException("Issue with exchange[" + exchangeVersion + "]: " + getMailServerAddress(), e);
        }
        return this;
    }

    @Override
    public List<EmailMessage> getAllMessages() throws Exception {
        List<EmailMessage> emailMessages = new ArrayList<EmailMessage>();
        try {
            Folder folderToUse = Folder.bind(service, getFolder());
            FindItemsResults<Item> emails = service.findItems(folderToUse.getId(), new ItemView(maxNumberOfMessages));
            ItemId itemId;
            Item emailItem;
            for (Item item : emails.getItems()) {
                try {
                    itemId = item.getId();
                    emailItem = Item.bind(service, itemId, PropertySet.FirstClassProperties);
                    emailMessages.add(EmailMessage.bind(service, emailItem.getId()));
                } catch (Exception e) {
                    TS.log().warn("Issue with email:" + item, e);
                }
            }
        } catch (Exception e) {
            TS.log().warn("Issue with getting email from " + getFolder(), e);
        }
        return emailMessages;
    }

    @Override
    public EmailMessageFilter<EmailMessage> getMsgBySubjectFilter(String subject) throws Exception {
        return (message) -> {
            try {
                return StringUtils.equalsIgnoreCase(subject, message.getSubject());
            } catch (ServiceLocalException e) {
                TS.log().warn("Issue with subject filter for message: " + message, e);
            }
            return false;
        };
    }

    private boolean getAddressFilter(String expectedAddress,
                                     final EmailAddressCollection addresses) throws Exception {
        for (final EmailAddress address : addresses) {
            if (StringUtils.equalsIgnoreCase(expectedAddress, address.getAddress())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EmailMessageFilter<EmailMessage> getMsgByToEmailFilter(String toAddress) throws Exception {
        return (message) -> {
            try {
                return getAddressFilter(toAddress, message.getToRecipients());
            } catch (Exception e) {
                TS.log().warn("Issue with ToAddress filter for message: " + message, e);
            }
            return false;
        };
    }

    @Override
    public EmailMessageFilter<EmailMessage> getMsgByCcEmailFilter(String ccAddress) throws Exception {
        return (message) -> {
            try {
                return getAddressFilter(ccAddress, message.getCcRecipients());
            } catch (Exception e) {
                TS.log().warn("Issue with ToAddress filter for message: " + message, e);
            }
            return false;
        };
    }

    @Override
    public EmailMessageFilter<EmailMessage> getMsgByBccEmailFilter(String bccAddress) throws Exception {
        return (message) -> {
            try {
                return getAddressFilter(bccAddress, message.getBccRecipients());
            } catch (Exception e) {
                TS.log().warn("Issue with ToAddress filter for message: " + message, e);
            }
            return false;
        };
    }

    @Override
    public EmailMessageFilter<EmailMessage> getMsgByFromEmailFilter(String fromAddress) throws Exception {
        return (message) -> {
            try {
                return StringUtils.equalsIgnoreCase(fromAddress, message.getFrom().getAddress());
            } catch (ServiceLocalException e) {
                TS.log().warn("Issue with subject filter for message: " + message, e);
            }
            return false;
        };
    }

    @Override
    public List<File> getAttachmentFiles(EmailMessage message, boolean shouldDeleteOnExit) throws Exception {
        final List<File> attachmentFiles = new ArrayList<>();
        if (message != null) {
            try {
                message.getAttachments().forEach(attachment -> {
                    if (attachment instanceof FileAttachment) {
                        final FileAttachment fileAttachment = (FileAttachment) attachment;
                        if (fileAttachment != null) {
                            final File file = new File(attachment.getName());
                            System.out.println(file.getAbsolutePath());
                            try {
                                deleteOnExit(file, shouldDeleteOnExit);
                                fileAttachment.load(file.getAbsolutePath());
                                attachmentFiles.add(file);
                            } catch (Exception e) {
                                TS.log().debug("Issue with attachment[" + fileAttachment.getName() + "]");
                            }
                        }
                    }
                });
            } catch (ServiceLocalException e) {
                TS.log().debug("Issue with getAttachmentsAsFiles", e);
            }
        }
        return attachmentFiles;
    }

    @Override
    public String getMsgBodyText(EmailMessage message) throws Exception {
        return getMsgBody(message, BodyType.Text.name());
    }

    @Override
    public String getMsgBodyHtml(EmailMessage message) throws Exception {
        return getMsgBody(message, BodyType.HTML.name());
    }

    @Override
    public String getMsgBody(EmailMessage message, String contentType) throws Exception {
        final PropertySet BindPropSet = new PropertySet(BasePropertySet.FirstClassProperties);
        BindPropSet.setRequestedBodyType(BodyType.valueOf((contentType != null ? contentType : BodyType.HTML.name())));
        final Item itm = Item.bind(service, message.getId(), BindPropSet);
        return itm.getBody().toString();
    }

    @Override
    public ExchangeEmailUtil deleteMsg(EmailMessage message) throws Exception {
        return deleteMsg(message,DeleteMode.HardDelete);
    }

    public ExchangeEmailUtil deleteMsg(EmailMessage message, final DeleteMode deleteMode) throws Exception {
        if(message!=null) {
            message.delete(DeleteMode.HardDelete);
        }
        return self();
    }

    @Override
    public ExchangeEmailUtil clearInbox() throws Exception {
        return null;
    }

    @Override
    public ExchangeEmailUtil sendMessage(SendMailDto sendMailDto) {
        throw new NotImplementedException("Not Implimented Yet");
    }

    @Override
    public ExchangeEmailUtil setAuth(String userName, String password, String domain) {
        this.auth = new WebCredentials(userName, password, domain);
        this.setDomain(domain);
        return this;
    }

    @Override
    protected String getUserName() {
        return this.auth.getUser();
    }

    @Override
    protected String getPassword() {
        return this.auth.getPwd();
    }

    /**
     * Gets max number of messages.
     *
     * @return the max number of messages
     */
    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    /**
     * Sets max number of messages.
     *
     * @param maxNumberOfMessages the max number of messages
     * @return the max number of messages
     */
    public ExchangeEmailUtil setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
        return this;
    }

    @Override
    public void close() {
        try {
            if (service != null) {
                service.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue trying to close Exchange connection", e);
        }
    }

    /**
     * Get Folder Name used.
     *
     * @return folder name
     */
    public String getFolderName() {
        if (folder != null) {
            return folder.name();
        }
        return null;
    }

    /**
     * Gets exchange version.
     *
     * @return the exchange version
     */
    public ExchangeVersion getExchangeVersion() {
        return exchangeVersion;
    }

    /**
     * Sets exchange version.
     *
     * @param exchangeVersion the exchange version
     * @return the exchange version
     */
    public ExchangeEmailUtil setExchangeVersion(ExchangeVersion exchangeVersion) {
        this.exchangeVersion = exchangeVersion;
        return this;
    }

    @Override
    public HashMap<String, List<String>> getMessageHeadersAsMap(final EmailMessage message) {
        HashMap<String, List<String>> headers = new HashMap<>();
        try {
            message.getInternetMessageHeaders().forEach(header -> {
                if (headers.get(header.getName()) == null) {
                    headers.put(header.getName(), new ArrayList<String>());
                }
                headers.get(header.getName()).add(header.getValue());
            });
        } catch (ServiceLocalException e) {
            TS.log().debug("Issue with getMessageHeaders", e);
        }
        return headers;
    }

    @Override
    public List<InternetMessageHeader> getMessageHeaders(final EmailMessage message) {
        List<InternetMessageHeader> headers = new ArrayList<>();
        try {
            for (InternetMessageHeader internetMessageHeader : message.getInternetMessageHeaders()) {
                headers.add(internetMessageHeader);
            }
        } catch (ServiceLocalException e) {
            TS.log().debug("Issue with getMessageHeaders", e);
        }
        return headers;
    }

    @Override
    protected EmailDto<EmailMessage> fillEmailDto(final EmailMessage message, final EmailDto<EmailMessage> emailDto) throws Exception {
        emailDto.setSubject(message.getSubject());
        emailDto.setToAddresses(getAddresses(message.getToRecipients()));
        emailDto.setCcAddresses(getAddresses(message.getCcRecipients()));
        emailDto.setBccAddresses(getAddresses(message.getBccRecipients()));
        emailDto.setFromAddress(message.getFrom().getAddress());
        emailDto.setSubject(message.getSubject());
        return emailDto;
    }

    private List<String> getAddresses(final EmailAddressCollection addresses) throws Exception {
        List<String> addressList = new ArrayList<>();
        for (final EmailAddress address : addresses) {
            addressList.add(address.getAddress());
        }
        return addressList;
    }

}
