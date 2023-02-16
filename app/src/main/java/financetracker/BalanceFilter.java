package financetracker;

import java.time.chrono.ChronoLocalDate;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * This class defines the logic behind Record filtering by date, and provides facilities for
 * filtering the object in a Balance.
 */
public class BalanceFilter {
    /**
     * Test if a record has a date comprised after start and before end. Extremes included.
     * @param r The record to test
     * @param start Initial date of period
     * @param end End date of period
     * @return true i the record has a date comprised, false otherwise.
     */
    public static boolean isRecordBetween(Record r, ChronoLocalDate start, ChronoLocalDate end) {
        return r.getDate().compareTo(start) >= 0 && r.getDate().compareTo(end) <= 0;
    }

    /**
     * Filters a stream of Records using the logic defined in {@link #isRecordBetween(Record, ChronoLocalDate, ChronoLocalDate)}
     * and returns a stream with filtered objects.
     * @param s A stream of Record, typically obtained from {@link Balance#stream()}
     * @param start Start date, the record must be after this
     * @param end End date, record must be before this
     * @return a stream of filtered records that are comprised between start and stop
     */
    public static Stream<Record> filterByDateStream(Stream<Record> s, ChronoLocalDate start, ChronoLocalDate end) {
        return s.filter(r -> isRecordBetween(r, start, end));
    }
}
