/**
 * AgentMetrics.java
 * Created on Jul 23, 2009
 */
package sim.metrics;

import sim.DistanceCache;
import sim.agent.AgentSensorProxy;

/**
 * <p>
 *   <code>AgentMetrics</code> represents a metric that is defined between two agents.
 * </p>
 *
 * @author Elisha Peterson
 */
public enum AgentMetrics implements Metric<AgentSensorProxy> {
    /** Computes minimum distance between the two teams. */
    DISTANCE("Distance") {
        public double getValue(DistanceCache dt, double curTime, AgentSensorProxy agent1, AgentSensorProxy agent2) {
            return agent1.getPosition().distance(agent2.getPosition());
        }
    };

    String str;
    @Override public String toString() { return str; }
    AgentMetrics(String str) { this.str = str; }
}
