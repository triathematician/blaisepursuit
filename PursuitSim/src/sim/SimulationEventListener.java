/**
 * SimulationEventListener.java
 * Created on Jul 28, 2009
 */

package sim;

import java.util.EventListener;

/**
 * <p>
 *   <code>SimulationEventListener</code> is a class that can respond to simulation events.
 * </p>
 *
 * @author Elisha Peterson
 */
public interface SimulationEventListener extends EventListener {

    /** Handles a simulation event. */
    public void handleSimulationEvent(SimulationEvent e);

}
