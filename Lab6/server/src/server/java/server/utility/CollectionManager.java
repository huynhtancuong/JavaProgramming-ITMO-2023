package server.utility;

import common.data.SpaceMarine;
import common.data.SpaceMarine;
import common.exceptions.CollectionIsEmptyException;

import java.time.ZonedDateTime;
import java.util.LinkedList;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private LinkedList<SpaceMarine> marinesCollection =  new LinkedList<>();
    private ZonedDateTime lastInitTime;
    private ZonedDateTime lastSaveTime;
    private FileManager fileManager;

    public CollectionManager(FileManager fileManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.fileManager = fileManager;
        loadCollection();
    }
    
    /**
     * @return The collection itself.
     */
    public LinkedList<SpaceMarine> getCollection() {
        return marinesCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public ZonedDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Last save time or null if there wasn't saving.
     */
    public ZonedDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return marinesCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return marinesCollection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public SpaceMarine getFirst() {
        if (marinesCollection.isEmpty()) return null;
        return marinesCollection.getFirst();
    }

    /**
     * @return The last element of the collection or null if collection is empty.
     */
    public SpaceMarine getLast() {
        if (marinesCollection.isEmpty()) return null;
        return marinesCollection.getLast();
    }

    /**
     * @return The min element of the collection or null if collection is empty.
     */
    public SpaceMarine getMin() {
        if (marinesCollection.isEmpty()) return null;
        SpaceMarine min = marinesCollection.getFirst();
        for (SpaceMarine spaceMarine : marinesCollection) {
            if (min.compareTo(spaceMarine) < 0) min = spaceMarine;
        }
        return min;
    }

    /**
     * @return The max element of the collection or null if collection is empty.
     */
    public SpaceMarine getMax() {
        if (marinesCollection.isEmpty()) return null;
        SpaceMarine max = marinesCollection.getFirst();
        for (SpaceMarine spaceMarine : marinesCollection) {
            if (max.compareTo(spaceMarine) > 0) max = spaceMarine;
        }
        return max;
    }

    /**
     * @param id ID of the marine.
     * @return A marine by his ID or null if marine isn't found.
     */
    public SpaceMarine getById(Long id) {
        for (SpaceMarine marine : marinesCollection) {
            if (marine.getId().equals(id)) return marine;
        }
        return null;
    }

    /**
     * @param marineToFind A marine who's value will be found.
     * @return A marine by his value or null if marine isn't found.
     */
    public SpaceMarine getByValue(SpaceMarine marineToFind) {
        for (SpaceMarine marine : marinesCollection) {
            if (marine.equals(marineToFind)) return marine;
        }
        return null;
    }

    /**
     * @return Sum of all marines' health or 0 if collection is empty.
     */
    public double getSumOfHealth() {
        double sumOfHealth = 0;
        for (SpaceMarine marine : marinesCollection) {
            sumOfHealth += marine.getHealth();
        }
        return sumOfHealth;
    }

    /**
     * @return Marine, who has max melee weapon.
     * @throws CollectionIsEmptyException If collection is empty.
     */
    public String maxByMeleeWeapon() throws CollectionIsEmptyException {
        if (marinesCollection.isEmpty()) throw new CollectionIsEmptyException();

        SpaceMarine maxMarine = getFirst();
        for (SpaceMarine marine : marinesCollection) {
            if (marine.getMeleeWeapon().compareTo(maxMarine.getMeleeWeapon()) > 0) {
                maxMarine = marine;
            }
        }
        return maxMarine.toString();
    }

    /**
     * @return Marine, who has max by height.
     * @throws CollectionIsEmptyException If collection is empty.
     */
    public String maxByHeight() throws CollectionIsEmptyException {
        if (marinesCollection.isEmpty()) throw new CollectionIsEmptyException();

        SpaceMarine maxMarine = getFirst();
        for (SpaceMarine marine : marinesCollection) {
            if (marine.getHeight().compareTo(maxMarine.getHeight()) > 0) {
                maxMarine = marine;
            }
        }
        return maxMarine.toString();
    }

    /**
     * Adds a new marine to collection.
     * @param marine A marine to add.
     */
    public void addToCollection(SpaceMarine marine) {
        marinesCollection.add(marine);
    }

    /**
     * Removes a new marine to collection.
     * @param marine A marine to remove.
     */
    public void removeFromCollection(SpaceMarine marine) {
        marinesCollection.remove(marine);
    }

    /**
     * Remove marines greater than the selected one.
     * @param marineToCompare A marine to compare with.
     */
    public void removeGreater(SpaceMarine marineToCompare) {
        marinesCollection.removeIf(marine -> marine.compareTo(marineToCompare) > 0);
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        marinesCollection.clear();
    }

    /**
     * Generate next ID. It will be the bigger one + 1.
     * @return Next ID.
     */
    public Long generateNextId() {
        if (marinesCollection.isEmpty()) return 1L;
        return marinesCollection.getLast().getId() + 1;
    }

    /**
     * Saves the collection to file.
     */
    public void saveCollection() {
            fileManager.writeCollection(marinesCollection);
            lastSaveTime = ZonedDateTime.now();
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection() {
        marinesCollection = fileManager.readCollection();
        marinesCollection = verifyCollection(marinesCollection);
        marinesCollection = regenerateID(marinesCollection);
        lastInitTime = ZonedDateTime.now();
    }

    private LinkedList<SpaceMarine> regenerateID(LinkedList<SpaceMarine> marinesCollection) {
        for (int i = 0; i < marinesCollection.size(); i++) {
            SpaceMarine spaceMarine = marinesCollection.get(i);
            spaceMarine.setId((long) (i+1));
            marinesCollection.set(i, spaceMarine);
        }
        return marinesCollection;
    }


    /**
     * iterate through all spaceMarine in collection and discard element has invalid.
     */
    private LinkedList<SpaceMarine> verifyCollection(LinkedList<SpaceMarine> collection) {
        for(int i=0; i<collection.size(); i++){
            SpaceMarine spaceMarine = collection.get(i);
            if (spaceMarine.verifySpaceMarine() == false) {
                collection.remove(spaceMarine);
            }
        }
        return collection;
    }

    @Override
    public String toString() {
        if (marinesCollection.isEmpty()) return "Коллекция пуста!";

        String info = "";
        for (SpaceMarine marine : marinesCollection) {
            info += marine;
            if (marine != marinesCollection.getLast()) info += "\n\n";
        }
        return info;
    }
}
