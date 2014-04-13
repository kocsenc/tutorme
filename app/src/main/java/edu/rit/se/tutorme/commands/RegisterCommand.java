package edu.rit.se.tutorme.commands;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.TutorMeUser;
import edu.rit.se.tutorme.api.exceptions.APIResponseException;

/**
 * Class that performs a register request to the API.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class RegisterCommand implements Command {

    /**
     * New user to be registered.
     */
    private TutorMeUser user;

    /**
     * Password of the new user.
     */
    private String password;

    /**
     * Status of command execution.
     */
    private boolean status;

    /**
     * Constructor for the RegisterCommand class.
     */
    public RegisterCommand(TutorMeUser user, String password) {
        this.status = false;
    }

    /**
     * Execute the register command.
     *
     * @param api API implementation object
     */
    @Override
    public void execute(Object api) {
        if (api instanceof BackendInterface) {
            try {
                this.status = ((BackendInterface) api).register(this.user, this.password);
            } catch (APIResponseException e) {
            }
        }
    }

    /**
     * Get status of command execution.
     *
     * @return true if successful, false otherwise
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Get result of the request (a TutorMeUser).
     *
     * @return user object
     */
    @Override
    public Object getResult() {
        return null;
    }
}
