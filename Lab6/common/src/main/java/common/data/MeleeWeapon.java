package common.data;

import java.io.Serializable;

/**
 * Enumeration with marine melee weapon constants.
 */
public enum MeleeWeapon implements Serializable {
    POWER_SWORD,
    LIGHTING_CLAW,
    POWER_FIST;

    /**
     * Generates a beautiful list of enum string values.
     * @return String with all enum values separated by comma.
     */
    public static String nameList() {
        String nameList = "";
        for (MeleeWeapon meleeWeapon : values()) {
            nameList += meleeWeapon.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
