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
     * @param data
     * @return true if execution successful, false otherwise
     */
    public boolean execute(Object data);
}
