package server.commands;

import common.data.SpaceMarine;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import server.utility.ResponseOutputer;

/**
 * Command 'count_greater_than_loyal'. Print the number of elements whose value of the loyal field is greater than the given one.
 */
public class CountGreaterThanLoyalCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public CountGreaterThanLoyalCommand(CollectionManager collectionManager) {
        super("count_greater_than_loyal", "", "print the number of elements whose value of the loyal field is greater than the given one");
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
            Boolean loyalToCompare = Boolean.parseBoolean(argument);
            Integer loyalCounter = 0;
            if (loyalToCompare == false) {
                loyalCounter = collectionManager.collectionSize();
            } else {
                for (SpaceMarine spaceMarine : collectionManager.getCollection()) {
                    if (spaceMarine.getLoyal()) {
                        loyalCounter++;
                    }
                }
            }
            ResponseOutputer.appendln("Number of elements whose value of " +
                    "the loyal field is greater than the given one: " + loyalCounter.toString());

            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " loyal");
        }
        return false;
    }
}
