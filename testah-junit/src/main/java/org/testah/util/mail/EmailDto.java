package org.testah.util.mail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.testah.framework.dto.base.AbstractDtoBase;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class EmailDto<M> extends AbstractDtoBase<EmailDto> {

    @JsonIgnore
    private M message;
    private String subject;
    private String fromAddress;
    private List<String> toAddresses;
    private List<String> ccAddresses;
    private List<String> bccAddresses;
    private String bodyHtml;
    private String bodyText;
    private String body;
    private List<File> attachments;
    private HashMap<String, List<String>> headers;

    public EmailDto() {

    }

    @JsonIgnore
    public M getMessage() {
        return message;
    }

    @JsonIgnore
    public EmailDto<M> setMessage(M message) {
        this.message = message;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EmailDto<M> setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public EmailDto<M> setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public EmailDto<M> setToAddresses(List<String> toAddresses) {
        this.toAddresses = toAddresses;
        return this;
    }

    public List<String> getCcAddresses() {
        return ccAddresses;
    }

    public EmailDto<M> setCcAddresses(List<String> ccAddresses) {
        this.ccAddresses = ccAddresses;
        return this;
    }

    public List<String> getBccAddresses() {
        return bccAddresses;
    }

    public EmailDto<M> setBccAddresses(List<String> bccAddresses) {
        this.bccAddresses = bccAddresses;
        return this;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public EmailDto<M> setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
        return this;
    }

    public String getBodyText() {
        return bodyText;
    }

    public EmailDto<M> setBodyText(String bodyText) {
        this.bodyText = bodyText;
        return this;
    }

    public String getBody() {
        return body;
    }

    public EmailDto<M> setBody(String body) {
        this.body = body;
        return this;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public EmailDto<M> setAttachments(List<File> attachments) {
        this.attachments = attachments;
        return this;
    }

    public HashMap<String, List<String>> getHeaders() {
        return headers;
    }

    public EmailDto<M> setHeaders(HashMap<String, List<String>> headers) {
        this.headers = headers;
        return this;
    }
}
