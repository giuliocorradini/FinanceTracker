package financetracker.io.export;

import financetracker.Balance;
import financetracker.Record;
import financetracker.io.export.Export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Export a balance to a columnar file, such as CSV, PSV, TSV etc.
 */
public class ColumnarExport extends Export {
    private Balance balance;
    private String separator;

    /**
     * Constructor.
     * @param b the balance to export.
     * @param separator a separator sequence to interleave the columns.
     */
    public ColumnarExport(Balance b, String separator) {
        this.balance = b;
        this.separator = separator;
    }

    /**
     * Produce a columnar string representation of a record.
     * @param r the record.
     * @return the representation.
     */
    private String recordToColumnar(Record r) {
        return String.format("%.2f%s\"%s\"%s%s\n", r.getAmount(), this.separator, r.getReason(), this.separator, r.getDate().toString());
    }

    /**
     * Generate a columnar file of the balance.
     * @param w an OutputStream to write bytes, or characters if reopened with {@link OutputStreamWriter}.
     * @throws IOException if a problem arises when writing to file.
     */
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
