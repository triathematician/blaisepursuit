/*
 * Simulation.java
 * Created Aug 19, 2010
 */

package simutils;

/**
 * Generic simulation interface.
 * @author elisha
 */
public interface Simulation extends Runnable {

    /** Adds a listener to the simulation */
    public void addSimulationEventListener(SimulationEventListener l);

    /** Removes a listener from the simulation */
    public void removeSimulationEventListener(SimulationEventListener l);

}
