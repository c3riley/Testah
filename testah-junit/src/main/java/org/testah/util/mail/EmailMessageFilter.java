package org.testah.util.mail;

/**
 * The interface Email message filter.
 *
 * @param <T> the type parameter
 */
public interface EmailMessageFilter<T> {

    /**
     * Is match boolean.
     *
     * @param emailMessage the email message
     * @return the boolean
     */
    boolean isMatch(T emailMessage);


}
