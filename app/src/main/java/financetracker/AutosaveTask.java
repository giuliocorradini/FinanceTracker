package financetracker;

import financetracker.gui.model.BalanceModel;
import financetracker.io.Persistence;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * This class provides a Runnable for the auto-save facility timer,
 * that every 2 minutes (this time is defined in {@link App}) saves
 * your unfinished work, taking the lock on the {@link BalanceModel}.
 *
 * The save directory and filename are automatically sorted out by the
 * {@link java.io.File} class. The directory is usually obtained from
 * "java.io.tmpdir" system property.
 */
public class AutosaveTask extends TimerTask {
    private BalanceModel model;
    private File tempFile;
    private File oldTemp;
    Persistence pmech;

    /**
     * Creates a new AutosaveTask object.
     * @param m The data model, as used by the graphical user interface.
     *          We don't use a {@link Balance} object directly, because
     *          the underlying data access object might change after we
     *          create the object. For example if we load a file from disk.
     */
    public AutosaveTask(BalanceModel m) {
        this.model = m;
        this.pmech = new Persistence();
    }

    /**
     * This is the actual method called by Timer. It saves the current
     * data access object, obtained from the model, on a ".tmp" file.
     */
    @Override
    public void run() {
        try {
            if(this.tempFile == null)
                this.tempFile = File.createTempFile("ftrack", ".tmp");


            synchronized (this.model) {
                pmech.saveData(this.model.getDao(), this.tempFile);
            }
        } catch (IOException e) {
            System.err.println("Can't save temporary file");
        }
    }

    /**
     * Loads data from a temporary file. The temporary file is determined
     * by calling the {@link #checkForTempPresence()} method first, otherwise
     * this call will fail.
     */
    public Balance loadFromTemp() throws IOException {
        return this.pmech.loadData(this.oldTemp);
    }

    /**
     * Looks for temporary files, and tries to load the first found that
     * isn't the newly created tempfile.
     * @return true if a previous temporary file is found, false otherwise.
     */
    public boolean checkForTempPresence() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        this.oldTemp =  Stream.of(tempDir.listFiles())
                .filter(f -> f.getName().matches("ftrack.*\\.tmp"))
                .filter(f -> !f.equals(this.tempFile))
                .findAny()
                .orElse(null);

        return this.oldTemp != null && this.oldTemp.exists();
    }

    /**
     * Deletes all temporary files (current and previous) in the
     * system temporary directory.
     */
    public void cleanup() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        Stream.of(tempDir.listFiles())
                .filter(f -> f.getName().matches("ftrack.*\\.tmp"))
                .forEach(f -> f.delete());
    }
}
