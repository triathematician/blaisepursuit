/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.editor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import sim.SimulationComponent;
import sim.SimulationComposite;
import sim.agent.SimulationAgent;
import sim.agent.SimulationObstacle;
import sim.agent.SimulationPath;
import sim.agent.SimulationRandomPath;
import sim.agent.SimulationTeam;
import sim.agent.Sensor;
import sim.agent.Sensor.SensorEnum;
import sim.tasks.Router;
import sim.tasks.Router.RouterEnum;

/**
 *
 * @author ae3263
 */
public class SimComponentTransferHandler extends TransferHandler {

    public static final SimComponentTransferHandler INSTANCE = new SimComponentTransferHandler();

    //
    // IMPORT METHODS
    //

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        System.out.println(Arrays.toString(transferFlavors));
        return true;
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        String input;
        try {
            input = (String) t.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception ex) {
            return false;
        }
        System.out.println(input);

        if (comp instanceof SimulationComponentList) {
            SimulationComponentList scl = (SimulationComponentList) comp;
            SimulationComponent sc = (SimulationComponent) scl.getSelectedValue();

            SimulationComposite scc =
                    sc instanceof SimulationComposite ? (SimulationComposite) sc : scl.getSim();
            if (input.equals("Team"))
                scc.addComponent(new SimulationTeam("Team"));
            else if (input.equals("Agent"))
                scc.addComponent(new SimulationAgent("Agent"));
            else if (input.equals("Path"))
                scc.addComponent(new SimulationPath("10*cos(t)", "10*sin(t)"));
            else if (input.equals("Random Path"))
                scc.addComponent(new SimulationRandomPath());
            else if (input.equals("Obstacle"))
                scc.addComponent(new SimulationObstacle());
            else if (input.endsWith("Sensor"))
                addSensor(sc, input);
            else if (input.equals("Straight")) {
                if (sc instanceof SimulationAgent)
                    ((SimulationAgent) sc).getParameters().setRouter(Router.getInstance(RouterEnum.STRAIGHT));
            } else if (input.equals("Leading")) {
                if (sc instanceof SimulationAgent)
                    ((SimulationAgent) sc).getParameters().setRouter(Router.getInstance(RouterEnum.LEADING));
            } else if (input.equals("Plucker Leading")) {
                if (sc instanceof SimulationAgent)
                    ((SimulationAgent) sc).getParameters().setRouter(Router.getInstance(RouterEnum.PLUCKER_LEADING));
            } else if (input.equals("Constant Bearing")) {
                if (sc instanceof SimulationAgent)
                    ((SimulationAgent) sc).getParameters().setRouter(Router.getInstance(RouterEnum.CONSTANT_BEARING));
            }
            scl.updateModel();
        }
        return true;
    }

    static void addSensor(SimulationComponent sc, String s) {
        if (!(sc instanceof SimulationAgent || sc instanceof SimulationTeam))
            return;
        Sensor sensor = null;
        if (s.equals("No Sensor"))
            sensor = Sensor.getInstance(SensorEnum.NONE);
        else if (s.equals("Global Sensor"))
            sensor = Sensor.getInstance(SensorEnum.ALL);
        else if (s.equals("Radial Sensor"))
            sensor = Sensor.getInstance(SensorEnum.RADIAL);
        else if (s.equals("Wedge Sensor"))
            sensor = Sensor.getInstance(SensorEnum.WEDGE);
        else
            return;
        if (sc instanceof SimulationTeam) {
            ((SimulationTeam) sc).getParameters().setSensor(sensor);
        } else if (sc instanceof SimulationAgent) {
            ((SimulationAgent) sc).getParameters().setSensor(sensor);
        }
    }
    

}
