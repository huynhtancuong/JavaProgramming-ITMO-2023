package server.utility;

import common.exceptions.HistoryIsEmptyException;
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
    private Command saveCommand;
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
    private Command countGreaterThanLoyalCommand;
    private Command serverExitCommand;

    public CommandManager(HelpCommand helpCommand, InfoCommand infoCommand, ShowCommand showCommand, AddCommand addCommand, UpdateCommand updateCommand,
                          RemoveByIdCommand removeByIdCommand, ClearCommand clearCommand, SaveCommand saveCommand, ExitCommand exitCommand, ExecuteScriptCommand executeScriptCommand,
                          AddIfMinCommand addIfMinCommand, AddIfMaxCommand addIfMaxCommand, RemoveGreaterCommand removeGreaterCommand, HistoryCommand historyCommand, SumOfHealthCommand sumOfHealthCommand,
                          MaxByMeleeWeaponCommand maxByMeleeWeaponCommand, MaxByHeightCommand maxByHeightCommand, GroupCountingByMeleeWeaponCommand groupCountingByMeleeWeaponCommand, CountGreaterThanLoyalCommand countGreaterThanLoyalCommand,
                          ServerExitCommand serverExitCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.addCommand = addCommand;
        this.updateCommand = updateCommand;
        this.removeByIdCommand = removeByIdCommand;
        this.clearCommand = clearCommand;
        this.saveCommand = saveCommand;
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
        this.countGreaterThanLoyalCommand = countGreaterThanLoyalCommand;
        this.serverExitCommand = serverExitCommand;

        commands.add(helpCommand);
        commands.add(infoCommand);
        commands.add(showCommand);
        commands.add(addCommand);
        commands.add(updateCommand);
        commands.add(removeByIdCommand);
        commands.add(clearCommand);
        commands.add(saveCommand);
        commands.add(exitCommand);
        commands.add(executeScriptCommand);
        commands.add(addIfMinCommand);
        commands.add(addIfMaxCommand);
        commands.add(removeGreaterCommand);
        commands.add(historyCommand);
        commands.add(sumOfHealthCommand);
        commands.add(maxByMeleeWeaponCommand);
        commands.add(maxByHeightCommand);
        commands.add(groupCountingByMeleeWeaponCommand);
        commands.add(countGreaterThanLoyalCommand);
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
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean help(String argument, Object objectArgument) {
        if (helpCommand.execute(argument, objectArgument)) {
            for (Command command : commands) {
                ResponseOutputer.appendtable(command.getName(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean info(String argument, Object objectArgument) {
        return infoCommand.execute(argument, objectArgument);
    }

    public boolean serverExit(String stringArgument, Object objectArgument) {
        save(stringArgument, objectArgument);
        return serverExitCommand.execute(stringArgument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean show(String argument, Object objectArgument) {
        return showCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean add(String argument, Object objectArgument) {
        return addCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean update(String argument, Object objectArgument) {
        return updateCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeById(String argument, Object objectArgument) {
        return removeByIdCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean clear(String argument, Object objectArgument) {
        return clearCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean save(String argument, Object objectArgument) {
        return saveCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean exit(String argument, Object objectArgument) {
        return exitCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean executeScript(String argument, Object objectArgument) {
        return executeScriptCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean addIfMin(String argument, Object objectArgument) {
        return addIfMinCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean addIfMax(String argument, Object objectArgument) {
        return addIfMaxCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeGreater(String argument, Object objectArgument) {
        return removeGreaterCommand.execute(argument, objectArgument);
    }

    /**
     * Prints the history of used commands.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean history(String argument, Object objectArgument) {
        if (historyCommand.execute(argument, objectArgument)) {
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
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean sumOfHealth(String argument, Object objectArgument) {
        return sumOfHealthCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean maxByMeleeWeapon(String argument, Object objectArgument) { return maxByMeleeWeaponCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean maxByHeight(String argument, Object objectArgument) {
        return maxByHeightCommand.execute(argument, objectArgument);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean groupCountingByMeeleWeapon(String argument, Object objectArgument) { return groupCountingByMeeleWeaponCommand.execute(argument, objectArgument); }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean countGreaterThanLoyal(String argument, Object objectArgument) {
        return countGreaterThanLoyalCommand.execute(argument, objectArgument);
    }

    @Override
    public String toString() {
        return "CommandManager (вспомогательный класс для работы с командами)";
    }
}