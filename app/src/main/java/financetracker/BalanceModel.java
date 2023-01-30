package financetracker;

import java.util.Date;
import java.util.LinkedList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class BalanceModel implements TableModel {

    Balance b;
    LinkedList<TableModelListener> views;

    public BalanceModel() {
        b = new Balance();
        views = new LinkedList<TableModelListener>();
    }

    public BalanceModel(Balance balance) {
        b = balance;
    }

    public void addRecord() {
        b.addRecord(Record.income(100, "test income", null));
        updateListeners(new TableModelEvent(this));
    }

    @Override
    public int getRowCount() {
        return b.getRecordCount();
    }

    @Override
    public int getColumnCount() {
        return Record.getExportedFields();
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 1:
                return "Amount";
            case 2:
                return "Reason";
            case 3:
                return "Datetime";
            default:
                return "Column";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 1:
                return int.class;
            case 2:
                return String.class;
            case 3:
                return Date.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Record r = b.getRecordAt(rowIndex);

            switch(columnIndex) {
                case 1:
                    return r.getAmount();
                case 2:
                    return r.getReason();
                case 3:
                    return r.getDatetime();
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        
    }

    private void updateListeners(TableModelEvent e) {
        for(TableModelListener l: views) {
            l.tableChanged(e);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        views.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        views.remove(l);
    }
    
}
