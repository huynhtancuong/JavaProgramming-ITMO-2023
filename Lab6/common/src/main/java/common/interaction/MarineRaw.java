package common.interaction;

import common.data.*;

import java.io.Serializable;

/**
 * Class for get Marines value.
 */
public class MarineRaw implements Serializable {
    private String name;
    private Coordinates coordinates;
    private double health;
    private MeleeWeapon meleeWeapon;
    private Chapter chapter;
    private Boolean loyal;
    private float height;

    public MarineRaw(String name, Coordinates coordinates, double health, MeleeWeapon meleeWeapon, Chapter chapter, Boolean loyal, float height) {
        this.name = name;
        // A constructor.
        this.coordinates = coordinates;
        this.health = health;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
        this.loyal = loyal;
        this.height = height;
    }

    /**
     * @return Name of the Marine.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates of the Marine.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Health of the marine.
     */
    public double getHealth() {
        return health;
    }

    /**
     * @return Melee weapon of the marine.
     */
    public MeleeWeapon getMeleeWeapon() { return meleeWeapon; }

    /**
     * @return Chapter of the marine.
     */
    public Chapter getChapter() {
        return chapter;
    }

    /**
     * @return Loyal of the marine.
     */
    public Boolean getLoyal(){ return loyal; }

    /**
     * @return Height of the marine.
     */
    public Float getHeight(){ return height; }

    @Override
    public String toString() {
        String info = "";
        info += "\nName: " + name;
        info += "\nCoordinate: " + coordinates;
        info += "\nHealth: " + health;
        info += "\nMelee Weapon: " + meleeWeapon;
        info += "\n Chapter: " + chapter;
        info += "\n Loyal: " + loyal;
        info += "\n Height: " + height;
        return info;
    }

    @Override
    // A method that returns a hash code value for the object.
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + (int) health + meleeWeapon.hashCode() + chapter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof SpaceMarine) {
            SpaceMarine marineObj = (SpaceMarine) obj;
            return name.equals(marineObj.getName()) && coordinates.equals(marineObj.getCoordinates()) &&
                    (health == marineObj.getHealth()) && (meleeWeapon == marineObj.getMeleeWeapon()) &&
                    chapter.equals(marineObj.getChapter());
        }
        return false;
    }
}
