package financetracker;

import java.util.Date;

public class Record {
    private double amount;
    private String reason;
    private Date datetime;
    private String type;

    Record(double amount, String reason, Date datetime, String type) {
        if (amount == 0)
            throw new RecordConsistencyError("Record amount can't be equal to 0");

        if (amount < 0)
            throw new RecordConsistencyError("Records must have a strictly positive amount");

        if (!type.equals("income") && !type.equals("outcome"))
            throw new RecordConsistencyError("Invalid record type");

        this.amount = amount;
        this.reason = reason;
        this.datetime = datetime;
        this.type = type;

        if (type.equals("outcome"))
            this.amount *= -1;
    }

    public static Record income(double amount, String reason, Date datetime) {
        return new Record(amount, reason, datetime, "income");
    }
    
    public static Record outcome(double amount, String reason, Date datetime) {
        return new Record(amount, reason, datetime, "outcome");
    }

    public double getAmount() {
        return this.amount;
    }

    public String getReason() {
        return this.reason;
    }

    public Date getDatetime() {
        return this.datetime;
    }

    /*
     * Define the exported fields (for marshalling or representation).
     * Those are: amount, reason, datetime
     */
    public static int getExportedFields() {
        return 3;
    }
}
