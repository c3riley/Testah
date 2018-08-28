package org.testah.util.mail;

import org.testah.framework.dto.base.AbstractDtoBase;

import java.util.Date;

/**
 * The type Send mail dto.
 */
public class SendMailDto extends AbstractDtoBase<SendMailDto> {

    private String from;
    private String to;
    private String cc = null;
    private String bcc = null;
    private String subject = "this is a test";
    private String body = "<h1>this is a test</h1>";
    private String content = "text/html; charset=ISO-8859-1";
    private Date sentDate;

    /**
     * Instantiates a new Send mail dto.
     *
     * @param from the from
     */
    public SendMailDto(final String from) {
        this.from = from;
        this.to = from;
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets from.
     *
     * @param from the from
     * @return the from
     */
    public SendMailDto setFrom(String from) {
        this.from = from;
        return this;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets to.
     *
     * @param to the to
     * @return the to
     */
    public SendMailDto setTo(String to) {
        this.to = to;
        return this;
    }

    /**
     * Gets cc.
     *
     * @return the cc
     */
    public String getCc() {
        return cc;
    }

    /**
     * Sets cc.
     *
     * @param cc the cc
     * @return the cc
     */
    public SendMailDto setCc(String cc) {
        this.cc = cc;
        return this;
    }

    /**
     * Gets bcc.
     *
     * @return the bcc
     */
    public String getBcc() {
        return bcc;
    }

    /**
     * Sets bcc.
     *
     * @param bcc the bcc
     * @return the bcc
     */
    public SendMailDto setBcc(String bcc) {
        this.bcc = bcc;
        return this;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     * @return the subject
     */
    public SendMailDto setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     * @return the body
     */
    public SendMailDto setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     * @return the content
     */
    public SendMailDto setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Gets sent date.
     *
     * @return the sent date
     */
    public Date getSentDate() {
        if (sentDate == null) {
            sentDate = new Date();
        }
        //Send copy of date to avoid findbugs issue
        return (Date) sentDate.clone();
    }

    /**
     * Sets sent date.
     *
     * @param sentDate the sent date
     * @return the sent date
     */
    public SendMailDto setSentDate(final Date sentDate) {
        this.sentDate = (Date) sentDate.clone();
        return this;
    }
}
