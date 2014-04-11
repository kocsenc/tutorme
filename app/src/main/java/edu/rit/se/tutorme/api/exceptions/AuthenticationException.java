package edu.rit.se.tutorme.api.exceptions;

/**
 * Exception that represents an authentication exception.
 *
 * @author Craig Cabrey
 */
public class AuthenticationException extends APIResponseException {

    /**
     * Default constructor for the AuthenticationException class.
     *
     * @param message String of the error message.
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
