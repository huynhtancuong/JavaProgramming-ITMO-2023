package server.utility;

import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.interaction.User;

/**
 * Handles requests.
 */
public class HandleResquestTask {
    private CommandManager commandManager;

    public HandleResquestTask(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Handles requests.
     *
     * @param request Request to be processed.
     * @return Response to request.
     */
    public Response handle(Request request) {
        User hashedUser = new User(
                request.getUser().getUsername(),
                PasswordHasher.hashPassword(request.getUser().getPassword())
        );
        commandManager.addToHistory(request.getCommandName());
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    /**
     * Executes a command from a request.
     *
     * @param command               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private ResponseCode executeCommand(String command, String commandStringArgument,
                                        Object commandObjectArgument, User user) {
        switch (command) {
            case "":
                break;
            case "help":
                if (!commandManager.help(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "info":
                if (!commandManager.info(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "show":
                if (!commandManager.show(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "add":
                if (!commandManager.add(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "update":
                if (!commandManager.update(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_by_id":
                if (!commandManager.removeById(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "clear":
                if (!commandManager.clear(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "execute_script":
                if (!commandManager.executeScript(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "exit":
                if (!commandManager.exit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "add_if_max":
                if (!commandManager.addIfMax(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "add_if_min":
                if (!commandManager.addIfMin(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "remove_greater":
                if (!commandManager.removeGreater(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "history":
                if (!commandManager.history(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "sum_of_health":
                if (!commandManager.sumOfHealth(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "max_by_melee_weapon":
                if (!commandManager.maxByMeleeWeapon(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "max_by_height":
                if (!commandManager.maxByHeight(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "group_counting_by_melee_weapon":
                if (!commandManager.groupCountingByMeeleWeapon(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;

            case "server_exit":
                if (!commandManager.serverExit(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                return ResponseCode.SERVER_EXIT;
            case "login":
                if (!commandManager.login(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            case "register":
                if (!commandManager.register(commandStringArgument, commandObjectArgument, user))
                    return ResponseCode.ERROR;
                break;
            default:
                ResponseOutputer.appendln("Command '" + command + "' not found. Type 'help' for help..");
                return ResponseCode.ERROR;
        }
        return ResponseCode.OK;
    }
}
