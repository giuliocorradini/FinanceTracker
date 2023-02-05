package financetracker;

import java.io.*;

/*
 * Implements export in different formats
 */
public abstract class Export {
    private FileWriter file;
    private String path;

    public Export(String export_path) {
        this.path = export_path;
    }

    private void openFile() throws IOException {
        this.file = new FileWriter(path);
    }

    public void export() throws IOException {
        this.openFile();
        this.generate(this.file);
        this.file.close();
    }

    protected abstract void generate(Writer w) throws IOException;
}
