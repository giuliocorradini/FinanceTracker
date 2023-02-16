package financetracker;

import java.time.chrono.ChronoLocalDate;
import java.util.LinkedList;
import java.util.stream.Stream;

public class BalanceFilter {
    public static boolean isRecordBetween(Record r, ChronoLocalDate start, ChronoLocalDate end) {
        return r.getDate().compareTo(start) >= 0 && r.getDate().compareTo(end) <= 0;
    }

    public static Stream<Record> filterByDateStream(Stream<Record> s, ChronoLocalDate start, ChronoLocalDate end) {
        return s.filter(r -> isRecordBetween(r, start, end));
    }

    public static Stream<Record> filterByDateStream(Balance b, ChronoLocalDate start, ChronoLocalDate end) {
        return BalanceFilter.filterByDateStream(b.stream(), start, end);
    }
}
