/**
 * Metric.java
 * Created on Jul 23, 2009
 */

package sim.metrics;

import sim.DistanceCache;

/**
 * <p>
 *   A <code>Metric</code> contains a method that computes a value associated with
 *   two entities of type <code>T</code> at a specified time and configuration of
 *   the scenario.
 * </p>
 *
 * @author Elisha Peterson
 */
public interface Metric<T> {

    /**
     * Computes and returns a value associated to 2 entities
     * @param dt current information about distances between the entities
     * @param curTime current simulation time
     * @param c1 first entity
     * @param c2 second entity
     * @return value at the specified time for the specified teams.
     */
    public double getValue(DistanceCache dt, double curTime, T c1, T c2);
}
