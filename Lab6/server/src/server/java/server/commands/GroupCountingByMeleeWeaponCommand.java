package server.commands;

import common.data.MeleeWeapon;
import common.data.SpaceMarine;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.WrongAmountOfElementsException;
import server.utility.CollectionManager;
import common.interaction.MarineRaw;
import server.utility.ResponseOutputer;
import java.util.Arrays;

/**
 * Command 'group_counting_by_melee_weapon'. Shows information about group melee weapon of the collection.
 */
public class GroupCountingByMeleeWeaponCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public GroupCountingByMeleeWeaponCommand(CollectionManager collectionManager) {
        super("group_counting_by_melee_weapon", "", "group the elements of the collection by the value of the melee Weapon field, display the number of elements in each group");
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
            ResponseOutputer.appendln("Использование: '" + getName() + "'");
        }
        return false;
    }
}
