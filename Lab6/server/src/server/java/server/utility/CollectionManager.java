package server.utility;

import com.sun.source.tree.Tree;
import common.data.SpaceMarine;
import common.exceptions.CollectionIsEmptyException;
import common.exceptions.DatabaseHandlingException;
import common.utility.Outputer;
import server.App;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private final DatabaseCollectionManager databaseCollectionManager;
    private LinkedList<SpaceMarine> marinesCollection =  new LinkedList<>();
    private ZonedDateTime lastInitTime;
    private ZonedDateTime lastSaveTime;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.databaseCollectionManager = databaseCollectionManager;
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
        // StreamAPI duoc the hien o cho nay
        double sumOfHealth = marinesCollection.stream().mapToDouble(SpaceMarine::getHealth).sum();

        // Cach lap trinh thong thuong
//        double sumOfHealth = 0;
//        for (SpaceMarine marine : marinesCollection) {
//            sumOfHealth += marine.getHealth();
//        }

        return sumOfHealth;
    }

    /**
     * @return Collection content or corresponding string if collection is empty.
     */
    public String showCollection() {
        if (marinesCollection.isEmpty()) return "Collection is empty!";
        return marinesCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
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
     * Remove marines greater than the selected one.
     *
     * @param marineToCompare A marine to compare with.
     * @return Greater marines list.
     */
    public TreeSet<SpaceMarine> getGreater(SpaceMarine marineToCompare) {
        return marinesCollection.stream().filter(marine -> marine.compareTo(marineToCompare) > 0).collect(
                TreeSet::new,
                TreeSet::add,
                TreeSet::addAll
        );
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
     * Loads the collection from file.
     */
    private void loadCollection() {
        try {
            marinesCollection = databaseCollectionManager.getCollection();
            lastInitTime = ZonedDateTime.now();
            Outputer.println("Collection loaded.");
            App.logger.info("Collection loaded.");
        } catch (DatabaseHandlingException exception) {
            marinesCollection = new LinkedList<>();
            Outputer.printerror("Collection could not be loaded!");
            App.logger.error("Collection could not be loaded!");
        }
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
