package financetracker;

import org.jetbrains.annotations.Nullable;

import java.io.*;

public class Persistence {
    private Balance balance;

    public Persistence(Balance b) {
        this.balance = b;
    }

    public Persistence() {
        this(null);
    }

    public void saveData(String path) throws IOException {
        FileOutputStream stream = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(stream);

        try {
            if(this.balance != null)
                out.writeObject(this.balance);
        } finally {
            stream.close();
        }
    }

    public static Balance loadData(String path) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(stream);

        try {
            Balance b = (Balance) in.readObject();
            return b;
        } catch(ClassNotFoundException e) {
            System.err.println("The object in file is of an unknown class. Your data might be corrupted.");
        } finally {
            stream.close();
        }

        return null;
    }
}
