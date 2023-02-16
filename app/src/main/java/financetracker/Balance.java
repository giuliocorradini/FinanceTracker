package financetracker;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.LinkedList;
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

    public synchronized void addRecord(Record r) {
        this.container.add(r);
    }

    @Deprecated
    public synchronized void addRecord(double amount, String reason, LocalDate datetime) {
        Record r = new Record(amount, reason, datetime);
        this.addRecord(r);
    }

    public synchronized void deleteRecord(Record r) {
        Record saved_record = getRecordByUID(r.getUID());
        if(saved_record != null)
            this.container.remove(saved_record);
    }

    private Record getRecordByUID(int uid) {
        for(Record r: container) {
            if(r.getUID() == uid)
                return r;
        }

        return null;
    }

    public synchronized Record[] getRecords() {
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

    protected synchronized void rebuildIndex() {
        Record.resetObjectCounter();
        this.container.stream().forEach(Record::setNewUID);
    }
}
