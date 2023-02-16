package financetracker;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.LinkedList;
import java.util.function.Supplier;
import java.util.stream.*;


/*
 * Data Access Object for records
 */
public class Balance implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    List<Record> container;

    public Balance() {
        this.container = new LinkedList<>();
    }

    public void addRecord(Record r) {
        this.container.add(r);
    }

    public void addRecord(double amount, String reason, LocalDate datetime) {
        Record r = new Record(amount, reason, datetime);
        this.addRecord(r);
    }

    public void deleteRecord(Record r) {
        Record saved_record = getRecordByUID(r.getUID());
        if(saved_record != null)
            this.container.remove(saved_record);
    }

    public Record searchRecord(String key) {
        for(Record r: container) {
            if (r.getReason().equals(key))
                return r.clone();
        }

        return null;
    }

    private Record getRecordByUID(int uid) {
        for(Record r: container) {
            if(r.getUID() == uid)
                return r;
        }

        return null;
    }

    public Record searchRecord(int uid) {
        Record r = getRecordByUID(uid);
        if(r != null) {
            return r.clone();
        }

        return null;
    }

    public void editRecord(Record r) {
        Record saved_r = searchRecord(r.getUID());

        if (saved_r != null) {
            saved_r.cloneAttributesFrom(r);
        }
    }

    public Record[] getRecords() {
        return this.container.toArray(Record[]::new);
    }

    /*
     * Returns a stream of clones
     */
    public Stream<Record> stream() {
        return this.container.stream();
    }

    public int getRecordCount() {
        return this.container.size();
    }

    public Record getRecordAt(int index) {
        return this.container.get(index);
    }

    /*
     * Return a list of Record clones
     */
    @Deprecated
    public List<Record> getRecordList() {
        return stream().toList();
    }

    public List<Record> getUnderlyingRecordList() {
        return this.container;
    }

    protected void rebuildIndex() {
        Record.resetObjectCounter();
        this.container.stream().forEach(Record::setNewUID);
    }
}
