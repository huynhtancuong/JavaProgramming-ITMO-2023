package common.data;

import common.interaction.User;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Main character. Is stored in the collection.
 */
public class SpaceMarine implements Comparable<SpaceMarine>, Serializable {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private double health;
    private Boolean loyal;
    private float height;
    private MeleeWeapon meleeWeapon;
    private Chapter chapter;

    private User owner;

    public SpaceMarine(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, double health, Boolean loyal, float height, MeleeWeapon meleeWeapon, Chapter chapter, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.loyal=loyal;
        this.height=height;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
        this.owner = owner;
    }

    /**
     * @return ID of the mariƒne.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Name of the marine.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates of the marine.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Creation date of the marine.
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @return Health of the marine.
     */
    public double getHealth() {
        return health;
    }

    /**
     * @return Loyal of the marine.
     */
    public Boolean getLoyal(){ return loyal; }

    /**
     * @return Height of the marine.
     */
    public Float getHeight(){ return height; }

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
     * @return owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Check spaceMarine is vailid or not.
     */
    public boolean verifySpaceMarine(){
        if (health <= 0 ) return false;
        if (name.isEmpty() || name.isEmpty() || name == null ) return false;
        if (coordinates == null || coordinates.getY() >267) return false;
        if (creationDate == null ) return false;
        if (meleeWeapon == null ) return false;
        if (chapter.getName().isEmpty() || chapter.getName().isEmpty() || chapter.getName() == null || chapter.getMarinesCount() < 0 || chapter.getMarinesCount()> 1001) return false;

        return true;
    }

    @Override
    public int compareTo(SpaceMarine marineObj) {
        return (int) (health - marineObj.health);
    }

    @Override
    public String toString() {
        String info = "";
        info += "Created number: " + id;
        info += " (Added: " + owner.getUsername() + " " + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
        info += "\n Name: " + name;
        info += "\n Coordinate: " + coordinates;
        info += "\n Health: " + health;
        info += "\n Melee Weapon: " + meleeWeapon;
        info += "\n Chapter: " + chapter;
        info += "\n Loyal: " + loyal;
        info += "\n Height: " + height;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + (int) health + meleeWeapon.hashCode() + chapter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof SpaceMarine marineObj) {
            return name.equals(marineObj.getName()) && coordinates.equals(marineObj.getCoordinates()) &&
                   (health == marineObj.getHealth()) && (meleeWeapon == marineObj.getMeleeWeapon()) &&
                   chapter.equals(marineObj.getChapter());
        }
        return false;
    }
}
