package edu.rit.se.tutorme.api.exceptions;

/**
 * Generic API response exception for an "invalid parameters" response.
 *
 * @author Craig Cabrey
 */
public class InvalidParametersException extends APIResponseException {

    /**
     * Default constructor for the InvalidParametersException class.
     * @param message String of the error message.
     */
    public InvalidParametersException(String message) {
        super(message);
    }
}
