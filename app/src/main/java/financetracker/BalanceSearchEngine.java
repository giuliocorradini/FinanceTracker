package financetracker;

import java.util.regex.*;
import java.util.stream.*;

/*
 * Implements a free text search for Records in a given Balance
 */
public class BalanceSearchEngine {
    private Balance base;
    public BalanceSearchEngine(Balance b) {
        this.base = b;
    }

    public Stream<Record> search(Pattern query) {
        return base.stream()
                .filter(r -> query.matcher(r.getReason()).find());
    }
}
