package client.utility;

import client.App;
import common.data.Chapter;
import common.data.Coordinates;
import common.data.MeleeWeapon;
import common.exceptions.CommandUsageException;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.interaction.MarineRaw;
import common.interaction.Request;
import common.interaction.ResponseCode;
import common.utility.Outputer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Receives user requests.
 */
public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public UserHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Receives user input.
     *
     * @param serverResponseCode Last server's response code.
     * @return New request to server.
     */
    public Request handle(ResponseCode serverResponseCode) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        Outputer.println("Back to the script '" + scriptStack.pop().getName() + "'...");
                    }
                    if (fileMode()) {
                        // Nhan input tu file
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            Outputer.print(App.PS1);
                            Outputer.println(userInput);
                        }
                    } else {
                        // Nhan input tu nguoi dung
                        Outputer.print(App.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printerror("An error occurred while entering the command!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerror("Exceeded number of input attempts!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        MarineRaw ticketAddRaw = generateTicketAdd();
                        return new Request(userCommand[0], userCommand[1], ticketAddRaw);
                    case UPDATE_OBJECT:
                        MarineRaw ticketUpdateRaw = generateMarineUpdate();
                        return new Request(userCommand[0], userCommand[1], ticketUpdateRaw);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Outputer.println("Executing a script '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException exception) {
                Outputer.printerror("Script file not found!");
            } catch (ScriptRecursionException exception) {
                Outputer.printerror("Scripts cannot be called recursively!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            Outputer.printerror("Script execution aborted!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request();
        }
        return new Request(userCommand[0], userCommand[1]);
    }

    /**
     * Processes the entered command.
     *
     * @return Status of code.
     */
    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "show":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add_if_max":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "add_if_min":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "remove_greater":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "sum_of_health":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "count_greater_than_loyal":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "group_counting_by_melee_weapon":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "max_by_height":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "max_by_melee_weapon":
                    if(!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "server_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("Command '" + command + "' not found. Type 'help' for help.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Usage: '" + command + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    /**
     * Generates ticket to add.
     *
     * @return Ticket to add.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private MarineRaw generateTicketAdd() throws IncorrectInputInScriptException {
        MarineAsker marineAsker = new MarineAsker(userScanner);
        if (fileMode()) marineAsker.setFileMode();
        return new MarineRaw(
                marineAsker.askName(),
                marineAsker.askCoordinates(),
                marineAsker.askHealth(),
                marineAsker.askMeleeWeapon(),
                marineAsker.askChapter(),
                marineAsker.askLoyal(),
                marineAsker.askHeight()
        );
    }

    /**
     * Generates ticket to update.
     *
     * @return Ticket to update.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private MarineRaw generateMarineUpdate() throws IncorrectInputInScriptException {
        MarineAsker marineAsker = new MarineAsker(userScanner);
        if (fileMode()) marineAsker.setFileMode();
        String name = marineAsker.askQuestion("Do you want to change name?") ?
                marineAsker.askName() : null;
        Coordinates coordinates = marineAsker.askQuestion("Do you want to change coordinate?") ?
                marineAsker.askCoordinates() : null;
        double health = marineAsker.askQuestion("Do you want to change health?") ?
                marineAsker.askHealth() : -1;
        MeleeWeapon meleeWeapon = marineAsker.askQuestion("Do you want to change Melee Weapon?") ?
                marineAsker.askMeleeWeapon() : null;
        Chapter chapter = marineAsker.askQuestion("Do you want to change Chapter?") ?
                marineAsker.askChapter() : null;
        Boolean loyal = marineAsker.askQuestion("Loyal?") ?
                marineAsker.askLoyal() : null;
        float height = marineAsker.askQuestion("Height?") ?
                marineAsker.askHeight() : -1;

        return new MarineRaw(
                name,
                coordinates,
                health,
                meleeWeapon,
                chapter,
                loyal,
                height
        );
    }

    /**
     * Checks if UserHandler is in file mode now.
     *
     * @return Is UserHandler in file mode now boolean.
     */
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}
