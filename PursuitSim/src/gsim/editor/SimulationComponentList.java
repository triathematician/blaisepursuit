/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.editor;

import data.propertysheet.PropertySheetDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import sim.Simulation;
import sim.SimComponent;
import sim.SimComposite;
import sim.component.agent.AgentParameters;
import sim.component.agent.Agent;
import sim.component.Obstacle;
import sim.component.ParametricPath;
import sim.component.RandomPath;
import sim.component.team.Team;
import sim.component.team.TeamParameters;

/**
 *
 * @author ae3263
 */
public class SimulationComponentList extends JList
        implements MouseListener {

    Simulation sim;

    //
    // CONSTRUCTORS
    //

    public SimulationComponentList() {
        this(new Simulation());
    }

    public SimulationComponentList(Simulation sim) {
        foldedComponents = new HashSet<SimComposite>();
        
        setSim(sim);
        setCellRenderer(new SimComponentRenderer());
        setSelectionBackground(new Color(232, 232, 255));
        setSelectionForeground(new Color(32, 32, 128));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        setDragEnabled(true);
        setTransferHandler(SimComponentTransferHandler.INSTANCE);

        addMouseListener(this);

        setComponentPopupMenu(getPopup());
    }

    JPopupMenu popup;

    JPopupMenu getPopup() {
        if (popup == null) {
            popup = new JPopupMenu();
            popup.add(new DeleteAction());
            popup.add(new EditAction());
        }
        return popup;
    }

    class DeleteAction extends AbstractAction {
        public DeleteAction() {
            putValue(NAME, "Delete component");
            putValue(SHORT_DESCRIPTION, "Delete selected component");
        }
        public void actionPerformed(ActionEvent e) {
            SimComponent sc = (SimComponent) getSelectedValue();
            if (sc != null) {
                sc.getParent().removeComponent(sc);
                updateModel();
            }
        }
    }
    class EditAction extends AbstractAction {
        public EditAction() {
            putValue(NAME, "Edit component");
            putValue(SHORT_DESCRIPTION, "Delete selected component");
        }
        public void actionPerformed(ActionEvent e) {
            SimComponent comp = (SimComponent) getSelectedValue();
            if (comp != null) {
                new PropertySheetDialog(null, false, comp).setVisible(true);
            }
        }
    }


    //
    // GETTERS & SETTERS
    //

    public Simulation getSim() {
        return sim;
    }

    public void setSim(Simulation sim) {
        this.sim = sim;
        updateModel();
    }

    //
    // MODEL METHODS
    //
    
    /** Stores how many parents a component has for suitable display on the screen. */
    HashMap<SimComponent, Integer> indents;
    /** List of composites whose elements are "hidden". */
    HashSet<SimComposite> foldedComponents;

    public void updateModel() {
        DefaultListModel model = new DefaultListModel();
        indents = new HashMap<SimComponent, Integer>();
        addToModel(model, sim, 0);
        setModel(model);
    }

    void addToModel(DefaultListModel model, SimComponent sc, int indent) {
        model.addElement(sc);
        indents.put(sc, indent);
        if ( !(sc instanceof SimComposite && foldedComponents.contains((SimComposite)sc)) ) {
            if (sc instanceof SimComposite)
                for (SimComponent child : ((SimComposite)sc).getComponent())
                    addToModel(model, child, indent + 1);
        }
    }

    //
    // EVENT HANDLING CODE: double-click opens up a property sheet dialog
    //

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            SimComponent comp = (SimComponent) getSelectedValue();
            if (comp instanceof SimComposite) {
                if (foldedComponents.contains((SimComposite) comp))
                    foldedComponents.remove((SimComposite) comp);
                else
                    foldedComponents.add((SimComposite) comp);
                updateModel();
            } else {
                new EditAction().actionPerformed(null);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}


    //============================================//
    //                                            //
    //               INNER CLASSES                //
    //                                            //
    //============================================//


    /** Sim Component renderer */
    public class SimComponentRenderer
            implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (! (value instanceof SimComponent))
                return new DefaultListCellRenderer().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            GenericCellRenderer r;
            if (value instanceof Agent)
                r = AGENT_RENDERER;
            else if (value instanceof Team)
                r = TEAM_RENDERER;
            else if (value instanceof ParametricPath)
                r = PATH_RENDERER;
            else if (value instanceof RandomPath)
                r = RANDOM_PATH_RENDERER;
            else if (value instanceof Obstacle)
                r = OBSTACLE_RENDERER;
            else if (value instanceof Simulation)
                r = SIM_RENDERER;
            else
                r = new GenericCellRenderer();
            r.indent = indents.get((SimComponent) value);
            return r.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    final static GenericCellRenderer SIM_RENDERER = new SimCellRenderer();
    final static GenericCellRenderer TEAM_RENDERER = new TeamCellRenderer();
    final static GenericCellRenderer AGENT_RENDERER = new AgentCellRenderer();
    final static GenericCellRenderer PATH_RENDERER = new GenericCellRenderer(SimActions.getIcon(ParametricPath.class));
    final static GenericCellRenderer RANDOM_PATH_RENDERER = new GenericCellRenderer(SimActions.getIcon(RandomPath.class));
    final static GenericCellRenderer OBSTACLE_RENDERER = new GenericCellRenderer(SimActions.getIcon(Obstacle.class));
    
    /** Generic sim renderer */
    public static class GenericCellRenderer extends JPanel
            implements ListCellRenderer {
        TitledBorder tBorder;
        JLabel typeLabel;
        int indent = 0;

        public GenericCellRenderer() {
            this(null);
        }

        public GenericCellRenderer(Icon icon) {
            super(new GridBagLayout());
            tBorder = new TitledBorder(BorderFactory.createLineBorder(Color.GRAY), "");
            setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), tBorder));
            typeLabel = new JLabel(icon);
            typeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));
            typeLabel.setVerticalTextPosition(JLabel.BOTTOM);
            typeLabel.setHorizontalTextPosition(JLabel.CENTER);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.gridheight = 2;
            add(typeLabel, c);
            c.gridx = 2; c.gridwidth = 1;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(0, 10*indent, 0, 0), tBorder));
            tBorder.setTitle(value.toString());
            
            if (isSelected) {
                tBorder.setTitleColor(list.getSelectionForeground());
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                tBorder.setTitleColor(list.getForeground());
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            
            return this;
        }
    } // INNER CLASS GenericCellRenderer

    /** Simulation renderer */
    public static class SimCellRenderer extends GenericCellRenderer {
        public SimCellRenderer() {
            super(null);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(null);
            typeLabel.setText("<html><b>"+value.toString()+"</b>");
            return this;
        }
    }

    /** Team renderer */
    public static class TeamCellRenderer extends GenericCellRenderer {
        JLabel locLabel, sensorLabel, taskerLabel,
                defSensorLabel, defTaskerLabel, defChooserLabel, defRouterLabel;
        public TeamCellRenderer() {
            super(SimActions.getIcon(Team.class));
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 3; c.gridy = 0;
            add(locLabel = new JLabel(), c);
            c.gridx++;
            add(sensorLabel = new JLabel());
            c.gridx++;
            add(taskerLabel = new JLabel());
            // put separator here
            c.gridx = 3; c.gridy++;
            add(defSensorLabel = new JLabel(), c);
            c.gridx++;
            add(defTaskerLabel = new JLabel(), c);
            c.gridx++;
            add(defChooserLabel = new JLabel(), c);
            c.gridx++;
            add(defRouterLabel = new JLabel(), c);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            TeamParameters par = ((Team)value).getParameters();
            tBorder.setBorder(BorderFactory.createLineBorder(par.getColor(), 2));
            tBorder.setTitle(par.getName());

            SimActions.updateLabel(locLabel, par.getLocationGenerator(), false, true, 24);
            SimActions.updateLabel(sensorLabel, par.getSensor(), false, true, 24);
            SimActions.updateLabel(taskerLabel, par.getTasker().length == 0 ? null : par.getTasker(0), false, true, 24);

            SimActions.updateLabel(defSensorLabel, par.getDefaultAgentParameters().getSensor(), false, true, 24);
            SimActions.updateLabel(defTaskerLabel, par.getDefaultAgentParameters().getTasker(), false, true, 24);
            SimActions.updateLabel(defChooserLabel, par.getDefaultAgentParameters().getTaskChooser(), false, true, 24);
            SimActions.updateLabel(defRouterLabel, par.getDefaultAgentParameters().getRouter(), false, true, 24);

            return this;
        }
    } // INNER CLASS TeamCellRender


    /** Agent renderer */
    public static class AgentCellRenderer extends GenericCellRenderer {
        JLabel sensorLabel, taskerLabel, chooserLabel, routerLabel;
        public AgentCellRenderer() {
            super(SimActions.getIcon(Agent.class));
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 3; c.gridy = 0;
            add(sensorLabel = new JLabel(), c);
            c.gridx++;
            add(taskerLabel = new JLabel(), c);
            c.gridx = 3; c.gridy++;
            add(chooserLabel = new JLabel(), c);
            c.gridx++;
            add(routerLabel = new JLabel(), c);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            AgentParameters par = ((Agent)value).getParameters();
            tBorder.setBorder(BorderFactory.createLineBorder(par.getColor(), 2));
            tBorder.setTitle(par.getName());

            SimActions.updateLabel(sensorLabel, par.getSensor(), false, true, 32);
            SimActions.updateLabel(taskerLabel, par.getTasker().length == 0 ? null : par.getTasker(0), false, true, 32);
            SimActions.updateLabel(chooserLabel, par.getTaskChooser(), false, true, 32);
            SimActions.updateLabel(routerLabel, par.getRouter(), false, true, 32);

            return this;
        }
    } // INNER CLASS AgentCellRenderer


}
