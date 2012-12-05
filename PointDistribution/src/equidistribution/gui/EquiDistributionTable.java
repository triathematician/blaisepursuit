/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package equidistribution.gui;

import equidistribution.scenario.EquiTeam.Agent;
import equidistribution.scenario.EquiController;
import java.beans.PropertyChangeEvent;
import main.*;
import org.bm.blaise.specto.plane.compgeom.ColoredShapeStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import simutils.SimulationEvent;
import simutils.SimulationEventListener;

/**
 *
 * @author ae3263
 */
public class EquiDistributionTable extends JTable
        implements PropertyChangeListener, SimulationEventListener {

    EquiController controller;

    public EquiDistributionTable() {
        controller = null;
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        setModel(new ScenarioTableModel());
        setDefaultRenderer(Object.class, RENDERER);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(200, 300);
    }

    public EquiController getController() {
        return controller;
    }

    public void setController(EquiController sc) {
        if (this.controller != null) {
            sc.removeSimulationEventListener(this);
            sc.removePropertyChangeListener(this);
        }
        this.controller = sc;
        if (sc != null) {
            sc.addSimulationEventListener(this);
            sc.addPropertyChangeListener(this);
        }
    }

    int nRows = 0;

    public void updateModel() {
        if (nRows != dataModel.getRowCount()) {
            tableChanged(new TableModelEvent(dataModel, TableModelEvent.HEADER_ROW));
            nRows = dataModel.getRowCount();
        } else
            repaint();
    }

    public void propertyChange(PropertyChangeEvent evt) { updateModel(); }
    public void handleResetEvent(SimulationEvent e) { updateModel(); }
    public void handleIterationEvent(SimulationEvent e) { updateModel(); }
    public void handleGenericEvent(SimulationEvent e) {}


    //
    // MODEL FOR POINT TABLE
    //

    /** Represents an individual node's "score" */
    static double individualScore(Agent a, Double target) {
        return a.area == null || target == null ? null
                : 1 - Math.abs(1 - a.area / target);
    }

    final static String[] COLS = { "Color", "Capacity", "(x,y)", "Area", "Score" };
    final static Class[] CLASSES = { Color.class, Double.class, String.class, Double.class, Double.class };

    /** Model reflecting the underlying data */
    public class ScenarioTableModel implements TableModel {
        public int getRowCount() { return controller == null ? 0 : controller.getAgentNumber(); }
        public int getColumnCount() { return COLS.length; }
        public String getColumnName(int col) { return COLS[col]; }
        public Class<?> getColumnClass(int col) { return CLASSES[col]; }
        public Object getValueAt(int row, int col) {
            Agent a = controller.getAgent(row);
            switch(col) {
                case 0: return ColoredShapeStyle.getColor(row);
                case 1: return a.getCapacity();
                case 2: return String.format("(%.3f,%.3f)", a.x, a.y);
                case 3: return a.area;
                case 4: return individualScore(a, controller.getTargetArea());
            }
            return null;
        }
        public boolean isCellEditable(int row, int column) { return column == 1; }
        public void setValueAt(Object value, int row, int col) {
            if (col == 1)
                controller.setAgentCapacity(row, (Double) value);
        }
        public void addTableModelListener(TableModelListener l) {}
        public void removeTableModelListener(TableModelListener l) {}
    }

    public static final ScenarioTableRenderer RENDERER = new ScenarioTableRenderer();

    /** Used to render the table's custom components */
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
