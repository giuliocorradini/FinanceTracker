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

    public Stream<Record> search(String query) {
        Pattern q = Pattern.compile(Pattern.quote(query));

        Stream<Record> results = base.stream()
                                     .filter(r -> q.matcher(r.getReason())
                                                             .find());

        return results;
    }
}
