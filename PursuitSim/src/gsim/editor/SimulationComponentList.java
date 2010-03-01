/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import sim.Simulation;
import sim.SimulationComponent;
import sim.SimulationComposite;
import sim.agent.AgentParameters;
import sim.agent.SimulationAgent;
import sim.agent.SimulationObstacle;
import sim.agent.SimulationPath;
import sim.agent.SimulationRandomPath;
import sim.agent.SimulationTeam;
import sim.agent.TeamParameters;

/**
 *
 * @author ae3263
 */
public class SimulationComponentList extends JList {

    Simulation sim;
    /** Stores how many parents a component has for suitable display on the screen. */
    HashMap<SimulationComponent, Integer> indents;

    public SimulationComponentList() {
        this(new Simulation());
    }

    public SimulationComponentList(Simulation sim) {
        setSim(sim);
        setCellRenderer(new SimComponentRenderer());
        setSelectionBackground(new Color(232, 232, 255));
        setSelectionForeground(new Color(32, 32, 128));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDragEnabled(true);
        setTransferHandler(SimComponentTransferHandler.INSTANCE);
    }

    public Simulation getSim() {
        return sim;
    }

    public void setSim(Simulation sim) {
        this.sim = sim;
        updateModel();
    }

    public void updateModel() {
        DefaultListModel model = new DefaultListModel();
        indents = new HashMap<SimulationComponent, Integer>();
        addToModel(model, sim, 0);
        setModel(model);
    }

    void addToModel(DefaultListModel model, SimulationComponent sc, int indent) {
        model.addElement(sc);
        indents.put(sc, indent);
        if (sc instanceof SimulationComposite)
        for (SimulationComponent child : ((SimulationComposite)sc).getComponent())
            addToModel(model, child, indent + 1);
    }


    /** Sim Component renderer */
    public class SimComponentRenderer
            implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (! (value instanceof SimulationComponent))
                return new DefaultListCellRenderer().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            GenericCellRenderer r;
            if (value instanceof SimulationAgent)
                r = AGENT_RENDERER;
            else if (value instanceof SimulationTeam)
                r = TEAM_RENDERER;
            else if (value instanceof SimulationPath)
                r = PATH_RENDERER;
            else if (value instanceof SimulationRandomPath)
                r = RANDOM_PATH_RENDERER;
            else if (value instanceof SimulationObstacle)
                r = OBSTACLE_RENDERER;
            else if (value instanceof Simulation)
                r = SIM_RENDERER;
            else
                r = new GenericCellRenderer();
            r.indent = indents.get((SimulationComponent) value);
            return r.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    final static GenericCellRenderer SIM_RENDERER = new SimCellRenderer();
    final static GenericCellRenderer TEAM_RENDERER = new TeamCellRenderer();
    final static GenericCellRenderer AGENT_RENDERER = new AgentCellRenderer();
    final static GenericCellRenderer PATH_RENDERER = new GenericCellRenderer(SimActions.getIcon(SimulationPath.class));
    final static GenericCellRenderer RANDOM_PATH_RENDERER = new GenericCellRenderer(SimActions.getIcon(SimulationRandomPath.class));
    final static GenericCellRenderer OBSTACLE_RENDERER = new GenericCellRenderer(SimActions.getIcon(SimulationObstacle.class));
    
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
            super(new FlowLayout(FlowLayout.LEFT));
            tBorder = new TitledBorder(BorderFactory.createLineBorder(Color.GRAY), "");
            setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), tBorder));
            typeLabel = new JLabel(icon);
            typeLabel.setBackground(Color.WHITE);
            typeLabel.setVerticalTextPosition(JLabel.BOTTOM);
            typeLabel.setHorizontalTextPosition(JLabel.CENTER);
            add(typeLabel);
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
            super(SimActions.getIcon(SimulationTeam.class));
            add(locLabel = new JLabel());
            add(sensorLabel = new JLabel());
            add(taskerLabel = new JLabel());
            // put separator here
            add(defSensorLabel = new JLabel());
            add(defTaskerLabel = new JLabel());
            add(defChooserLabel = new JLabel());
            add(defRouterLabel = new JLabel());
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            TeamParameters par = ((SimulationTeam)value).getParameters();
            tBorder.setBorder(BorderFactory.createLineBorder(par.getColor(), 2));
            tBorder.setTitle(par.getName());

            SimActions.updateLabel(locLabel, par.getLocationGenerator(), false, true);
            SimActions.updateLabel(sensorLabel, par.getSensor(), false, true);
            SimActions.updateLabel(taskerLabel, par.getTasker(), false, true);

            SimActions.updateLabel(defSensorLabel, par.getDefaultAgentParameters().getSensor(), false, true);
            SimActions.updateLabel(defTaskerLabel, par.getDefaultAgentParameters().getTasker(), false, true);
            SimActions.updateLabel(defChooserLabel, par.getDefaultAgentParameters().getTaskChooser(), false, true);
            SimActions.updateLabel(defRouterLabel, par.getDefaultAgentParameters().getRouter(), false, true);

            return this;
        }
    } // INNER CLASS TeamCellRender


    /** Agent renderer */
    public static class AgentCellRenderer extends GenericCellRenderer {
        JLabel sensorLabel, taskerLabel, chooserLabel, routerLabel;
        public AgentCellRenderer() {
            super(SimActions.getIcon(SimulationAgent.class));
            add(sensorLabel = new JLabel());
            add(taskerLabel = new JLabel());
            add(chooserLabel = new JLabel());
            add(routerLabel = new JLabel());
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            AgentParameters par = ((SimulationAgent)value).getParameters();
            tBorder.setBorder(BorderFactory.createLineBorder(par.getColor(), 2));
            tBorder.setTitle(par.getName());

            SimActions.updateLabel(sensorLabel, par.getSensor(), false, true);
            SimActions.updateLabel(taskerLabel, par.getTasker(), false, true);
            SimActions.updateLabel(chooserLabel, par.getTaskChooser(), false, true);
            SimActions.updateLabel(routerLabel, par.getRouter(), false, true);

            return this;
        }
    } // INNER CLASS AgentCellRenderer


}
