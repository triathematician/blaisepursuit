/**
 * MetricLogger.java
 * Created on Jul 24, 2009
 */

package gsim.logger;

import java.awt.Color;
import java.util.ArrayList;
import sim.DistanceCache;
import sim.Simulation;
import sim.component.Obstacle;
import sim.component.VisiblePlayer;
import sim.component.agent.Agent;
import sim.metrics.AgentMetrics;

/**
 * <p>
 *   <code>MetricLogger</code> stores information about a particular metric in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class AgentMetricLogger extends MetricLogger {

    AgentMetrics metric;
    VisiblePlayer c1;
    VisiblePlayer c2;


    public AgentMetricLogger(String name, Simulation sim, AgentMetrics metric, VisiblePlayer c1, VisiblePlayer c2) {
        super(sim);
        this.name = name;
        this.metric = metric;
        this.c1 = c1;
        this.c2 = c2;
        times = new ArrayList<Double>();
        values = new ArrayList<Double>();
    }

    public AgentMetrics getMetric() {
        return metric;
    }

    public VisiblePlayer getFirst() {
        return c1;
    }

    public VisiblePlayer getSecond() {
        return c1;
    }

    public void logData(DistanceCache dt, double curTime) {
        times.add(curTime);
        values.add(metric.getValue(dt, curTime, c1, c2));
    }

    @Override
    public Color getColor() {
        return c1 instanceof Agent ? ((Agent) c1).getParameters().getColor()
                : c1 instanceof Obstacle ? ((Obstacle) c1).getColor()
                : Color.BLACK;
    }
}
