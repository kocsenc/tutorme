package edu.rit.se.tutorme.commands;

import edu.rit.se.tutorme.api.BackendInterface;
import edu.rit.se.tutorme.api.exceptions.TokenMismatchException;
import edu.rit.se.tutorme.util.Location;

/**
 * Class that performs a postal conversion request to the API.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public class ConvertPostalCommand implements Command {

    /**
     * Postal code to be sent in the request.
     */
    private String postal;

    /**
     * Status of command execution.
     */
    private boolean status;

    /**
     * Converted Location object.
     */
    private Location result;

    /**
     * Constructor for the ConvertPostalCommand class.
     * @param postal postal code to be sent in the request
     */
    public ConvertPostalCommand(String postal) {
        this.postal = postal;
        this.status = false;
    }

    /**
     * Execute the postal conversion command.
     *
     * @param api API implementation object
     */
    @Override
    public void execute(Object api) {
        if (api instanceof BackendInterface) {
            try {
                ((BackendInterface) api).convertPostal(this.postal);
            } catch (TokenMismatchException e) {
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
     * Get result of the request (a Location).
     *
     * @return converted location object
     */
    @Override
    public Object getResult() {
        return this.result;
    }
}
