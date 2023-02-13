package financetracker;

import java.time.chrono.ChronoLocalDate;
import java.util.LinkedList;
import java.util.stream.Stream;

public class BalanceFilter {
    @Deprecated
    public static Record[] getRecordsBetweenDates(Balance b, ChronoLocalDate start, ChronoLocalDate end) {
        LinkedList<Record> filtered = new LinkedList<Record>();

        for(Record r: b.getRecords()) {

            if(r.getDate().compareTo(start) >= 0 && r.getDate().compareTo(end) <= 0) {
                filtered.add(r.clone());
            }
        }

        return filtered.toArray(new Record[filtered.size()]);
    }

    public static Stream<Record> filterByDateStream(Stream<Record> s, ChronoLocalDate start, ChronoLocalDate end) {
        return s.filter(r -> r.getDate().compareTo(start) >= 0 && r.getDate().compareTo(end) <= 0);
    }

    public static Stream<Record> filterByDateStream(Balance b, ChronoLocalDate start, ChronoLocalDate end) {
        return BalanceFilter.filterByDateStream(b.stream(), start, end);
    }
}
