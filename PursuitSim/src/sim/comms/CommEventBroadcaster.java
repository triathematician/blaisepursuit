/**
 * CommEventBroadcaster.java
 * Created on Jul 20, 2009
 */

package sim.comms;

/**
 * <p>
 *   <code>CommEventBroadcaster</code> encapsulate a simulation component's outgoing
 *   communications events. Classes that wish to generate communications events should
 *   implement this interface!
 * </p>
 *
 * @author Elisha Peterson
 */
public interface CommEventBroadcaster {

//    /** Queues a communication event to send.
//     * @param ce the communication event to send */
//    public void addToQueue(CommEvent ce);
    
    /** Sends out communications events stored by this class
     * @param simTime current simulation time
     */
    public void sendAllCommEvents(double simTime);

}
