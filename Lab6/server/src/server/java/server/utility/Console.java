package server.utility;

import client.App;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class for display text on console
 */
public class Console implements Runnable{
    private CommandManager commandManager;
    private Scanner userScanner;
    private List<String> scriptStack = new ArrayList<>();

    private Thread t;
    private String threadName = "Console";

    /**
     * @param commandManager Instance of CommandManager Class
     * @param scanner Instance of Scanner class
     */
    public Console(CommandManager commandManager, Scanner scanner) {
        this.commandManager = commandManager;
        this.userScanner = scanner;
    }

    /**
     * Start Interactive Mode with user input
     */
    public void run() {
        String[] userCommand = {"", ""};
        int commandStatus;
        Console.println("List of available commands on server side: save");
        try {
            do {
                Console.print(App.PS1);
                userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandManager.addToHistory(userCommand[0]);
                commandStatus = launchCommand(userCommand);

                switch (commandStatus) {
                    case 0:
                        Console.println("Command " + userCommand[0] + " executed successfully.");
                        break;
                    case 3:
                        Console.println("Command " + userCommand[0] + " not found. Available commands: save.");
                        break;
                }
            } while (commandStatus != 2);
        }
        catch (NoSuchElementException exception) {
            Console.printError("User input not found");
        }
        catch (IllegalStateException exception) {
            Console.printError("Unexpected error");
        }
    }

    public void start() {
        if (t==null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }


    /**
     * Launchs the command.
     * @param userCommand Command to launch.
     * @return Exit code.
     */
    private int launchCommand(String[] userCommand) {
        switch (userCommand[0]) {
            case "":
                return 3;
            case "save":
                if (!commandManager.save(userCommand[1], null)) return 1;
                break;
            default:
                return 3;
        }
        return 0;
    }

    /**
     * @param toOut Object to print out
     */
    public static void print(Object toOut) {
        System.out.print(toOut);
    }

    /**
     * @param toOut Object to be printed out
     */
    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    /**
     * @param toOut Object to be printed out
     */
    public static void printError(Object toOut) {
        System.out.println("Error: " +  toOut);
    }

    /**
     * @param toOut1 Object to be printed out on the left
     * @param toOut2 Object to be printed out on the right
     */
    public static void printTable(Object toOut1, Object toOut2) {
        System.out.printf("%-37s%-1s%n", toOut1, toOut2);
    }

    @Override
    public String toString() {
        return "Console (class for launch command)";
    }
}