package financetracker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.stream.Stream;

public class ColumnarExport extends Export {
    private Balance balance;
    private String separator;

    public ColumnarExport(Balance b, String separator) {
        this.balance = b;
        this.separator = separator;
    }

    private String recordToColumnar(Record r) {
        return String.format("%.2f%s\"%s\"%s%s\n", r.getAmount(), this.separator, r.getReason(), this.separator, r.getDate().toString());
    }

    protected void generate(OutputStream w) throws IOException {
        Locale.setDefault(new Locale("en"));

        OutputStreamWriter osw = new OutputStreamWriter(w);

        Stream<String> out = this.balance.stream().map(this::recordToColumnar);

        for(String s: out.toList()) {
            osw.write(s);
        }

        osw.flush();
    }
}
