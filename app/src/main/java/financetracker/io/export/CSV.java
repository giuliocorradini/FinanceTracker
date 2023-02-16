package financetracker.io.export;

import financetracker.Balance;

public class CSV extends ColumnarExport {

    public CSV(Balance b) {
        super(b, ",");
    }
}
