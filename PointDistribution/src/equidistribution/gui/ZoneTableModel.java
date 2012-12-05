package equidistribution.gui;

import equidistribution.scenario.EquiController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ZoneTableModel extends AbstractTableModel
        implements PropertyChangeListener {

    EquiController controller;
    List<Double> pr;

    public ZoneTableModel() {
        this(new ArrayList<Double>());
    }

    public ZoneTableModel(List<Double> priorities) {
        pr = priorities;
    }

    public EquiController getController() {
        return controller;
    }

    public void setController(EquiController sc) {
        if (this.controller != null)
            sc.removePropertyChangeListener(this);
        controller = sc;
        updatePr();
        if (sc != null)
            sc.addPropertyChangeListener(this);
        fireTableDataChanged();
    }

    private void updatePr() {
        pr = new ArrayList<Double>();
        for (Double d : controller.getZonePriority())
            pr.add(d);
    }

    public int getRowCount() {
        return pr.size();
    }

    public int getColumnCount() {
        return 3;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return row >= 0 && col == 1;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0:
                return Integer.class;
            case 1:
                return Double.class;
            case 2:
                return String.class;
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Index";
            case 1:
                return "Priority";
            case 2:
                return "Points";
        }
        return null;
    }

    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return row;
            case 1:
                return pr.get(row);
            case 2:
                return "TBI";
        }
        return null;
    }

    @Override
    public void setValueAt(Object val, int row, int col) {
        if (col == 1) {
            pr.set(row, (Double) val);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("scenario") || evt.getPropertyName().equals("zone priority") || evt.getPropertyName().equals("zones")) {
            updatePr();
            fireTableDataChanged();
        }
    }
}
