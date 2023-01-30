package financetracker;

public class App {

    public static void main(String[] args) {
        BalanceModel bm = new BalanceModel();
        MainWindow mainwin = new MainWindow(bm);

        mainwin.setSize(600, 400);
        mainwin.setVisible(true);
    }
}
