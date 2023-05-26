package server.commands;

import common.data.SpaceMarine;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MarineNotFoundException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import common.interaction.MarineRaw;
import server.utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'remove_greater'. Removes elements greater than user entered.
 */
public class RemoveGreaterCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater {element}", "", "remove from the collection all elements greater than the given");
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
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            SpaceMarine marineToFind = new SpaceMarine(
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
            SpaceMarine marineFromCollection = collectionManager.getByValue(marineToFind);
            if (marineFromCollection == null) throw new MarineNotFoundException();
            collectionManager.removeGreater(marineFromCollection);
            ResponseOutputer.appendln("Солдаты успешно удалены!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (MarineNotFoundException exception) {
            ResponseOutputer.appenderror("Солдата с такими характеристиками в коллекции нет!");
        }
        return false;
    }
}
