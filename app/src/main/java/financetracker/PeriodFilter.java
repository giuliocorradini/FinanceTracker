package financetracker;

import java.time.LocalDate;

public enum PeriodFilter {
    ALL         ("All", -1),
    THIS_WEEK   ("This week", 7),
    THIS_MONTH  ("This month", 30),
    THIS_YEAR   ("This year", 365);

    private final String descriptor;
    private final int period;
    PeriodFilter(String desc, int period) {
        this.descriptor = desc;
        this.period = period;
    }

    public String toString() {
        return this.descriptor;
    }
}
