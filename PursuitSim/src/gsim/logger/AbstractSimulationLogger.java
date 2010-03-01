/**
 * AbstractSimulationLogger.java
 * Created on Jul 24, 2009
 */

package gsim.logger;

import sim.DistanceCache;
import sim.Simulation;
import sim.SimulationEvent;
import sim.SimulationEventListener;

/**
 * <p>
 *   <code>AbstractSimulationLogger</code> is called during a simulation to store any data
 *   relevant to the simulation. Implementing classes will usually store data for
 *   particular elements of the simulation.
 * </p>
 *
 * @author Elisha Peterson
 */
public abstract class AbstractSimulationLogger implements SimulationEventListener {

    public AbstractSimulationLogger(Simulation sim) {
        sim.addSimulationEventListener(this);        
    }

    public abstract void reset();

    public abstract void logData(DistanceCache dt, double curTime);

    public abstract void printData();

    public void handleSimulationEvent(SimulationEvent e) {
        if (e.getMessage().equals("Reset")) {
            reset();
        } else if (e.getMessage().equals("Iteration")) {
            logData(e.getDistances(), e.getSimTime());
        }
    }

}
