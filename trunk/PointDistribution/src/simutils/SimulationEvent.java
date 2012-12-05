/**
 * SimulationEvent.java
 * Created on Jul 28, 2009
 */

package simutils;

/**
 * <p>
 *   <code>SimulationEvent</code> is a significant simulation event that may be passed
 *   on to interested listeners.
 * </p>
 *
 * @author Elisha Peterson
 */
public class SimulationEvent {
    public Simulation source;
    public double time;
    public String message;

    public SimulationEvent(Simulation source, String message, double simTime) {
        this.source = source;
        this.message = message;
        this.time = simTime;
    }

    public Simulation getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public double getSimTime() {
        return time;
    }
}
