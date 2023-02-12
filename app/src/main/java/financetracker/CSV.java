package financetracker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.stream.Stream;

public class CSV extends Export {
    private Balance balance;

    public CSV(Balance b) {
        this.balance = b;
    }

    private String recordToCSV(Record r) {
        return String.format("%.2f,\"%s\",%s\n", r.getAmount(), r.getReason(), r.getDate().toString());
    }

    protected void generate(OutputStream w) throws IOException {
        Locale.setDefault(new Locale("en"));

        OutputStreamWriter osw = new OutputStreamWriter(w);

        Stream<String> out = this.balance.stream().map(r -> this.recordToCSV(r));

        for(String s: out.toList()) {
            osw.write(s);
        }

        osw.flush();
    }
}
