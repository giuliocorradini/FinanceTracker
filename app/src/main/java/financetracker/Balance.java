package financetracker;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.*;


/**
 * Data structure to collect {@link Record} objects.
 * This class provides the lowest-level functionalities to manage
 * the data of the application.
 *
 * <p>It implements the Serializable interface since it is used by
 * the {@link financetracker.io.Persistence persistence mechanism} for
 * saving the data.</p>
 *
 * <p>Under the hood it wraps a LinkedList that holds all the Records.
 * Every operation on the list is exposed via various methods.
 * Data can be primarily read using the {@link #stream()} method, that simply
 * proxies the underlying collection stream method.</p>
 */
public class Balance implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    List<Record> container;

    /**
     * Create a new empty Balance object.
     */
    public Balance() {
        this.container = new LinkedList<>();
    }

    /**
     * Add the passed record to the list.
     * @param r The record to add.
     */
    public synchronized void addRecord(Record r) {
        this.container.add(r);
    }

    /**
     * Deletes the passed record. A record is compared by its {@link Record UID}.
     * @param r The record to delete.
     */
    public synchronized void deleteRecord(Record r) {
        Record saved_record = getRecordByUID(r.getUID());
        if(saved_record != null)
            this.container.remove(saved_record);
    }

    /**
     * Internal method to search a Record in the balance by its UID.
     * UID management in the collection is not exposed to the user,
     * hence this method is private.
     * @param uid the UID field of a record.
     * @return Record if found, null otherwise.
     */
    private Record getRecordByUID(int uid) {
        for(Record r: container) {
            if(r.getUID() == uid)
                return r;
        }

        return null;
    }

    /**
     * Get a new array of saved Records.
     * @return a new array of saved Records.
     */
    public synchronized Record[] getRecords() {
        return this.container.toArray(Record[]::new);
    }

    /**
     * Get a Stream with saved Records
     * @return a Stream with saved Records.
     */
    public Stream<Record> stream() {
        return this.container.stream();
    }

    /**
     * Get the number of saved records.
     * @return The number of saved records.
     */
    public int getRecordCount() {
        return this.container.size();
    }

    /**
     * Get the Record at given index.
     * @param index the index to get.
     * @return a Record if found.
     * @throws IndexOutOfBoundsException if the index is out of the container size.
     */
    public Record getRecordAt(int index) {
        return this.container.get(index);
    }

    /**
     * Resets the Record object counter and sets a new UID for every
     * record. This is done when loading data from a file.
     * Only Balance and connected class are aware of UIDs. The model and
     * other part of the application are not.
     */
    public synchronized void rebuildIndex() {
        Record.resetObjectCounter();
        this.container.stream().forEach(Record::setNewUID);
    }
}
