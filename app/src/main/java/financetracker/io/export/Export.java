package financetracker.io.export;

import java.io.*;

/**
 * This interface defines the protocol for exporting data in different formats.
 * Uses the Execute Around pattern: in this case the method {@link #export(String)} opens
 * the file, handles the exceptions and call the {@link #generate(OutputStream)} abstract
 * method that actually performs the writing on an already open file.
 */
public abstract class Export {
    /**
     * Export the data from a Balance on a file. The format is defined by the
     * actual implementation of the {@link #generate(OutputStream)} method.
     * @param path the path of the file.
     * @throws IOException if the file can't be opened, or problem arises while
     * generating.
     */
    public void export(String path) throws IOException {
        FileOutputStream f = new FileOutputStream(path);
        try {
            this.generate(f);
        } finally {
            f.close();  // This will be called, no matter what Exception arises
        }
    }

    /**
     * Override this method in the concrete class.
     * @param w an OutputStream to write bytes, or characters if reopened with {@link OutputStreamWriter}.
     * @throws IOException because a problem might arise while writing.
     */
    protected abstract void generate(OutputStream w) throws IOException;
}
