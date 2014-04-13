package edu.rit.se.tutorme.commands;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.TutorMeUser;
import edu.rit.se.tutorme.api.exceptions.AuthenticationException;

/**
 * Class that performs a login request to the API.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class LoginCommand implements Command {

    /**
     * User email of the user attempting login.
     */
    private String userEmail;
    /**
     * User password of the user attempting login.
     */
    private String userPassword;

    /**
     * User resultant from login request.
     */
    private TutorMeUser user;

    /**
     * Constructor for the LoginCommand class.
     * @param userEmail user email of the user attempting login
     * @param userPassword user password of the user attempting login
     */
    public LoginCommand(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    /**
     * Execute the login command.
     *
     * @param data API implementation object
     */
    @Override
    public void execute(Object data) {
        if (data instanceof BackendInterface) {
            try {
                this.user = ((BackendInterface) data).login(this.userEmail, this.userPassword);
            } catch (AuthenticationException e) {
            }
        } else {
        }
    }

    /**
     * Get result of the request (a TutorMeUser).
     *
     * @return user object
     */
    @Override
    public Object getResult() {
        return this.user;
    }
}
