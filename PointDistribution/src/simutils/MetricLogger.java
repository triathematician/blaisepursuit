/*
 * MetricLogger.java
 * Created Jul 2009
 */

package simutils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides methods for tracking a metric over time, using a simulation logger.
 * @param <N> type of value tracked
 * @author Elisha Peterson
 */
public class MetricLogger<N> extends SimulationLogger {
    
    String name;
    SimulationMetric<N> metric;
    Color color;
    
    ArrayList<Double> times;
    ArrayList<N> values;

    public MetricLogger(String name, SimulationMetric metric) {
        this(name, metric, null);
    }

    public MetricLogger(String name, SimulationMetric metric, Simulation sim) {
        super(sim);
        this.name = name;
        this.metric = metric;
        times = new ArrayList<Double>();
        values = new ArrayList<N>();
    }

    // VALUE METHODS

    public N getLastValue() { return values.get(values.size()-1); }
    public List<N> getValues() { return Collections.unmodifiableList(values); }

    // BEANS

    public Color getColor() { return color; }
    public void setColor(Color c) { color = c; }

    public String getName() { return name; }
    public void setName(String n) { name = n; }

    // INTERFACE METHODS

    public void handleResetEvent(SimulationEvent e) {
        times.clear();
        values.clear();
    }

    public void logData(Simulation src, double curTime) {
        times.add(curTime);
        values.add(metric.value(src, curTime));
    }

    public void printData() {
        System.out.println(name + " values: " + values.toString());
    }

}
