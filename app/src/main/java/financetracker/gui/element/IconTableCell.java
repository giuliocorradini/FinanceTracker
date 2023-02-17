package financetracker.gui.element;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An icon table cell that shows an icon based on its boolean value.
 * @param <T> the row model class (e.g. {@link financetracker.gui.model.RecordTableModel})
 */
public class IconTableCell<T> extends TableCell<T, Boolean> {
    private static final Image incomeIcon = new Image("icons/income.png");
    private static final Image outcomeIcon = new Image("icons/outcome.png");
    private final ImageView view;

    /**
     * Constructor.
     */
    public IconTableCell() {
        super();

        view = new ImageView();
        view.setPreserveRatio(true);
        view.setFitWidth(25);

        setGraphic(view);
    }

    /**
     * This method is called when the observable value of the cell changes. Here we set the
     * suitable icon. If the underlying value is true, we set the income icon (the green one), outcome
     * otherwise (the red one).
     * @param income The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
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
