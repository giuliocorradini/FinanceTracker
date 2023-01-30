package financetracker;

import java.util.List;
import java.util.LinkedList;

public class Balance {
    List<Record> container;

    public Balance() {
        this.container = new LinkedList<Record>();
    }

    public void addRecord(Record r) {
        this.container.add(r);
    }

    public void deleteRecord(Record r) {
        this.container.remove(r);
    }

    public void editRecord(Record r) {
        //search and replace
    }

    /*
     * @return the algebraic sum of all the currently displayed records
     */
    public double getRecordSum() {
        double sum = 0;

        for (Record record : container) {
            sum += record.getAmount();
        }

        return sum;
    }

    public int getRecordCount() {
        return this.container.size();
    }

    public Record getRecordAt(int index) {
        return this.container.get(index);
    }
}
