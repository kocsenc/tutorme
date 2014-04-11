package edu.rit.se.tutorme.api;

import java.util.ArrayList;

import edu.rit.se.tutorme.api.exceptions.APIResponseException;
import edu.rit.se.tutorme.api.exceptions.AuthenticationException;
import edu.rit.se.tutorme.api.exceptions.InvalidParametersException;
import edu.rit.se.tutorme.api.exceptions.TokenMismatchException;

/**
 * Interface that defines the backend
 * programming interface.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public interface BackendInterface {

    /**
     * Method to register a new user.
     *
     * @param user User object to be registered.
     * @return true if successful, false otherwise.
     */
    public boolean register(TutorMeUser user, String password) throws APIResponseException;

    /**
     * Method to authenticate a user.
     *
     * @param email user's email
     * @param password user's password (attempt)
     * @return TutorMeUser object of the user just logged in.
     */
    public TutorMeUser login(String email, String password) throws AuthenticationException;

    /**
     * Method to destroy a user's current session.
     *
     * @return true if successful, false otherwise.
     */
    public boolean logout() throws APIResponseException;

    /**
     * Method to update a user's information.
     *
     * @param user User object with updated information.
     * @return true if successful, false otherwise.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     * server side.
     */
    public boolean updateUser(TutorMeUser user) throws TokenMismatchException;

    /**
     * Method to remove a user's account.
     *
     * @param user User to be removed.
     * @return true if successful, false otherwise.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     * server side.
     */
    public boolean deleteUser(TutorMeUser user) throws TokenMismatchException;

    /**
     * Method to get the list of newest messages.
     *
     * @return List of new TutorMeMessages.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     * server side.
     */
    public ArrayList<TutorMeMessage> getMessages() throws TokenMismatchException;

    /**
     * Method to send a message to a user.
     *
     * @param message Message to be sent
     * @return true if successful, false otherwise.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     * server side.
     */
    public boolean sendMessage(TutorMeMessage message) throws TokenMismatchException;

    /**
     * Method to fetch a tutor profile.
     *
     * @param email Email of the profile to be get.
     * @return TutorMeProfile object received from server.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     * server side.
     * @throws InvalidParametersException Thrown if no such profile (or user is a student) exists.
     */
    public TutorMeProfile getProfile(String email)
            throws InvalidParametersException, TokenMismatchException;

    /**
     * Method to search the system for tutors.
     *
     * @param query The search query to be executed.
     * @return List of TutorMeUser results.
     * @throws TokenMismatchException Thrown if internal token state causes a token mismatch
     * server side.
     */
    public ArrayList<TutorMeUser> search(String query) throws TokenMismatchException;
}
