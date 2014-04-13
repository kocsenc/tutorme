package edu.rit.se.tutorme.commands;

/**
 * Interface for a command.
 *
 * @author Craig Cabrey <craigcabrey@gmail.com>
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param api API implementation object.
     */
    public void execute(Object api);

    /**
     * Get status of command execution.
     *
     * @return true if successful, false otherwise
     */
    public boolean getStatus();

    /**
     * Get result of command execution.
     *
     * @return Generic object of result.
     */
    public Object getResult();
}
