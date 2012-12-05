/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsim.logger;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import sim.Simulation;
import sim.SimulationEvent;

/**
 *
 * @author ae3263
 */
public abstract class MetricLogger extends SimulationLogger {
    
    String name;
    ArrayList<Double> times;
    ArrayList<Double> values;

    public MetricLogger(Simulation sim) {
        super(sim);
    }

    public void handleResetEvent(SimulationEvent e) {
        times.clear();
        values.clear();
    }

    public String getName() {
        return name;
    }
    
    public Point2D.Double[] getValueGraph() {
        Point2D.Double[] result = new Point2D.Double[times.size()];
        for (int i = 0; i < times.size(); i++) {
            result[i] = new Point2D.Double(times.get(i), values.get(i));
        }
        return result;
    }

    public abstract Color getColor();

    public void printData() {
        System.out.println(name + " values: " + values.toString());
    }
}
