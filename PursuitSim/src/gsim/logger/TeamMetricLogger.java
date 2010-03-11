/**
 * MetricLogger.java
 * Created on Jul 24, 2009
 */

package gsim.logger;

import java.awt.Color;
import java.util.ArrayList;
import sim.DistanceCache;
import sim.Simulation;
import sim.component.team.Team;
import sim.metrics.TeamMetrics;

/**
 * <p>
 *   <code>MetricLogger</code> stores information about a particular metric in a simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public class TeamMetricLogger extends MetricLogger {

    TeamMetrics metric;
    Team c1;
    Team c2;

    public TeamMetricLogger(String name, Simulation sim, TeamMetrics metric, Team c1, Team c2) {
        super(sim);
        this.name = name;
        this.metric = metric;
        this.c1 = c1;
        this.c2 = c2;
        times = new ArrayList<Double>();
        values = new ArrayList<Double>();
    }

    public TeamMetrics getMetric() {
        return metric;
    }

    public Team getFirst() {
        return c1;
    }

    public Team getSecond() {
        return c1;
    }

    public void logData(DistanceCache dt, double curTime) {
        times.add(curTime);
        values.add(metric.getValue(dt, curTime, c1, c2));
    }

    public Color getColor() {
        return c1.getParameters().getColor();
    }
}
