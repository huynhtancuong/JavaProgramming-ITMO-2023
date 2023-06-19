package server.commands;

import common.exceptions.CollectionIsEmptyException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.MarineRaw;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'max_by_height'. Display any object from the collection with the largest height field value.
 */
public class MaxByHeightCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public MaxByHeightCommand(CollectionManager collectionManager) {
        super("max_by_height", "", "display any object from the collection whose height field value is the maximum");
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
            ResponseOutputer.appendln(collectionManager.maxByHeight());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        }
        return true;
    }
}
