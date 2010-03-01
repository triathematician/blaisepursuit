/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.customizers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import sim.agent.AgentParameters;
import sim.agent.Sensor.SensorEnum;
import sim.tasks.Router.RouterEnum;

/**
 *
 * @author ae3263
 */
public class AgentPanel extends JPanel {

    AgentParameters par;

    JLabel name;
    JComboBox sensorBox;
    JComboBox routerBox;

    public AgentPanel() {
        this(new AgentParameters());
    }

    public AgentPanel(AgentParameters par) {
        this.par = par;
        initComponents();
    }

    private void initComponents() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        name = new JLabel(new ImageIcon(getClass().getResource("/gsim/icons/agent-48.png")));
        sensorBox = new JComboBox(SensorEnum.values());
        sensorBox.setRenderer(new SensorBoxRenderer());
        routerBox = new JComboBox(RouterEnum.values());
        routerBox.setRenderer(new RouterBoxRenderer());

        add(name);
        add(sensorBox);
        add(routerBox);
    }


    public static class SensorBoxRenderer extends JLabel implements ListCellRenderer {
        ImageIcon[] SENSOR_ICONS = new ImageIcon[]{
            new ImageIcon(getClass().getResource("/gsim/icons/sensor-all-48.png")),
            new ImageIcon(getClass().getResource("/gsim/icons/sensor-radial-48.png")),
            new ImageIcon(getClass().getResource("/gsim/icons/sensor-arc-48.png")),
            new ImageIcon(getClass().getResource("/gsim/icons/sensor-none-48.png"))
        };

        public SensorBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setText("");
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            SensorEnum eValue = (SensorEnum) value;

            switch (eValue) {
                case ALL : setIcon(SENSOR_ICONS[0]); break;
                case RADIAL : setIcon(SENSOR_ICONS[1]); break;
//                case ARC : setIcon(SENSOR_ICONS[2]); break;
                case NONE : setIcon(SENSOR_ICONS[3]); break;
                default : setIcon(null); break;
            }

            return this;
        }
        
    }

    public static class RouterBoxRenderer extends JLabel implements ListCellRenderer {
        ImageIcon[] PURSUIT_ICONS = new ImageIcon[]{
            new ImageIcon(getClass().getResource("/gsim/icons/pursuit-straight-48.png")),
            new ImageIcon(getClass().getResource("/gsim/icons/pursuit-leading-48.png")),
            new ImageIcon(getClass().getResource("/gsim/icons/pursuit-angle-48.png"))
        };

        public RouterBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setText("");
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            RouterEnum eValue = (RouterEnum) value;

            switch (eValue) {
                case STRAIGHT : setIcon(PURSUIT_ICONS[0]); setText(""); break;
                case LEADING : setIcon(PURSUIT_ICONS[1]); setText(""); break;
                case PLUCKER_LEADING : setIcon(PURSUIT_ICONS[1]); setText(""); break;
                case CONSTANT_BEARING : setIcon(PURSUIT_ICONS[2]); setText(""); break;
                case NOISY : setIcon(null); setText("NOISY"); break;
                default : setIcon(null); break;
            }

            return this;
        }

    }

}
