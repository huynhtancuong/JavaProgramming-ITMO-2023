package server.commands;

import common.interaction.User;

/**
 * A interface for Command structure
 */
public interface Command {
    /**
     *
     * @return Description of command
     */
    String getDescription();

    String getUsage();

    /**
     * @return Name of command
     */
    String getName();

    /**
     * @param commandStringArgument argument of the command
     * @return exit status of command
     */
    boolean execute(String commandStringArgument, Object commandObjectArgument, User user);
}
