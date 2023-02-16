package financetracker.io.export;

import financetracker.Balance;

/**
 * This class extends {@link ColumnarExport} to implement a CSV export.
 */
public class CSV extends ColumnarExport {

    /**
     * Constructor.
     * @param b the balance to export as CSV.
     */
    public CSV(Balance b) {
        super(b, ",");
    }
}
