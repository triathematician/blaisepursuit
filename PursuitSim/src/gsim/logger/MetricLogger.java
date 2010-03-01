/**
 * MetricLogger.java
 * Created on Jul 24, 2009
 */

package gsim.logger;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import sim.DistanceCache;
import sim.Simulation;
import sim.metrics.Metric;

/**
 * <p>
 *   <code>MetricLogger</code> stores information about a particular metric in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class MetricLogger extends AbstractSimulationLogger {

    String name;
    Metric metric;
    Object c1;
    Object c2;

    ArrayList<Double> times;
    ArrayList<Double> values;

    public <T> MetricLogger(String name, Simulation sim, Metric<T> metric, T c1, T c2) {
        super(sim);
        this.name = name;
        this.metric = metric;
        this.c1 = c1;
        this.c2 = c2;
        times = new ArrayList<Double>();
        values = new ArrayList<Double>();
    }

    public String getName() {
        return name;
    }

    public Metric getMetric() {
        return metric;
    }

    public Object getFirst() {
        return c1;
    }

    public Object getSecond() {
        return c1;
    }

    public Point2D.Double[] getValueGraph() {
        Point2D.Double[] result = new Point2D.Double[times.size()];
        for (int i = 0; i < times.size(); i++) {
            result[i] = new Point2D.Double(times.get(i), values.get(i));
        }
        return result;
    }

    public void reset() {
        times.clear();
        values.clear();
    }

    public void logData(DistanceCache dt, double curTime) {
        times.add(curTime);
        values.add(metric.getValue(dt, curTime, c1, c2));
//        System.out.println("logging data, curtime=" + curTime + " & value = " + values.get(values.size()-1));
//        System.out.println("  c1 = " + c1 + ", c2 = " + c2);
//        System.out.println("  dt = " + dt);
    }

    public void printData() {
        System.out.println(name + " values: " + values.toString());
    }
}
