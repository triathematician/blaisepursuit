/*
 * SimulationMetric.java
 * Created Aug 19, 2010
 */

package simutils;

/**
 * A simple metric that works with a simulation.
 * @param <N> return type of value method
 *
 * @author Elisha Peterson
 */
public interface SimulationMetric<N> {

    /** @return value of the specified simulation at the specified time */
    public N value(Simulation sim, double time);

}
