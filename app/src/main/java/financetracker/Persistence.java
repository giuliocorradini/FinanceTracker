package financetracker;

import java.io.*;

public class Persistence {
    private File lastFile;

    public Persistence() {

    }

    public File getLastFile() {
        return lastFile;
    }

    public void saveData(Balance balance, String path) throws IOException {
        FileOutputStream stream = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(stream);

        try {
            if(balance != null)
                out.writeObject(balance);
        } finally {
            stream.close();
        }
    }

    public void saveData(Balance balance, File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(stream);

        try {
            if(balance != null)
                out.writeObject(balance);

            this.lastFile = file;
        } finally {
            stream.close();
        }
    }

    public static Balance loadData(String path) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(stream);

        try {
            Balance b = (Balance) in.readObject();
            b.rebuildIndex();
            return b;
        } catch(ClassNotFoundException e) {
            System.err.println("The object in file is of an unknown class. Your data might be corrupted.");
        } finally {
            stream.close();
        }

        return null;
    }

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
