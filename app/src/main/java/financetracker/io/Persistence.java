package financetracker.io;

import financetracker.Balance;

import java.io.*;

/**
 * This class provides the facilities to save a Balance to file, and load a saved
 * Balance from file. It doesn't provide methods for exporting to CSV or other formats
 * as that is handled by {@link financetracker.io.export.Export Export} implementations.
 */
public class Persistence {
    private File lastFile;

    public File getLastFile() {
        return lastFile;
    }

    /**
     * Save a balance to the given file, and saves the last used filename.
     * @param balance The balance to save
     * @param file A file object representing the file to save onto.
     * @throws IOException if the file can't be open, or writing operations can't be performed.
     */
    public void saveData(Balance balance, File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(stream);

        try {
            if(balance != null) {
                synchronized (balance) {
                    out.writeObject(balance);
                }
            }

            this.lastFile = file;
        } finally {
            stream.close();
        }
    }

    /**
     * Loads data from a file, and returns a Balance if load was successful.
     * @param file
     * @return a Balance object, null if the file can't be parsed
     * @throws IOException if file can't be read.
     */
    public Balance loadData(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(stream);

        try {
            Balance b = (Balance) in.readObject();
            b.rebuildIndex();
            this.lastFile = file;
            return b;
        } catch(ClassNotFoundException e) {
            System.err.println("The object in file is of an unknown class. Your data might be corrupted.");
        } finally {
            stream.close();
        }

        return null;
    }
}
