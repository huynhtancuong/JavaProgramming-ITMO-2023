package server.commands;

import common.data.SpaceMarine;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import common.interaction.MarineRaw;
import server.utility.ResponseOutputer;

/**
 * Command 'sum_of_health'. Prints the sum of health of all marines.
 */
public class SumOfHealthCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public SumOfHealthCommand(CollectionManager collectionManager) {
        super("sum_of_health", "", "display the sum of the health field values for all elements of the collection");
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
            double sum_of_health = collectionManager.getSumOfHealth();
            if (sum_of_health == 0) throw new CollectionIsEmptyException();
            ResponseOutputer.appendln("Сумма здоровья всех солдат: " + sum_of_health);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Коллекция пуста!");
        }
        return false;
    }
}
