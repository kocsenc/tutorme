package edu.rit.se.tutorme.api.exceptions;

/**
 * Exception to represent a server-side token mismatch error.
 *
 * @author Craig Cabrey
 */
public class TokenMismatchException extends AuthenticationException {

    /**
     * Default constructor for the TokenMismatchException class.
     *
     * @param message String of the error message.
     */
    public TokenMismatchException(String message) {
        super(message);
    }

    /**
     * Get the exception message.
     *
     * @return the exception message
     */
    public String getMessage() {
        return super.getMessage();
    }
}
