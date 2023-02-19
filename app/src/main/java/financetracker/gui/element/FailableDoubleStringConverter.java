package financetracker.gui.element;

import java.text.NumberFormat;
import java.text.ParseException;

public class FailableDoubleStringConverter extends FailableStringConverter<Double> {
    NumberFormat fmt;

    public FailableDoubleStringConverter() {
        fmt = NumberFormat.getInstance();
    }

    @Override
    public Double fromStringWithException(String string) throws ParseException {
        return fmt.parse(string).doubleValue();
    }

    @Override
    public String toString(Double val) {
        return String.format("%.2f", val);
    }
}
