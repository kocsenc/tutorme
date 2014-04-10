package edu.rit.se.tutorme.api.exceptions;

/**
 *
 *
 * @author Craig Cabrey
 */
public class TokenMismatchException extends AuthenticationException {

    /**
     * Default constructor for the TokenMismatchException class.
     * @param message String of the error message.
     */
    public TokenMismatchException(String message) {
        super(message);
    }
}
