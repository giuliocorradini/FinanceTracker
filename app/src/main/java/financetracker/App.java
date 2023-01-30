package financetracker;

public class App {

    public static void main(String[] args) {
        Balance b = new Balance();

        MainWindow mainwin = new MainWindow();

        mainwin.setSize(600, 400);
        mainwin.setVisible(true);
    }
}
