package financetracker;

import java.time.LocalDate;

public class Record {
    private int UID;
    private double amount;
    private String reason;
    private LocalDate date;
    private static int object_counter = 0;

    public Record(double amount, String reason, LocalDate date) {

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

    public static Record income(double amount, String reason, LocalDate date) {
        return new Record(amount, reason, date);
    }
    
    public static Record outcome(double amount, String reason, LocalDate date) {
        return new Record(-amount, reason, date);
    }

    public double getAmount() {
        return this.amount;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String new_reason) {
        this.reason = new_reason;
    }

    public LocalDate getDate() {
        return this.date;
    }

    /*
     * Define the exported fields (for marshalling or representation).
     * Those are: amount, reason, datetime
     */
    public static int getExportedFields() {
        return 3;
    }

    public int getUID() {
        return UID;
    }

    /*
     * Clone attributes (amount, reason, income) from a clone or another Record object,
     * without copying the UID.
     */
    public void cloneAttributesFrom(Record r) {
        this.amount = r.amount;
        this.reason = r.reason;
        this.date = r.date;
    }

    public Record clone() {
        return new Record(this);
    }
}
