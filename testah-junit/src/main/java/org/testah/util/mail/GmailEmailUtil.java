package org.testah.util.mail;

import java.util.Properties;

/**
 * The type Gmail email util.
 */
public class GmailEmailUtil extends ImapsEmailUtil {

    /**
     * Instantiates a new Gmail email util.
     */
    public GmailEmailUtil() {
        //Sent email to read from
        setMailServerAddress("imap.gmail.com");
        setAuth();
        //Setting send mail props for gmail use
        final Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        setSendMailProps(props);
    }

}
