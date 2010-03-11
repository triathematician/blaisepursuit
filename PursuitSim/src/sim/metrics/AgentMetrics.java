/**
 * AgentMetrics.java
 * Created on Jul 23, 2009
 */
package sim.metrics;

import sim.DistanceCache;
import sim.component.VisiblePlayer;

/**
 * <p>
 *   <code>AgentMetrics</code> represents a metric that is defined between two agents.
 * </p>
 *
 * @author Elisha Peterson
 */
public enum AgentMetrics {
    /** Computes distance between two agents. */
    DISTANCE("Distance") {
        public double getValue(DistanceCache dt, double curTime, VisiblePlayer agent1, VisiblePlayer agent2) {
            return agent1.getPosition().distance(agent2.getPosition());
        }
    };

    String str;
    abstract public double getValue(DistanceCache dt, double curTime, VisiblePlayer agent1, VisiblePlayer agent2);
    @Override public String toString() { return str; }
    AgentMetrics(String str) { this.str = str; }
}
