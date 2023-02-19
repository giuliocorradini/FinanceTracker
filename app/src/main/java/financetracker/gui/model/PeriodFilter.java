package financetracker.gui.model;

import java.time.LocalDate;

/**
 * This class represents a time window to filter BalanceModel rows.
 * The boolean attribute {@link #all} marks if the period shouldn't filter
 * and show all the records instead.
 */
public record PeriodFilter(boolean all, LocalDate startDate, LocalDate endDate) {
    /**
     * Constructor for standard period filters where start and end date are meaningful.
     * @param startDate the start date
     * @param endDate the end date
     */
    public PeriodFilter(LocalDate startDate, LocalDate endDate) {
        this(false, startDate, endDate);
    }

    /**
     * Factory for period filters that don't filter at all.
     * @return a new PeriodFilter with all field set to true.
     */
    public static PeriodFilter ALL() {
        return new PeriodFilter(true, null, null);
    }
}