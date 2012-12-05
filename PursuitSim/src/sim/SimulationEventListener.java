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
public abstract class SimulationEventListener
        implements EventListener {

    /** Handles a general event. */
    public void handleSimulationEvent(SimulationEvent e) {
        if (e.message.equals("Reset"))
            handleResetEvent(e);
        else if (e.message.equals("Iteration"))
            handleIterationEvent(e);
    }

    /** Handles a reset event. */
    abstract public void handleResetEvent(SimulationEvent e);

    /** Handles an iteration event. */
    abstract public void handleIterationEvent(SimulationEvent e);

}
