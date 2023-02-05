package financetracker;

import java.io.*;

public class Persistence {
    private FileOutputStream ofd;
    private ObjectOutputStream out_filter;
    private FileInputStream ifd;
    private ObjectInputStream in_filter;
    private String savepath;

    public Persistence(String path) {
        this.savepath = path;
    }

    public Persistence() {
        this("financetracker.db");
    }

    public void setSavepath(String path) {
        this.savepath = path;
    }

    private void openOutputFile() throws IOException {
        this.ofd = new FileOutputStream(this.savepath);
        this.out_filter = new ObjectOutputStream(this.ofd);
    }

    private void openInputFile() throws IOException {
        this.ifd = new FileInputStream(this.savepath);
        this.in_filter = new ObjectInputStream(this.ifd);
    }

    public void saveData(Balance b) {
        try {
            this.openOutputFile();
            this.out_filter.writeObject(b);
            this.out_filter.close();
            this.ofd.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public Balance loadData() {
        Balance loaded = null;

        try {
            this.openInputFile();
            loaded = (Balance) this.in_filter.readObject();
            this.ifd.close();
        } catch (IOException e) {
            System.err.println(e);
        } catch (ClassNotFoundException e) {
            System.err.println("The object in file is of an unknown class");
        }

        return loaded;
    }
}
