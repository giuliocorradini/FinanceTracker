package financetracker;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Record implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient int UID;

    private double amount;

    private String reason;
    private LocalDate date;
    private static int object_counter = 0;
    public Record(double amount, String reason, LocalDate date) {

        if(date == null)
            throw new IllegalArgumentException("date can't be null");

        object_counter++;

        this.UID = object_counter;
        this.amount = amount;
        this.reason = reason;
        this.date = date;
    }

    /*
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

    /*
     * Define the exported fields (for marshalling or representation).
     * Those are: amount, reason, datetime
     */
    public static int getExportedFields() {
        return 3;
    }

    public synchronized int getUID() {
        return UID;
    }

    /*
     * Clone attributes (amount, reason, income) from a clone or another Record object,
     * without copying the UID.
     */
    public synchronized void cloneAttributesFrom(Record r) {
        this.amount = r.amount;
        this.reason = r.reason;
        this.date = r.date;
    }

    public synchronized Record clone() {
        return new Record(this);
    }

    @Override
    public synchronized String toString() {
        return String.format("Record - %s of %.2f in date %s", reason, amount, date.toString());
    }

    protected synchronized static void resetObjectCounter() {
        object_counter = 0;
    }

    protected synchronized void setNewUID() {
        this.UID = ++object_counter;
    }
}
