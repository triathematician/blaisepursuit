/**
 * CaptureCondition.java
 * Created on Jul 23, 2009
 */

package sim.metrics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sim.DistanceCache;
import sim.agent.SimulationAgent;
import sim.agent.SimulationTeam;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>CaptureCondition</code> encodes a condition of capture, which depends on a particular
 *   metric between two teams.
 * </p>
 *
 * @author Elisha Peterson
 */
public class CaptureCondition {

    AgentMetrics metric = AgentMetrics.DISTANCE;
    ComparisonType type = ComparisonType.LESS;
    double threshold;
    SimulationTeam team1;
    SimulationTeam team2;

    public CaptureCondition() {
        this(null, null, 5.0);
    }

    public CaptureCondition(SimulationTeam team1, SimulationTeam team2, double threshold) {
        setTeam1(team1);
        setTeam2(team2);
        setThreshold(threshold);
    }

    @Override
    public String toString() {
        return "Capture [" + metric + "(" + team2 + ") " + type + " " + threshold + "]";
    }

    //
    // GETTERS & SETTERS
    //

    public AgentMetrics getMetric() {
        return metric;
    }

    public void setMetric(AgentMetrics metric) {
        this.metric = metric;
    }

    public String getMetricByName() {
        return metric==null ? "" : metric.name();
    }

    public void setMetricByName(String name) {
        metric = AgentMetrics.valueOf(name);
    }

    public SimulationTeam getTeam1() {
        return team1;
    }

    public void setTeam1(SimulationTeam team1) {
        this.team1 = team1;
    }

    public SimulationTeam getTeam2() {
        return team2;
    }

    public void setTeam2(SimulationTeam team2) {
        this.team2 = team2;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public ComparisonType getType() {
        return type;
    }

    public void setType(ComparisonType type) {
        this.type = type;
    }

    public String getTypeByName() {
        return type==null ? "" : type.name();
    }

    public void setTypeByName(String name) {
        type = ComparisonType.valueOf(name);
    }

    //
    // BEHAVIORAL METHODS
    //

    /** Tests to see if a capture has been made between the two agents. */
    public boolean hasBeenMet(DistanceCache dt, double curTime, AgentSensorProxy a1, AgentSensorProxy a2) {
        boolean result = type.compare(metric.getValue(dt, curTime, a1, a2), threshold);
        return result;
    }

    /**
     * Returns a list of captures made, as a map pointing from the capturing agent to the captured agent.
     */
    public Map<SimulationAgent, SimulationAgent> findCaptures(DistanceCache dt, double curTime) {
        HashMap<SimulationAgent, SimulationAgent> result = new HashMap<SimulationAgent, SimulationAgent>();
        Iterator<SimulationAgent> i1 = team1.getActiveAgents().iterator();
        while (i1.hasNext()) {
            SimulationAgent a1 = i1.next();
            for (SimulationAgent a2 : team2.getActiveAgents()) {
                if (hasBeenMet(dt, curTime, a1, a2) && !result.containsValue(a2)) {
                    result.put(a1, a2);
                    break;
                }
            }
        }
        return result;
    }
}
