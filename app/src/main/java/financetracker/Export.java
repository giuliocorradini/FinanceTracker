package financetracker;

import java.io.*;

/*
 * Implements export in different formats.
 * Uses the Execute Around pattern.
 */
public abstract class Export {
    public void export(String path) throws IOException {
        FileOutputStream f = new FileOutputStream(path);
        try {
            this.generate(f);
        } finally {
            f.close();  // This will be called, no matter what Exception arises
        }
    }

    protected abstract void generate(OutputStream w) throws IOException;
}
