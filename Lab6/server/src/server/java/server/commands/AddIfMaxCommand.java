package server.commands;

import common.data.SpaceMarine;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.MarineRaw;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'add_if_max'. Adds a new element to collection if it is greater than the largest element.
 */

public class AddIfMaxCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public AddIfMaxCommand(CollectionManager collectionManager) {
        super("add_if_max {element}","",  "add a new element to the collection if its value is greater than the value of the largest element in this collection");
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
            SpaceMarine marineToAdd = new SpaceMarine(
                    collectionManager.generateNextId(),
                    marineRaw.getName(),
                    marineRaw.getCoordinates(),
                    ZonedDateTime.now(),
                    marineRaw.getHealth(),
                    marineRaw.getLoyal(),
                    marineRaw.getHeight(),
                    marineRaw.getMeleeWeapon(),
                    marineRaw.getChapter()
            );
            if (collectionManager.collectionSize() == 0 || marineToAdd.compareTo(collectionManager.getMax()) > 0) {
                collectionManager.addToCollection(marineToAdd);
                ResponseOutputer.appendln("Солдат успешно добавлен!");
                return true;
            } else ResponseOutputer.appenderror("Значение солдата меньше, чем значение наибольшей из солдат!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        }
        return false;
    }
}
