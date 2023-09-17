package server.commands;

import common.data.SpaceMarine;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.MarineRaw;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.DatabaseUserManager;
import server.utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'add_if_max'. Adds a new element to collection if it is greater than the largest element.
 */

public class AddIfMaxCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddIfMaxCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager, DatabaseUserManager databaseUserManager) {
        super("add_if_max {element}","",  "add a new element to the collection if its value is greater than the value of the largest element in this collection");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            MarineRaw marineRaw = (MarineRaw) objectArgument;
            SpaceMarine marineToAdd = databaseCollectionManager.insertMarine(marineRaw, user);
            if (collectionManager.collectionSize() == 0 || marineToAdd.compareTo(collectionManager.getFirst()) > 0) {
                collectionManager.addToCollection(marineToAdd);
                ResponseOutputer.appendln("Soldier successfully added!");
                return true;
            } else ResponseOutputer.appenderror("The value of a soldier is greater than the value of the smallest of the soldiers!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        }
        return false;
    }
}
