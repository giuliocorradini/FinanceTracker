package financetracker;

import financetracker.gui.BalanceModel;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

/*
 * This task automatically saves your work.
 */
public class AutosaveTask extends TimerTask {
    private BalanceModel model;

    public AutosaveTask(BalanceModel m) {
        this.model = m;
    }

    @Override
    public void run() {
        synchronized (this.model) {
            Persistence pmech = new Persistence(this.model.getDao());
            try {
                pmech.saveData("tmp.dat");
            } catch (IOException e) {
                System.err.println("Can't save temporary file");
            }
        }
    }

    public static void cleanupTemp() {
        File f = new File("tmp.dat");
        f.delete();
    }

    public static Balance loadFromTemp() throws IOException {
        return Persistence.loadData("tmp.dat");
    }

    public static boolean checkForTempPresence() {
        File f = new File("tmp.dat");
        return f.exists();
    }
}