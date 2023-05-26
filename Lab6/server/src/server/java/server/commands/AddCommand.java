package server.commands;

import common.data.SpaceMarine;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import common.interaction.MarineRaw;
import server.utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add {element}",  "", "add a new element to the collection");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if (!argument.isEmpty()) throw new WrongAmountOfElementsException();
            MarineRaw marineRaw = (MarineRaw) objectArgument;
            collectionManager.addToCollection(new SpaceMarine(
                collectionManager.generateNextId(),
                marineRaw.getName(),
                marineRaw.getCoordinates(),
                ZonedDateTime.now(),
                marineRaw.getHealth(),
                marineRaw.getLoyal(),
                marineRaw.getHeight(),
                marineRaw.getMeleeWeapon(),
                marineRaw.getChapter()
            ));
            ResponseOutputer.appendln("Солдат успешно добавлен!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        }
        return false;
    }
}
