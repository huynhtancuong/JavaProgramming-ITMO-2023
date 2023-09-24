package common.data;

import java.io.Serializable;

/**
 * Chapter with marines.
 */
public class Chapter implements Serializable {
    private String name;
    private String parentLegion;
    private int marinesCount;
    private String world;

    public Chapter(String name, String parentLegion, int marinesCount, String world) {
        this.name = name;
        this.parentLegion=parentLegion;
        this.marinesCount = marinesCount;
        this.world=world;
    }

    /**
     * @return Name of the chapter.
     */
    public String getName() {
        return name;
    }

    /**
     * @return parent legion of the chapter.
     */
    public String getParentLegion() {
        return parentLegion;
    }

    /**
     * @return Number of marines in the chapter.
     */
    public int getMarinesCount() {
        return marinesCount;
    }

    /**
     * @return World of marines in the chapter.
     */
    public String getWorld() {
        return world;
    }

    @Override
    public String toString() {
        return name + " (" + marinesCount + " soldier, " + "Parent legion: " + parentLegion + ", World: " + world + " )";
    }

    @Override
    public int hashCode() {
        return name.hashCode() + (int) marinesCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Chapter) {
            Chapter chapterObj = (Chapter) obj;
            return name.equals(chapterObj.getName()) && (marinesCount == chapterObj.getMarinesCount());
        }
        return false;
    }
}
