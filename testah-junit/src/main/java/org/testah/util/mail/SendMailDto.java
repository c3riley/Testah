package org.testah.util.mail;

import org.testah.framework.dto.base.AbstractDtoBase;
import java.util.Date;

public class SendMailDto extends AbstractDtoBase<SendMailDto> {

    private String from;
    private String to;
    private String cc = null;
    private String bcc = null;
    private String subject = "this is a test";
    private String body = "<h1>this is a test</h1>";
    private String content = "text/html; charset=ISO-8859-1";
    private Long sentDate;

    public SendMailDto(final String from) {
        this.from = from;
        this.to = from;
    }

    public String getFrom() {
        return from;
    }

    public SendMailDto setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public SendMailDto setTo(String to) {
        this.to = to;
        return this;
    }

    public String getCc() {
        return cc;
    }

    public SendMailDto setCc(String cc) {
        this.cc = cc;
        return this;
    }

    public String getBcc() {
        return bcc;
    }

    public SendMailDto setBcc(String bcc) {
        this.bcc = bcc;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public SendMailDto setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public SendMailDto setBody(String body) {
        this.body = body;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SendMailDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getSentDate() {
        if (sentDate == null) {
            sentDate = new Date().getTime();
        }
        //Send copy of date to avoid findbugs issue
        return new Date(sentDate);
    }

    public SendMailDto setSentDate(final Date sentDate) {
        this.sentDate = sentDate.getTime();
        return this;
    }
}
