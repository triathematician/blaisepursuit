/**
 * SimulationEventListener.java
 * Created on Jul 28, 2009
 */

package simutils;

import java.util.EventListener;

/**
 * <p>
 *   <code>SimulationEventListener</code> is a class that can respond to simulation events,
 *   particularly a "reset" message and a "iteration" message.
 * </p>
 *
 * @author Elisha Peterson
 */
public interface SimulationEventListener extends EventListener {

    /** Handles a reset event. */
    public void handleResetEvent(SimulationEvent e);

    /** Handles an iteration event. */
    public void handleIterationEvent(SimulationEvent e);

    /** Handles another event. */
    public void handleGenericEvent(SimulationEvent e);

}
