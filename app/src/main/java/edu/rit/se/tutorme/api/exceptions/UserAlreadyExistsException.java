package edu.rit.se.tutorme.api.exceptions;

/**
 * Generic API response exception class.
 *
 * @author Craig Cabrey
 */
public class UserAlreadyExistsException extends APIResponseException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
