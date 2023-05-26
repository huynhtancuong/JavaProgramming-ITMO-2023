package server.commands;

import common.data.SpaceMarine;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import common.interaction.MarineRaw;
import server.utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'add_if_min'. Adds a new element to collection if it is less than the smallest element.
 */
public class AddIfMinCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min {element}", "", "add a new element if its value is less than that of the smallest");
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
            if (collectionManager.collectionSize() == 0 || marineToAdd.compareTo(collectionManager.getMin()) < 0) {
                collectionManager.addToCollection(marineToAdd);
                ResponseOutputer.appendln("Солдат успешно добавлен!");
                return true;
            } else ResponseOutputer.appenderror("Значение солдата больше, чем значение наименьшего из солдат!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        }
        return false;
    }
}
