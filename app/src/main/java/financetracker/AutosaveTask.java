package financetracker;

import financetracker.gui.BalanceModel;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.stream.Stream;

/*
 * This task automatically saves your work.
 */
public class AutosaveTask extends TimerTask {
    private BalanceModel model;
    private File tempFile;
    private File oldTemp;
    Persistence pmech;

    public AutosaveTask(BalanceModel m) {
        this.model = m;
        this.pmech = new Persistence();
    }

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

    public Balance loadFromTemp() throws IOException {
        return this.pmech.loadData(this.oldTemp);
    }

    public boolean checkForTempPresence() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        this.oldTemp =  Stream.of(tempDir.listFiles())
                .filter(f -> f.getName().matches("ftrack.*\\.tmp"))
                .filter(f -> !f.equals(this.tempFile))
                .findAny()
                .orElse(null);

        return this.oldTemp != null && this.oldTemp.exists();
    }

    public void cleanup() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        Stream.of(tempDir.listFiles())
                .filter(f -> f.getName().matches("ftrack.*\\.tmp"))
                .forEach(f -> f.delete());
    }
}
