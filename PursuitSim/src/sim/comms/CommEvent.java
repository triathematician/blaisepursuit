/**
 * CommEvent.java
 * Created on Jul 20, 2009
 */

package sim.comms;

import sim.*;

/**
 * <p>
 *   <code>CommEvent</code> represents a single communications event.
 * </p>
 *
 * @author Elisha Peterson
 */
public class CommEvent implements Comparable {
    private SimulationComponent source;
    private double timeOfCreation;
    private double timeAvailable;

    /** Create an event with specified parameters.
     * @param source the class responsible for generating the event (?)
     * @param timeOfCreation the time of event creation
     * @param timeAvailable the time when the event becomes available... may be later if, e.g. the message takes a while to transmit.
     */
    public CommEvent(SimulationComponent source, double timeOfCreation, double timeAvailable) {
        // TODO (later) handle confusing constructor here.
        this.source = source;
        this.timeOfCreation = timeOfCreation;
        this.timeAvailable = timeAvailable;
    }

    /** @return source of the event. */
    public SimulationComponent getSource() {
        return source;
    }

    /** @return time stamp of the event. */
    public double getTimeOfCreation() {
        return timeOfCreation;
    }

    /** @return time when the event is first available to the receiver. */
    public double getTimeAvailableToReceiver() {
        return timeAvailable;
    }

    /** Sorts based on availability times. */
    public int compareTo(Object event2) {
        return Double.compare(timeAvailable, ((CommEvent)event2).timeAvailable);
    }

}
