/**
 * SimulationLogger.java
 * Created on Jul 24, 2009
 */

package simutils;

/**
 * <p>
 *   <code>AbstractSimulationLogger</code> is called during a simulation to store any data
 *   relevant to the simulation. Implementing classes will usually store data for
 *   particular elements of the simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class SimulationLogger implements SimulationEventListener {

    Simulation sim;

    public SimulationLogger(Simulation sim) {
        setSimulation(sim);
    }

    public Simulation getSimulation() { return sim; }

    public void setSimulation(Simulation sim) {
        if (this.sim != null)
            this.sim.removeSimulationEventListener(this);
        this.sim = sim;
        if (sim != null)
            sim.addSimulationEventListener(this);
    }

    public abstract void logData(Simulation src, double curTime);

    public abstract void printData();

    public void handleIterationEvent(SimulationEvent e) {
        logData(e.source, e.time);
    }

    public void handleGenericEvent(SimulationEvent e) {}

}
