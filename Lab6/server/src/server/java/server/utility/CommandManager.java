package server.utility;

import common.exceptions.HistoryIsEmptyException;
import common.interaction.User;
import server.commands.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Operates the commands.
 */
public class CommandManager {
    private final int COMMAND_HISTORY_SIZE = 10;
    private String[] commandHistory = new String[COMMAND_HISTORY_SIZE];
    private List<Command> commands = new ArrayList<>();
    private Command helpCommand;
    private Command infoCommand;
    private Command showCommand;
    private Command addCommand;
    private Command updateCommand;
    private Command removeByIdCommand;
    private Command clearCommand;
    private Command exitCommand;
    private Command executeScriptCommand;
    private Command addIfMinCommand;
    private Command addIfMaxCommand;
    private Command removeGreaterCommand;
    private Command historyCommand;
    private Command sumOfHealthCommand;
    private Command maxByMeleeWeaponCommand;
    private Command maxByHeightCommand;
    private Command groupCountingByMeeleWeaponCommand;
    private Command serverExitCommand;
    private Command loginCommand;
    private Command registerCommand;

    public CommandManager(HelpCommand helpCommand, InfoCommand infoCommand, ShowCommand showCommand, AddCommand addCommand,
                          UpdateCommand updateCommand, RemoveByIdCommand removeByIdCommand, ClearCommand clearCommand,
                          ExitCommand exitCommand, ExecuteScriptCommand executeScriptCommand, AddIfMinCommand addIfMinCommand,
                          AddIfMaxCommand addIfMaxCommand, RemoveGreaterCommand removeGreaterCommand, HistoryCommand historyCommand,
                          SumOfHealthCommand sumOfHealthCommand, MaxByMeleeWeaponCommand maxByMeleeWeaponCommand,
                          MaxByHeightCommand maxByHeightCommand, GroupCountingByMeleeWeaponCommand groupCountingByMeleeWeaponCommand,
                          ServerExitCommand serverExitCommand, Command loginCommand, Command registerCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.addCommand = addCommand;
        this.updateCommand = updateCommand;
        this.removeByIdCommand = removeByIdCommand;
        this.clearCommand = clearCommand;
        this.exitCommand = exitCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.addIfMinCommand = addIfMinCommand;
        this.addIfMaxCommand = addIfMaxCommand;
        this.removeGreaterCommand = removeGreaterCommand;
        this.historyCommand = historyCommand;
        this.sumOfHealthCommand = sumOfHealthCommand;
        this.maxByMeleeWeaponCommand = maxByMeleeWeaponCommand;
        this.maxByHeightCommand = maxByHeightCommand;
        this.groupCountingByMeeleWeaponCommand = groupCountingByMeleeWeaponCommand;
        this.serverExitCommand = serverExitCommand;
        this.loginCommand = loginCommand;
        this.registerCommand = registerCommand;


        commands.add(helpCommand);
        commands.add(infoCommand);
        commands.add(showCommand);
        commands.add(addCommand);
        commands.add(updateCommand);
        commands.add(removeByIdCommand);
        commands.add(clearCommand);
        commands.add(exitCommand);
        commands.add(executeScriptCommand);
        commands.add(addIfMinCommand);
        commands.add(addIfMaxCommand);
        commands.add(removeGreaterCommand);
        commands.add(historyCommand);
        commands.add(sumOfHealthCommand);
        commands.add(maxByMeleeWeaponCommand);
        commands.add(maxByHeightCommand); //
        commands.add(groupCountingByMeleeWeaponCommand);
        commands.add(serverExitCommand);
    }

    /**
     * @return The command history.
     */
    public String[] getCommandHistory() {
        return commandHistory;
    }

    /**
     * @return List of manager's commands.
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Adds command to command history.
     * @param commandToStore Command to add.
     */
    public void addToHistory(String commandToStore) {

        for (Command command : commands) {
            if (command.getName().split(" ")[0].equals(commandToStore)) {
                for (int i = COMMAND_HISTORY_SIZE-1; i>0; i--) {
                    commandHistory[i] = commandHistory[i-1];
                }
                commandHistory[0] = commandToStore;
            }
        }
    }

    /**
     * Prints that command is not found.
     * @param command Command, which is not found.
     * @return Command exit status.
     */
    public boolean noSuchCommand(String command) {
        ResponseOutputer.appendln("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
        return false;
    }
    
    /**
     * Prints info about the all commands.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean help(String stringArgument, Object objectArgument, User user) {
        if (helpCommand.execute(stringArgument, objectArgument, user)) {
            for (Command command : commands) {
                ResponseOutputer.appendtable(command.getName(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean info(String stringArgument, Object objectArgument, User user) {
        return infoCommand.execute(stringArgument, objectArgument, user);
    }

    public boolean serverExit(String stringArgument, Object objectArgument, User user) {
//        save(stringArgument, objectArgument);
        return serverExitCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean show(String stringArgument, Object objectArgument, User user) {
        return showCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean add(String stringArgument, Object objectArgument, User user) {
        return addCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean update(String stringArgument, Object objectArgument, User user) {
        return updateCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean removeById(String stringArgument, Object objectArgument, User user) {
        return removeByIdCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param objectArgument Its argument.
     * @return Command exit status.
     */
    public boolean clear(String stringArgument, Object objectArgument, User user) {
        return clearCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean exit(String stringArgument, Object objectArgument, User user) {
        return exitCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean executeScript(String stringArgument, Object objectArgument, User user) {
        return executeScriptCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean addIfMin(String stringArgument, Object objectArgument, User user) {
        return addIfMinCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean addIfMax(String stringArgument, Object objectArgument, User user) {
        return addIfMaxCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean removeGreater(String stringArgument, Object objectArgument, User user) {
        return removeGreaterCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Prints the history of used commands.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean history(String stringArgument, Object objectArgument, User user) {
        if (historyCommand.execute(stringArgument, objectArgument, user)) {
            try {
                if (commandHistory.length == 0) throw new HistoryIsEmptyException();
    
                ResponseOutputer.appendln("Последние использованные команды:");
                for (int i=0; i<commandHistory.length; i++) {
                    if (commandHistory[i] != null) ResponseOutputer.appendln(" " + commandHistory[i]);
                }
                return true;
            } catch (HistoryIsEmptyException exception) {
                ResponseOutputer.appendln("Ни одной команды еще не было использовано!");
            }
        }
        return false;
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean sumOfHealth(String stringArgument, Object objectArgument, User user) {
        return sumOfHealthCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean maxByMeleeWeapon(String stringArgument, Object objectArgument, User user) { return maxByMeleeWeaponCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean maxByHeight(String stringArgument, Object objectArgument, User user) {
        return maxByHeightCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param stringArgument Its argument.
     * @return Command exit status.
     */
    public boolean groupCountingByMeeleWeapon(String stringArgument, Object objectArgument, User user) { return groupCountingByMeeleWeaponCommand.execute(stringArgument, objectArgument, user); }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean login(String stringArgument, Object objectArgument, User user) {
        return loginCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean register(String stringArgument, Object objectArgument, User user) {
        return registerCommand.execute(stringArgument, objectArgument, user);
    }

    @Override
    public String toString() {
        return "CommandManager (вспомогательный класс для работы с командами)";
    }
}