package financetracker;

import java.io.IOException;
import java.io.Writer;
import java.util.stream.Stream;

public class CSV extends Export {
    private Balance balance;

    public CSV(Balance b, String path) {
        super(path);
        this.balance = b;
    }

    private String recordToCSV(Record r) {
        return String.format(".2lf,\"%s\",%s\n", r.getAmount(), r.getReason(), r.getDate().toEpochDay());
    }

    protected void generate(Writer w) throws IOException {
        Stream<String> out = this.balance.stream().map(r -> this.recordToCSV(r));

        for(String s: out.toList()) {
            w.write(s);
        }
    }
}
