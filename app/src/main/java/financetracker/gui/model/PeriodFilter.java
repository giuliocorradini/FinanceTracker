package financetracker.gui.model;

import java.time.LocalDate;

public enum PeriodFilter {
    ALL         ("All", -1),
    THIS_DAY    ("This day", 1),
    THIS_WEEK   ("This week", 7),
    THIS_MONTH  ("This month", 30),
    THIS_YEAR   ("This year", 365),
    CUSTOM      ("Custom", 0);

    private final String descriptor;
    private final int period;

    private LocalDate startDate;

    private LocalDate endDate;
    PeriodFilter(String desc, int period) {
        this.descriptor = desc;
        this.period = period;
        this.endDate = LocalDate.now();
        this.startDate = this.endDate.minusDays(this.period);
    }

    public String toString() {
        return this.descriptor;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
