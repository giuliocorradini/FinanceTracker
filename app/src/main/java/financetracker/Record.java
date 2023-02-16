package financetracker;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * This class represent a transaction, the smallest unit of data managed by
 * the application.
 *
 * <p>
 *     A list of Record is collected in a {@link Balance} and managed by it.
 *     To manage edits and deletions of Records in a Balance, each entry has
 *     a unique identifier that is determined with a static object counter.
 * </p>
 *
 * <p>
 *     The class implements Serializable because it is used to save data onto
 *     a file. The UID field is transient and is recalculated each time a file
 *     is opened, by calling {@link Balance#rebuildIndex()}.
 * </p>
 */
public class Record implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient int UID;

    private double amount;

    private String reason;
    private LocalDate date;
    private static int object_counter = 0;

    /**
     * The constructor for a Record.
     * @param amount signed amount of money for the transaction
     * @param reason why this transaction has happened
     * @param date when the transaction took place, can't be null
     * @throws IllegalArgumentException if date is null
     */
    public Record(double amount, String reason, LocalDate date) {

        if(date == null)
            throw new IllegalArgumentException("date can't be null");

        object_counter++;

        this.UID = object_counter;
        this.amount = amount;
        this.reason = reason;
        this.date = date;
    }

    /**
     * Create a clone of an existing Record without updating the global UID counter,
     * and without generating a new UID.
     */
    private Record(Record cloning) {
        this.UID = cloning.UID;
        this.amount = cloning.amount;
        this.reason = cloning.reason;
        this.date = cloning.date;
    }

    public synchronized void setAmount(double amount) {
        this.amount = amount;
    }

    public synchronized void setDate(LocalDate date) {
        this.date = date;
    }

    public synchronized double getAmount() {
        return this.amount;
    }

    public synchronized String getReason() {
        return this.reason;
    }

    public synchronized void setReason(String new_reason) {
        this.reason = new_reason;
    }

    public synchronized LocalDate getDate() {
        return this.date;
    }

    /**
     * Define the number of exported fields (for marshalling or representation).
     * Those are: amount, reason, datetime
     */
    public static int getExportedFields() {
        return 3;
    }

    public synchronized int getUID() {
        return UID;
    }

    /**
     * Clone attributes (amount, reason, income) from a clone or another Record object,
     * without copying the UID.
     * @param r the record to copy from
     */
    public synchronized void cloneAttributesFrom(Record r) {
        this.amount = r.amount;
        this.reason = r.reason;
        this.date = r.date;
    }

    /**
     * Obtain a new Record with the same UID and fields as this.
     * @return a clone Record.
     */
    public synchronized Record clone() {
        return new Record(this);
    }

    /**
     * Get a String representation for displaying the Record. Useful for debugging purposes.
     * @return a string representation of the record.
     */
    @Override
    public synchronized String toString() {
        return String.format("Record - %s of %.2f in date %s", reason, amount, date.toString());
    }

    /**
     * Reset the global object counter. Used by {@link Balance#rebuildIndex()}, hence
     * the protected access modifier.
     */
    protected synchronized static void resetObjectCounter() {
        object_counter = 0;
    }

    /**
     * Set a new UID for this record, by getting the current object counter.
     */
    protected synchronized void setNewUID() {
        this.UID = ++object_counter;
    }
}
