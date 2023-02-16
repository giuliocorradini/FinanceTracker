package financetracker;

import java.util.regex.*;
import java.util.stream.*;

/**
 * This class implements free text search for Records in a given Balance.
 */
public class BalanceSearchEngine {
    private Balance base;

    /**
     * Creates a new search engine.
     * @param b balance to get the data from
     */
    public BalanceSearchEngine(Balance b) {
        this.base = b;
    }

    /**
     * Search for a pattern in all the Records reason.
     * @param query the pattern to search for. Can be a string or a regex.
     * @return a stream of Records with the matching results.
     */
    public Stream<Record> search(Pattern query) {
        return base.stream()
                .filter(r -> query.matcher(r.getReason()).find());
    }
}
