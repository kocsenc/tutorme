package edu.rit.se.tutorme.api.exceptions;

/**
 * Generic API response exception class.
 *
 * @author Craig Cabrey
 */
public class APIResponseException extends Exception {

    /**
     * String of the error message.
     */
    private String message;

    /**
     * Default constructor for the APIResponseException class.
     *
     * @param message String of the error message.
     */
    public APIResponseException(String message) {
        this.message = message;
    }
}
