/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.editor;

import gui.MPanel;
import gui.RollupPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import sim.SimulationComponent;
import sim.agent.*;
import sim.tasks.Router;
import sim.tasks.TaskChooser;
import sim.tasks.Tasker;

/**
 *
 * @author ae3263
 */
public class EditorToolPanel extends RollupPanel {

    public EditorToolPanel() {
        initComponents();
    }

    private void initComponents() {
        ToolList tl;
        add(new MPanel("Simulation Components", tl = new ToolList()));
        tl.setActions(SimActions.getActionsBySuper(SimulationComponent.class));        
        add(new MPanel("Location Generators", tl = new ToolList()));
        tl.setActions(SimActions.getActionsBySuper(LocationGenerator.class));
        add(new MPanel("Sensors", tl = new ToolList()));
        tl.setActions(SimActions.getActionsBySuper(Sensor.class));
        add(new MPanel("Taskers", tl = new ToolList()));
        tl.setActions(SimActions.getActionsBySuper(Tasker.class));
        add(new MPanel("Task Choosers", tl = new ToolList()));
        tl.setActions(SimActions.getActionsBySuper(TaskChooser.class));
        add(new MPanel("Routers", tl = new ToolList()));
        tl.setActions(SimActions.getActionsBySuper(Router.class));
        validate();
    }
    
    /** Items are displayed as icons or possibly text. */
    public static class ToolList extends JList {
        DefaultListModel model;
        public ToolList(List<Action> actions) {
            this();
            setActions(actions);
        }
        public ToolList() {
            setCellRenderer(new ListCellRenderer(){
                JLabel label = new JLabel();
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    label.setOpaque(true);
                    label.setPreferredSize(new Dimension(48, 48));
                    if (isSelected) {
                        label.setForeground(getSelectionForeground());
                        label.setBackground(getSelectionBackground());
                    } else {
                        label.setForeground(getForeground());
                        label.setBackground(getBackground());
                    }
                    Action ac = (Action) value;
                    Icon icon = (Icon) ac.getValue(Action.SMALL_ICON);
                    label.setText(icon == null ? (String) ac.getValue(Action.NAME) : null);
                    label.setIcon(icon);
                    return label;
                }
            });

            setDragEnabled(true);
            
            setLayoutOrientation(JList.HORIZONTAL_WRAP);
            setVisibleRowCount(-1);

            model = new DefaultListModel();
            setModel(model);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        
        void setActions(List<Action> actions) {
            for (Action ac : actions)
                model.addElement(ac);
        }
    }
}
