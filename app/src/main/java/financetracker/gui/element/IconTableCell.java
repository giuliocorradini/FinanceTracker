package financetracker.gui.element;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconTableCell<T> extends TableCell<T, Boolean> {
    private static final Image incomeIcon = new Image("icons/income.png");
    private static final Image outcomeIcon = new Image("icons/outcome.png");
    private final ImageView view;

    public IconTableCell() {
        super();

        view = new ImageView();
        view.setPreserveRatio(true);
        view.setFitWidth(25);

        setGraphic(view);
    }

    @Override
    protected void updateItem(Boolean income, boolean empty) {
        if(empty || income == null)
            setGraphic(null);
        else {
            view.setImage(income ? incomeIcon : outcomeIcon);
            setGraphic(this.view);
        }
    }
}
