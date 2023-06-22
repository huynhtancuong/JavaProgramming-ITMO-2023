package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.commands.*;
import server.utility.*;

import java.util.Scanner;

/**
 * Main server class. Creates all server instances.
 *
 * @author Pham Ngoc Tam Chau.
 */
public class App {
    public static final int PORT = 25566;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    public static final String ENV_VARIABLE = "HEHE";
    public static final Logger logger = LoggerFactory.getLogger("Log4j2");

    public static void main(String[] args) {
        FileManager collectionFileManager = new FileManager(ENV_VARIABLE);
        CollectionManager collectionManager = new CollectionManager(collectionFileManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new RemoveByIdCommand(collectionManager),
                new ClearCommand(collectionManager),
                new SaveCommand(collectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new AddIfMinCommand(collectionManager),
                new AddIfMaxCommand(collectionManager),
                new RemoveGreaterCommand(collectionManager),
                new HistoryCommand(),
                new SumOfHealthCommand(collectionManager),
                new MaxByMeleeWeaponCommand(collectionManager),
                new MaxByHeightCommand(collectionManager),
                new GroupCountingByMeleeWeaponCommand(collectionManager),
                new CountGreaterThanLoyalCommand(collectionManager),
                new ServerExitCommand()
        );
        Console console = new Console(commandManager, new Scanner(System.in));
        RequestHandler requestHandler = new RequestHandler(commandManager);
        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler);
        console.start();
        server.run();
    }
}
