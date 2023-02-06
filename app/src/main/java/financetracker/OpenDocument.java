package financetracker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import org.odftoolkit.odfdom.doc.*;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

public class OpenDocument extends Export {
    private Balance balance;

    public OpenDocument(Balance balance, String path) {
        super(path);
        this.balance = balance;
    }

    @Override
    protected void generate(OutputStream w) throws IOException {
        OdfSpreadsheetDocument ods;

        try {
            ods = OdfSpreadsheetDocument.newSpreadsheetDocument();
        } catch (Exception e) {
            System.err.println("The document cannot be created");
            return;
        }

        List<OdfTable> tables = ods.getSpreadsheetTables();
        OdfTable main_sheet = tables.get(0);

        int row = 0;

        for(Record r: this.balance.getRecords()) {
            for(int col=0; col<3; col++) {
                OdfTableCell cell = main_sheet.getCellByPosition(col, row);

                switch(col) {
                    case 0:
                        cell.setStringValue(r.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        break;
                    case 1:
                        cell.setCurrencyValue(r.getAmount(), "EUR");
                        break;
                    case 2:
                        cell.setStringValue(r.getReason());
                        break;
                }
            }

            row++;
        }

        try {
            ods.save(w);
        } catch (Exception e) {
            System.err.println("There was a problem saving the ods");
        }

    }
}
