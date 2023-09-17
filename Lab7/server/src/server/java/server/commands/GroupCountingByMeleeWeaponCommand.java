package server.commands;

import common.data.MeleeWeapon;
import common.data.SpaceMarine;
import common.exceptions.DatabaseHandlingException;
import common.exceptions.WrongAmountOfElementsException;
import common.interaction.MarineRaw;
import common.interaction.User;
import server.utility.CollectionManager;
import server.utility.DatabaseCollectionManager;
import server.utility.ResponseOutputer;

import java.util.Arrays;

/**
 * Command 'group_counting_by_melee_weapon'. Shows information about group melee weapon of the collection.
 */
public class GroupCountingByMeleeWeaponCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public GroupCountingByMeleeWeaponCommand(CollectionManager collectionManager) {
        super("group_counting_by_melee_weapon", "", "group the elements of the collection by the value of the melee Weapon field, display the number of elements in each group");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {

        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            Integer MeleeWeaponCounter[] = new Integer[3];
            Arrays.fill(MeleeWeaponCounter, 0);
            for (SpaceMarine spaceMarine : collectionManager.getCollection()) {
                if (spaceMarine.getMeleeWeapon() == MeleeWeapon.POWER_SWORD)
                    MeleeWeaponCounter[0]++;
                if (spaceMarine.getMeleeWeapon() == MeleeWeapon.LIGHTING_CLAW)
                    MeleeWeaponCounter[1]++;
                if (spaceMarine.getMeleeWeapon() == MeleeWeapon.POWER_FIST)
                    MeleeWeaponCounter[2]++;
            }
            ResponseOutputer.appendln("Number element in: ");
            ResponseOutputer.appendln("   POWER_SWORD:\t" + MeleeWeaponCounter[0].toString());
            ResponseOutputer.appendln("   LIGHTING_CLAW:\t" + MeleeWeaponCounter[1].toString());
            ResponseOutputer.appendln("   POWER_FIST:\t" + MeleeWeaponCounter[2].toString());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        }
        return false;
    }
}
