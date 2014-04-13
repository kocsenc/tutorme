package edu.rit.se.tutorme.commands;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.exceptions.APIResponseException;

/**
 * Class that performs a logout request to the API.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class LogoutCommand implements Command {

    /**
     * Result of the request.
     */
    private boolean status;

    /**
     * Constructor for the LogoutCommand class.
     */
    public LogoutCommand() {
        this.status = false;
    }

    /**
     * Execute the logout call.
     *
     * @param api API implementation object
     */
    @Override
    public void execute(Object api) {
        if (api instanceof BackendInterface) {
            try {
                this.status = ((BackendInterface) api).logout();
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
     * Get the result of the request.
     *
     * @return result of the request
     */
    @Override
    public Object getResult() {
        return null;
    }
}