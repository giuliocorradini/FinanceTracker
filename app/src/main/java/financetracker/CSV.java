package financetracker;

public class CSV extends ColumnarExport {

    public CSV(Balance b) {
        super(b, ",");
    }
}
