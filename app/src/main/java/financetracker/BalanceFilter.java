package financetracker;

import java.time.chrono.ChronoLocalDate;
import java.util.LinkedList;

public class BalanceFilter {
    public static Record[] getRecordsBetweenDates(Balance b, ChronoLocalDate start, ChronoLocalDate end) {
        LinkedList<Record> filtered = new LinkedList<Record>();

        for(Object obj: b.getRecords()) {
            Record r = (Record)obj;

            if(r.getDate().compareTo(start) >= 0 && r.getDate().compareTo(end) <= 0) {
                filtered.add(r.clone());
            }
        }

        return filtered.toArray(new Record[filtered.size()]);
    }
}
