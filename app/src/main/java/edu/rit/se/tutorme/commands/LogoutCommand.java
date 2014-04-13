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
     * Execute the logout call.
     *
     * @param o
     * @return true if execution successful, false otherwise
     */
    @Override
    public boolean execute(Object o) {
        if (o instanceof BackendInterface) {
            try {
                return ((BackendInterface) o).logout();
            } catch (APIResponseException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}