/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author ae3263
 */
public class PointDistributionTable extends JTable {

    /** Stores the scenario */
    DistributionScenarioInterface scenario;

    public PointDistributionTable() {
        this.scenario = new DistributionScenario();
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        setModel(new ScenarioTableModel());
        setDefaultRenderer(Object.class, RENDERER);
    }

    public DistributionScenarioInterface getScenario() {
        return scenario;
    }

    public void setScenario(DistributionScenarioInterface scenario) {
        this.scenario = scenario;
        tableChanged(new TableModelEvent(dataModel, TableModelEvent.HEADER_ROW));
    }

    int nRows = 0;

    public void updateModel() {
        if (nRows != dataModel.getRowCount()) {
            tableChanged(new TableModelEvent(dataModel, TableModelEvent.HEADER_ROW));
            nRows = dataModel.getRowCount();
        } else
            repaint();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(200, 300);
    }


    //
    // MODEL FOR POINT TABLE
    //

    final static String[] COLS = { "Color", "x", "y", "Nearby Area" };
    final static Class[] CLASSES = { Color.class, double.class, double.class, double.class };

    public class ScenarioTableModel implements TableModel {
        public int getRowCount() { return scenario.getPoints().length; }
        public int getColumnCount() { return 4; }
        public String getColumnName(int col) { return COLS[col]; }
        public Class<?> getColumnClass(int col) { return Object.class; }
        public Object getValueAt(int row, int col) {
            switch(col) {
                case 0: return DistributionScenarioVis.getColor(row);
                case 1: return scenario.getPoints(row).x;
                case 2: return scenario.getPoints(row).y;
                case 3: return scenario.getArea(scenario.getPoints(row));
            }
            return null;
        }
        public boolean isCellEditable(int row, int column) { return false; }
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}
        public void addTableModelListener(TableModelListener l) {}
        public void removeTableModelListener(TableModelListener l) {}
    }

    public static final ScenarioTableRenderer RENDERER = new ScenarioTableRenderer();
    public static class ScenarioTableRenderer extends DefaultTableCellRenderer {
        JLabel colorLabel;

        public ScenarioTableRenderer() {
            colorLabel = new JLabel("");
            colorLabel.setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Color) {
                Color c = (Color) value;
                colorLabel.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 192));
                return colorLabel;
            } else
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
