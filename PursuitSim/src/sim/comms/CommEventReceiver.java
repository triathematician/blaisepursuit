/**
 * CommEventReceiver.java
 * Created on Jul 20, 2009
 */

package sim.comms;

/**
 * <p>
 *   <code>CommEventReceiver</code> encapsulates a simulation component's incoming communications
 *   events. Classes that wish to receive communications events should implement this interface!
 * </p>
 *
 * @author Elisha Peterson
 */
public interface CommEventReceiver {

    /** Processes received communications events
     * @param simTime the current simulation time
     */
    public void processIncomingCommEvents(double simTime);

    /** Queues up an event. */
    public void acceptEvent(CommEvent evt);

}
