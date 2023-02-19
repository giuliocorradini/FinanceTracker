package financetracker.gui.element;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FailableDateConverter extends FailableStringConverter<LocalDate> {

    @Override
    public LocalDate fromStringWithException(String string) throws ParseException {
        try {
            return LocalDate.parse(string, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeException e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    @Override
    public String toString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
