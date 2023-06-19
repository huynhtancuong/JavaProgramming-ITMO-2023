package server.commands;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.MeleeWeapon;
import common.data.SpaceMarine;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.MarineNotFoundException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.MarineRaw;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

import java.time.ZonedDateTime;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update <ID> {element}", "", "update collection element value by ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if (argument.isEmpty()) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            Long id = Long.parseLong(argument);
            SpaceMarine oldMarine = collectionManager.getById(id);
            if (oldMarine == null) throw new MarineNotFoundException();

            MarineRaw marineRaw = (MarineRaw) objectArgument;
            String name = marineRaw.getName() == null ? oldMarine.getName() : marineRaw.getName();
            Coordinates coordinates = marineRaw.getCoordinates() == null ? oldMarine.getCoordinates() : marineRaw.getCoordinates();
            ZonedDateTime creationDate = oldMarine.getCreationDate();
            double health = marineRaw.getHealth() == -1 ? oldMarine.getHealth() : marineRaw.getHealth() ;
            MeleeWeapon meleeWeapon = marineRaw.getMeleeWeapon() == null ? oldMarine.getMeleeWeapon() : marineRaw.getMeleeWeapon();
            Chapter chapter = marineRaw.getChapter() == null ? oldMarine.getChapter() : marineRaw.getChapter();
            Boolean loyal = marineRaw.getLoyal() == null ? oldMarine.getLoyal() : marineRaw.getLoyal();
            float height = marineRaw.getHeight() == -1 ? oldMarine.getHeight() : marineRaw.getHeight();


            collectionManager.removeFromCollection(oldMarine);

            collectionManager.addToCollection(new SpaceMarine(
                oldMarine.getId(),
                name,
                coordinates,
                creationDate,
                health,
                loyal,
                height,
                meleeWeapon,
                chapter
            ));
            ResponseOutputer.appendln("Солдат успешно изменен!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID должен быть представлен числом!");
        } catch (MarineNotFoundException exception) {
            ResponseOutputer.appenderror("Солдата с таким ID в коллекции нет!");
        }
        return false;
    }
}
