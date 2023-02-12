package financetracker;

import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.odftoolkit.odfdom.doc.*;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

public class OpenDocument extends Export {
    private Balance balance;

    public OpenDocument(Balance balance) {
        this.balance = balance;
    }

    @Override
    protected void generate(OutputStream w) {

        try (OdfSpreadsheetDocument ods = OdfSpreadsheetDocument.newSpreadsheetDocument()) {
            List<OdfTable> tables = ods.getSpreadsheetTables();
            OdfTable main_sheet = tables.get(0);

            int row = 0;

            for(Record r: this.balance.getRecords()) {
                for(int col=0; col<3; col++) {
                    OdfTableCell cell = main_sheet.getCellByPosition(col, row);

                    switch (col) {
                        case 0 -> cell.setStringValue(r.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        case 1 -> cell.setCurrencyValue(r.getAmount(), "EUR");
                        case 2 -> cell.setStringValue(r.getReason());
                    }
                }

                row++;
            }

            ods.save(w);
        } catch (Exception e) {
            System.err.println("The document cannot be created");
        }
    }
}
